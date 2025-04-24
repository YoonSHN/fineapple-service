const API_BASE = "/api/v1/carts";

// cart 조회
export async function fetchCart(userId) {
    const response = await fetch(`${API_BASE}/${userId}`);
    if (!response.ok) {
        throw new Error("장바구니 데이터를 불러오는 중 오류 발생");
    }
    return response.json();
}

// 장바구니 상품 목록 조회
export async function fetchCartItems(userId) {
    const response = await fetch(`${API_BASE}/${userId}/items`);
    if (!response.ok) {
        throw new Error("장바구니 상품 목록을 불러오는 중 오류 발생");
    }

    const data = await response.json(); // 서버에서 받은 데이터

    // 총 가격 계산 (상품 가격 * 수량 합산)
    const totalPrice = data.reduce((total, item) => total + item.productPrice * item.quantity, 0);

    return {
        cartItems: data,  // 장바구니 상품 목록
        totalPrice: totalPrice,  // 총 가격

    };
}

// 장바구니에 상품 추가
export async function addCartItem(userId, productId, quantity) {
    const response = await fetch(`${API_BASE}/${userId}/items`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',  // JSON 형식으로 데이터를 보냄
        },
        body: JSON.stringify({ productId, quantity }),  // JSON 형태로 데이터 전송
    });
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "상품을 장바구니에 추가하는 데 실패");
    }
}

// 수량 변경
export async function updateCartItem(userId, productId, quantity) {
    const response = await fetch(`${API_BASE}/${userId}/${productId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ quantity }),
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "장바구니 수량 변경 실패");
    }
}

// 장바구니에서 상품 제거
export async function removeCartItem(userId, productId) {
    const response = await fetch(`${API_BASE}/${userId}/items/${productId}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "상품을 장바구니에서 삭제하는 데 실패");
    }
}

// 장바구니 초기화
export async function initializeCart(userId) {
    const response = await fetch(`${API_BASE}/${userId}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "장바구니 초기화 실패");
    }
}
