import { fetchCartItems, addCartItem, removeCartItem } from './cartApi.js';
import { renderCartItems, setupRemoveButtons } from './cartUI.js';

let userId;

// DOM 로드 후 장바구니 데이터 표시
document.addEventListener("DOMContentLoaded", async () => {
    userId = document.getElementById('user-info').getAttribute('data-user-id');
    console.log("userId:", userId); // 값 확인용

    try {
        // 장바구니 상품 목록 조회
        const { cartItems, totalPrice} = await fetchCartItems(userId);  // cartItems와 totalPrice를 구조 분해 할당

        // 데이터를 제대로 받았는지 확인
        // console.log(cartItems, totalPrice);

        // 장바구니에 데이터가 있으면 렌더링
        if (cartItems && cartItems.length > 0) {
            renderCartItems(cartItems, totalPrice, userId);  // totalPrice를 함께 전달
            setupRemoveButtons(userId);
        } else {
            console.log("장바구니가 비어 있습니다.");
        }

    } catch (error) {
        console.error("장바구니 데이터를 불러오는 중 오류 발생:", error);
    }
});

// 장바구니에 상품 추가 버튼 이벤트 (상품 목록에서 버튼 클릭 시)
document.querySelectorAll(".add-to-cart-btn").forEach(btn => {
    btn.addEventListener("click", async (e) => {
        userId = document.getElementById('user-info').getAttribute('data-user-id');  // 실제 로그인된 사용자 ID
        const productId = e.target.dataset.productId;
        const quantity = 1; // 기본 1개 추가

        await addCartItem(userId, productId, quantity);
        alert("장바구니에 추가되었습니다!");

        const { cartItems, totalPrice } = await fetchCartItems(userId);
        renderCartItems(cartItems, totalPrice, userId);
        setupRemoveButtons(userId); // 버튼 이벤트 다시 등록
    });
});