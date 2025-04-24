const API_BASE = "/api/v1/product"

document.addEventListener("DOMContentLoaded", async () => {
    await loadProductDetail();

});

// 상품 정보 불러오기
async function loadProductDetail() {
    //모델에서 넘겨받은 productId
    const productId = document.getElementById("product-detail-data").getAttribute('data-product-id');
    if (!productId) {
        console.error("상품 ID가 없습니다.");
        return;
    }

    try {
        const product = await fetchProductDetail(productId);
        renderProductDetail(product);
    } catch (error) {
        console.error("상품 정보를 불러오는 데 실패했습니다.", error);
    }
}


// API 호출
async function fetchProductDetail(productId) {
    const response = await fetch(`${API_BASE}/${productId}`);
    if (!response.ok) throw new Error("상품 정보를 가져오지 못했습니다.");
    return response.json();
}

// UI 렌더링
function renderProductDetail(product) {
    const itemDetail = document.getElementById("item-detail");


    //totalPrice , subTotalPrice에 3자리마다 , 추가해서 가독성 향상 ex)  1,000,000
    function formatPrice(price) {
        return new Intl.NumberFormat('ko-KR').format(price);
    }
    //Html 초기화
    itemDetail.innerHTML = '';

    itemDetail.innerHTML =`
        <div id="product-image">
            <img src="${product.imageUrl}">
        </div>
        <div id="product-info">
            <p>${product.name}</p>
            <p>${product.description}</p>
            <select> opt1</select>
            <select> opt2</select>
            <p id="price"> &#8361; ${formatPrice(product.price)}</p>
            <button id="add-to-cart"> 담 기 </button>
        </div>
    `;
    document.getElementById("add-to-cart").addEventListener("click", addToCart);
}

// 장바구니 추가
async function addToCart() {
    const productId = document.getElementById("product-detail-data").getAttribute('data-product-id');
    const userId = document.getElementById("product-detail-data").getAttribute('data-user-id');
    const quantity = 1;

    if (!productId || !userId) {
        console.error("유효한 productId 또는 userId가 없습니다.");
        return;
    }

    const url = `/api/v1/carts/${userId}/items?productId=${productId}&quantity=${quantity}`;

    try {
        const response = await fetch(url, {
            method: "POST",
        });

        if (!response.ok) throw new Error("장바구니 추가 실패");

        alert("장바구니에 추가되었습니다.");
    } catch (error) {
        console.error("장바구니 추가 중 오류 발생:", error);
        alert("장바구니 추가에 실패했습니다.");
    }
}