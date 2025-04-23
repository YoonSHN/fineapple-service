import {fetchCartItems, removeCartItem, updateCartItem} from "./cartApi.js";

//입력이 끝나고 일정 시간(delay)가 지나면 실행되는기능
//수량변경시마다 실시간으로 업데이트하면 서버에 부하
function debounce(callback, delay) {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => {
            callback.apply(this, args);
        }, delay);
    };
}

//업데이트 입력값 받아오는 딜레이 0.5초
const updateQuantityOnServer = debounce(async (productId, quantity, userId) => {
    try {
        await updateCartItem(userId, productId, quantity);
        console.log("서버에 수량 업데이트 완료:", quantity);
    } catch (err) {
        console.error("수량 변경 중 오류:", err);
    }
}, 500);

export function renderCartItems(cartItems, totalPrice, userId) {

    //totalPrice , subTotalPrice에 3자리마다 , 추가해서 가독성 향상 ex)  1,000,000
    function formatPrice(price) {
        return new Intl.NumberFormat('ko-KR').format(price);
    }

    const cartContainer = document.getElementById("cart-items");
    const totalPriceEl = document.getElementById("total-price");

    cartContainer.innerHTML = ""; // 기존 목록 초기화
    let optionTotalPrice = 0;

    cartItems.forEach(item => {
        const cartItem = document.createElement("div");
        cartItem.classList.add("cart-item");

        // item.productPrice는 BigDecimal이므로, 숫자로 변환 필요
        const price = parseFloat(item.subTotal); // BigDecimal로 넘어왔으면 숫자로 변환
        let optionPrice = 0;

        let optionsDisplay = '';
        if (item.options) {
            const options = typeof item.options === 'string' ? JSON.parse(item.options) : item.options;
            const optionKeys = Object.keys(options);
            optionsDisplay = optionKeys.join(', ');

            optionKeys.forEach(key => {
                optionPrice += parseFloat(options[key]); // 옵션 가격 계산
            });
            optionPrice *= item.quantity; // 수량만큼 곱함
        }
        console.log(item);

        cartItem.innerHTML = `
            <img src="${item.productUrl}" />
            <div id="item-name">
                <p>${item.productName}</p>
                <p>${optionsDisplay}</p>
            </div>
            <input class="quantity" type="number" value="${item.quantity}" min="1" data-id="${item.productId}" />
            <div class="price">
                <p> &#8361;  ${formatPrice(price)}</p>
                ${optionPrice > 0 ? `<p> &#8361; +( ${formatPrice(optionPrice)} )</p>` : ''}
            </div>
            <button class="remove-btn" data-id="${item.productId}">취소</button>
        `;

        // <button className="remove-btn" data-id="${item.productId}">X</button>
        cartContainer.appendChild(cartItem);

        // 수량 변경 이벤트 등록
        const quantityInput = cartItem.querySelector(".quantity");
        const priceInfo = cartItem.querySelector(".price");

        quantityInput.addEventListener("input", (e) => {
            const newQuantity = parseInt(e.target.value);
            if (isNaN(newQuantity) || newQuantity < 1) return;

            item.quantity = newQuantity;

            // 옵션 가격 재계산
            const options = typeof item.options === 'string' ? JSON.parse(item.options) : item.options;
            const optionKeys = Object.keys(options);

            let newOptionPrice = 0;
            optionKeys.forEach(key => {
                newOptionPrice += parseFloat(options[key]);
            });
            newOptionPrice *= newQuantity;

            // 상품 가격 재계산
            const basePrice = parseFloat(item.productPrice) * newQuantity;

            // 가격 영역 업데이트
            priceInfo.innerHTML = `
                <p>&#8361; ${formatPrice(basePrice)}</p>
                ${newOptionPrice > 0 ? `<p>&#8361; +( ${formatPrice(newOptionPrice)} )</p>` : ''}
            `;

            // 총합 가격 재계산
            let newTotalPrice = 0;
            cartItems.forEach(ci => {
                const q = ci.quantity;
                const base = parseFloat(ci.productPrice) * q;
                let opt = 0;
                const opts = typeof ci.options === 'string' ? JSON.parse(ci.options) : ci.options;
                Object.values(opts || {}).forEach(v => opt += parseFloat(v));
                opt *= q;
                newTotalPrice += base + opt;
            });
            totalPriceEl.textContent = formatPrice(newTotalPrice);

            // 서버에 수량 업데이트
            updateQuantityOnServer(item.productId, newQuantity, userId);
        });
    });

    // 총 가격 표시 (DTO에서 전달받은 totalPrice 사용)
    totalPriceEl.textContent = formatPrice(totalPrice);
}

// 삭제 버튼 이벤트 처리
export function setupRemoveButtons(userId) {
    document.querySelectorAll(".remove-btn").forEach(btn => {
        btn.addEventListener("click", async (e) => {
            const productId = e.target.dataset.id;
            try {
                await removeCartItem(userId, productId);

                // 삭제 후 장바구니 데이터 새로 불러오기
                const { cartItems, totalPrice } = await fetchCartItems(userId);
                renderCartItems(cartItems, totalPrice, userId);

                // 삭제 후 버튼 이벤트 다시 설정 (UI가 갱신되었으므로)
                setupRemoveButtons(userId);

            } catch (error) {
                console.error("상품 삭제 중 오류 발생:", error);
            }
        });
    });
}