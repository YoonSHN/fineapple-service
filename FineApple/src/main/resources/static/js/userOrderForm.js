document.addEventListener("DOMContentLoaded", async () => {
    const itemsContainer = document.getElementById("selectedItemsContainer");
    const form = document.getElementById("orderForm");
    const resultDiv = document.getElementById("orderResult");
    const totalPriceDisplay = document.getElementById("totalPriceDisplay");

    const selectedItems = JSON.parse(localStorage.getItem("selectedItems") || "[]");
    const userId = localStorage.getItem("userId") || null;
    const guestId = localStorage.getItem("guestId") || null;
    const cartId = localStorage.getItem("cartId") || null;

    if (selectedItems.length === 0) {
        itemsContainer.innerHTML = "<p>선택된 상품이 없습니다.</p>";
        return;
    }

    let totalPrice = 0;
    selectedItems.forEach(item => {
        const itemDiv = document.createElement("div");
        itemDiv.textContent = `${item.productName} - ${item.price.toLocaleString()}원 (x${item.quantity})`;
        itemsContainer.appendChild(itemDiv);
        totalPrice += item.price * item.quantity;
    });
    // 총 결제 금액 표시
    if (totalPriceDisplay) {
        totalPriceDisplay.textContent = totalPrice.toLocaleString();
    }
    // 주문자 정보 자동 채움 (회원일 경우)
    if (userId) {
        try {
            const response = await fetch(`/api/v1/users/${userId}`);
            if (response.ok) {
                const userData = await response.json();
                document.getElementById("customerName").value = userData.name || "";
                document.getElementById("address").value = userData.address || "";
                document.getElementById("addressDetail").value = userData.addressDetail || "";
                document.getElementById("contact").value = userData.contact || "";
                document.getElementById("email").value = userData.email || "";
            } else {
                console.warn("회원 정보 불러오기 실패");
            }
        } catch (err) {
            console.error("회원 정보 조회 오류:", err);
        }
    }

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const name = document.getElementById("customerName").value.trim();
        const address = document.getElementById("address").value.trim();
        const addressDetail = document.getElementById("addressDetail").value.trim();
        const contact = document.getElementById("contact").value.trim();
        const email = document.getElementById("email").value.trim();
        const paymentMethod = document.getElementById("paymentMethod").value;

        if (!name || !address || !addressDetail || !contact) {
            alert("주문자 정보를 모두 입력해주세요.");
            return;
        }

        // 아임포트 초기화
        const IMP = window.IMP;
        IMP.init(IMP_CODE);

        IMP.request_pay({
            pg: 'html5_inicis.INIpayTest', // 테스트 PG
            pay_method: "card",
            merchant_uid: `order_${Date.now()}`,
            name: "주문 상품",
            amount: totalPrice,
            buyer_name: name,
            buyer_tel: contact,
            buyer_addr: `${address} ${addressDetail}`,
            buyer_email: email,
        }, async function (rsp) {
            if (rsp.success) {
                // 결제 성공 시 주문 생성
                const discountPrice = 0;

                const orderData = {
                    userId: userId ? parseInt(userId) : null,
                    guestId: guestId ? parseInt(guestId) : null,
                    cartId: parseInt(cartId),
                    totalPrice: totalPrice,
                    discountPrice: discountPrice,
                    finalPrice : (totalPrice-discountPrice),
                    paymentMethod: "OR0501", // 카드
                    userName: name,
                    userAddress: address,
                    userAddressDetail: addressDetail,
                    userPhone: contact,
                    userEmail: email,
                    orderItems: selectedItems.map(item => ({
                        productId: parseInt(item.productId),
                        itemName: item.productName,
                        optionId: item.optionId !== null && item.optionId !== undefined ? item.optionId : 1,
                        itemQuantity: item.quantity,
                        itemPrice: item.price,
                        itemDiscountPrice: 0
                    }))
                };
                console.log("최종 전송 데이터:", JSON.stringify(orderData, null, 2));
                try {
                    const response = await fetch("/api/v1/orders", {
                        method: "POST",
                        headers: {"Content-Type": "application/json"},
                        body: JSON.stringify(orderData)
                    });

                    if (!response.ok) {
                        const errText = await response.text();
                        console.error("서버 응답 본문:", errText);
                        throw new Error("주문 실패");
                    }

                    const orderResponse = await response.json();
                    // const orderCode = BigInt(orderResponse.orderCode).toString(); //2025041717110641444 -> 2025041717110641400
                    const orderCode = orderResponse.orderCode.toString();
                    const orderId = orderResponse.orderId;

                    console.log("서버 응답(orderResponse):", orderResponse);
                    console.log("orderId:", orderId);
                    console.log("orderCode:", orderCode);
                    // 결제 완료 후 /payments/complete 호출
                    const completeResponse = await fetch("/api/v1/payments/complete", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify({
                            impUid: rsp.imp_uid, // 아임포트 결제 고유 ID
                            orderId: orderId, // 주문 코드
                            orderCode: orderCode
                        })
                    });

                    if (!completeResponse.ok) {
                        throw new Error("결제 완료 처리에 실패했습니다.");
                    }

                    // 주문 상세 조회
                    const detailResponse = await fetch(`/api/v1/orders/${orderCode}/orderItemDetails`);
                    if (!detailResponse.ok) {
                        throw new Error("주문 상세 조회 실패");
                    }

                    const detailText = await detailResponse.text(); // 먼저 text로 받음

                    let orderDetail = null;
                    if (detailText) {
                        try {
                            orderDetail = JSON.parse(detailText); // 본문이 있을 경우만 JSON 파싱
                        } catch (err) {
                            console.error("주문 상세 JSON 파싱 실패:", err);
                            throw new Error("응답 파싱 중 오류 발생");
                        }
                    } else {
                        console.warn("주문 상세 응답 본문이 비어있습니다.");
                        throw new Error("주문 상세 응답이 비어있습니다.");
                    }

                    localStorage.setItem("lastOrderCode", orderCode);
                    localStorage.setItem("lastOrderDetail", JSON.stringify(orderDetail));

                    window.location.href = "/payment-complete";

                } catch (error) {
                    console.error("주문 오류:", error);
                    resultDiv.innerHTML = `<p style="color:red;">주문 처리 중 오류 발생: ${error.message}</p>`;
                }
            } else {
                alert("결제에 실패하였습니다: " + rsp.error_msg);
            }
        });
    });
});
