document.addEventListener("DOMContentLoaded", () => {
    const checkoutBtn = document.getElementById("checkout-btn");

    const userInfoDiv = document.getElementById("user-info");
    const userId = userInfoDiv?.getAttribute("data-user-id");

    if (checkoutBtn) {
        checkoutBtn.addEventListener("click", () => {
            const cartItems = document.querySelectorAll(".cart-item");

            const selectedItems = Array.from(cartItems).map(item => {
                const productId = item.querySelector(".remove-btn").dataset.id;
                const productName = item.querySelector("#item-name p").textContent;
                const quantity = parseInt(item.querySelector(".quantity").value);
                const priceText = item.querySelector(".price p")?.textContent || "0";
                const price = parseInt(priceText.replace(/[^\d]/g, '')) || 0;

                return {
                    productId,
                    productName,
                    quantity,
                    price
                };
            });

            if (!userId) {
                alert("로그인이 필요합니다.");
                return;
            }

            // userId는 전역변수라고 가정
            localStorage.setItem("selectedItems", JSON.stringify(selectedItems));
            localStorage.setItem("userId", userId);

            window.location.href = "/CreateOrderPayment";
        });
    }
});
