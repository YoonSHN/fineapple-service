$(document).ready(function () {
    // let userId = window.loggedInUserId || null;
    let guestId = window.guestSessionId || null;

    const userIdEl = document.getElementById("user-id-data");
    const userId = userIdEl ? userIdEl.getAttribute("data-user-id") : null;

    if (!userId || userId === "null" || userId.trim() === "") {
        console.warn("❗ 유효한 userId가 없어 장바구니 요청 생략");
    } else {
        fetch(`/api/v1/carts/${userId}`)
            .then(response => response.json())
            .then(data => console.log("🛒 장바구니:", data))
            .catch(error => console.error("장바구니 오류:", error));
    }
    const id = userId ? userId : guestId;

    console.log("✅ 최종 id:", id);

    if (!id || id === "null" || id === "") {
        console.warn("❗ 사용자 ID를 찾을 수 없습니다. 주문 목록 요청을 중단합니다.");
        return;
    }

    fetchOrders(1);

    function fetchOrders(pageNum) {
        fetch(`/api/v1/orders/${id}?pageNum=${pageNum}&pageSize=10`)
            .then(response => response.json())
            .then(data => {
                console.log("📦 API 응답:", data);
                const tableBody = document.getElementById('orderTableBody');
                const emptyMessage = document.getElementById('emptyOrderMessage');
                const pagination = document.getElementById('pagination');
                tableBody.innerHTML = "";
                pagination.innerHTML = "";

                if (data.list && data.list.length > 0) {
                    emptyMessage.style.display = "none";

                    data.list.forEach(order => {
                        const row = document.createElement('tr');
                        let formattedDate = '-';
                        if (order.createdAt) {
                            const date = new Date(order.createdAt);
                            if (!isNaN(date)) {
                                formattedDate = date.toLocaleString('ko-KR', {
                                    year: 'numeric',
                                    month: '2-digit',
                                    day: '2-digit',
                                    hour: '2-digit',
                                    minute: '2-digit'
                                });
                            }
                        }
                        console.log("📅 createdAt:", order.createdAt);
                        row.innerHTML = `
                            <td><a href="/user/orders/detail/${order.orderCode}">${order.orderCode}</a></td>
                            <td>${formattedDate}</td>
                            <td>${order.totalPrice.toLocaleString()}원</td>
                            <td>${order.orderStatusName}</td>
                            <td>${order.paymentMethodName}</td>
                        `;
                        tableBody.appendChild(row);
                    });

                    createPagination(data.pages, data.pageNum);
                } else {
                    emptyMessage.style.display = "block";
                }
            })
            .catch(error => console.error('주문 목록을 불러오는 중 오류 발생:', error));
    }

    function createPagination(totalPages, currentPage) {
        const pagination = document.getElementById('pagination');

        const prevBtn = document.createElement('button');
        prevBtn.textContent = '이전';
        prevBtn.disabled = currentPage === 1;
        prevBtn.addEventListener('click', () => fetchOrders(currentPage - 1));
        pagination.appendChild(prevBtn);

        for (let i = 1; i <= totalPages; i++) {
            const btn = document.createElement('button');
            btn.textContent = i;
            btn.className = i === currentPage ? 'active' : '';
            btn.addEventListener('click', () => {
                fetchOrders(i);
            });
            pagination.appendChild(btn);
        }

        const nextBtn = document.createElement('button');
        nextBtn.textContent = '다음';
        nextBtn.disabled = currentPage === totalPages;
        nextBtn.addEventListener('click', () => fetchOrders(currentPage + 1));
        pagination.appendChild(nextBtn);
    }
});