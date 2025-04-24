document.addEventListener('DOMContentLoaded', function () {
    const userOrderDetailContent = document.getElementById('userOrderDetailContent');
    if (!userOrderDetailContent) {
        console.error('주문 상세 정보를 표시할 요소를 찾을 수 없습니다.');
        return;
    }
    const urlParts = window.location.pathname.split('/');
    const orderCode = urlParts[urlParts.length - 1]; // URL의 마지막 부분이 orderCode

    userOrderDetailContent.innerHTML = '<p>로딩 중...</p>'; // 로딩 중 표시

    fetch(`/api/v1/orders/${orderCode}/orderItemDetails`)
        .then(response => response.json())
        .then(data => {
            const content = document.getElementById('userOrderDetailContent');
            console.log("📦 API 응답 데이터:", data);
            let html = `
      <p>주문 번호: ${data.orderCode || '정보 없음'}</p>
      <p>주문 날짜: ${data.createdAt ? new Date(data.createdAt).toLocaleString('ko-KR') : '정보 없음'}</p>
      <p>주문 상태: ${data.orderStatusName || '정보 없음'}</p>
      <p>결제 수단: ${data.paymentMethodName || '결제 수단 정보 없음'}</p>
      <p>주문자: ${data.userName || '주문자 정보 없음'}</p>
      <p>주소: ${data.userAddress ? data.userAddress + ' ' + data.userAddressDetail : '주소 정보 없음'}</p>
      <h4>상품 목록</h4>
      <ul>
    `;

            if (data.orderItems && data.orderItems.length > 0) {
                data.orderItems.forEach(item => {
                    html += `
          <li>
            <p>상품명: ${item.itemName || '정보 없음'}</p>
            <p>수량: ${item.itemQuantity ?? '정보 없음'}</p>
            <p>가격: ${item.itemPrice?.toLocaleString() ?? '정보 없음'}원</p>
            <p>할인 가격: ${item.itemDiscountPrice?.toLocaleString() ?? '정보 없음'}원</p>
            <hr/>
          </li>
        `;
                });
            } else {
                html += `<li>주문한 상품이 없습니다.</li>`;
            }

            html += `</ul>`;

            content.innerHTML = html;
        })
        .catch(error => {
            console.error('주문 상세 정보를 불러오는 중 오류 발생:', error);
            document.getElementById('userOrderDetailContent').innerHTML = '<p>오류가 발생했습니다.</p>';
        });
});