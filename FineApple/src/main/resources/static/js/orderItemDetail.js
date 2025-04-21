import {statusMapping} from "./utils.js";

export function initOrderItemDetail() {
    const itemDetailId = sessionStorage.getItem('selectedOrderItemDetailId');
    if (itemDetailId) {
        fetchOrderItemDetailPage(itemDetailId, 1, 10);
    }
    bindPaginationEvents();
}

export async function fetchOrderItemDetailPage(itemDetailId, pageNum = 1, pageSize = 10) {
    try {
        const detailResp = await fetch(`/api/v1/admin/orders/orderItemDetails/${itemDetailId}`);
        if (!detailResp.ok) throw new Error(`HTTP error! status: ${detailResp.status}`);
        const itemDetail = await detailResp.json();

        const historyUrl = `/api/v1/admin/orders/itemDetail/${itemDetailId}/history?pageNum=${pageNum}&pageSize=${pageSize}`;
        const historyResp = await fetch(historyUrl);
        if (!historyResp.ok) throw new Error(`HTTP error! status: ${historyResp.status}`);
        const historyResult = await historyResp.json();

        renderItemDetail(itemDetail);
        renderItemDetailHistory(historyResult.list);

        totalPages = (historyResult.pages && historyResult.pages >= 1) ? historyResult.pages : 1;
        currentPage = historyResult.pageNum ?? 1;
        renderPageNumbers();
    } catch (err) {
        console.error("Error in fetchOrderItemDetailPage:", err);
    }
}


function renderItemDetail(itemDetail) {
    const container = document.getElementById('item-detail-body');
    if (!container) {
        console.error("item-detail-body not found");
        return;
    }

    container.innerHTML = `
    <div class="item-detail-card">
      <div class="item-detail-image">
        ${
        itemDetail.imageUrl
            ? `<img src="${itemDetail.imageUrl}" alt="상품 이미지">`
            : '<div class="image-placeholder">No Image</div>'
    }
      </div>

      <div class="info-sections">

        <div class="info-box">
          <h3>주문 & 아이템 정보</h3>
          <div><strong>주문 ID:</strong> ${itemDetail.orderId || '-'}</div>
          <div><strong>주문 코드:</strong> ${itemDetail.orderCode || '-'}</div>
          <div><strong>주문 상태:</strong> ${statusMapping[itemDetail.orderStatus] || itemDetail.orderStatus || '-'}</div>
          <div><strong>취소 여부:</strong> ${itemDetail.isCancelled ? 'YES' : 'NO'}</div>
          <hr>
          <div><strong>주문 상세 ID:</strong> ${itemDetail.orderItemDetailId || '-'}</div>
          <div><strong>상품명:</strong> ${itemDetail.itemName || '-'}</div>
          <div><strong>수량:</strong> ${itemDetail.quantity || 0}</div>
          <div><strong>가격:</strong> ${itemDetail.price ? itemDetail.price.toLocaleString() + '원' : '-'}</div>
          <div><strong>할인 가격:</strong> ${itemDetail.discountPrice ? itemDetail.discountPrice.toLocaleString() + '원' : '-'}</div>
          <div><strong>쿠폰 적용:</strong> ${itemDetail.couponApplied ? '예' : '아니오'}</div>
          <div><strong>추가 가격:</strong> ${itemDetail.additionalPrice ? itemDetail.additionalPrice.toLocaleString() + '원' : '-'}</div>
          <div><strong>추가 사항:</strong> ${itemDetail.additional || '-'}</div>
          <div><strong>상품 상태:</strong> ${statusMapping[itemDetail.itemStatus] || itemDetail.itemStatus || '-'}</div>
        </div>

        <div class="info-box">
          <h3>결제 정보</h3>
          <div><strong>결제 방식(주문):</strong> ${statusMapping[itemDetail.paymentMethod] || itemDetail.paymentMethod || '-'}</div>
          <hr>
          <div><strong>결제 ID:</strong> ${itemDetail.paymentId || '-'}</div>
          <div><strong>결제 상태:</strong> ${statusMapping[itemDetail.paymentStatus] || itemDetail.paymentStatus || '-'}</div>
          <div><strong>결제 총 금액:</strong> ${itemDetail.paymentTotal ? itemDetail.paymentTotal.toLocaleString() + '원' : '-'}</div>
          <div><strong>결제 요청 일시:</strong> ${itemDetail.paymentRequestedAt || '-'}</div>
          <div><strong>결제 완료 일시:</strong> ${itemDetail.paidAt || '-'}</div>
          <div><strong>결제 취소 일시:</strong> ${itemDetail.paymentCancelledAt || '-'}</div>
          <div><strong>결제 상세 수단:</strong> ${statusMapping[itemDetail.paymentMethodDetail] || itemDetail.paymentMethodDetail || '-'}</div>
          <hr>
          <div><strong>결제 상세 ID:</strong> ${itemDetail.paymentDetailId || '-'}</div>
          <div><strong>결제 상품명:</strong> ${itemDetail.paymentProductName || '-'}</div>
          <div><strong>결제 수량:</strong> ${itemDetail.paidQuantity || '-'}</div>
          <div><strong>결제 금액:</strong> ${itemDetail.paidAmount ? itemDetail.paidAmount.toLocaleString() + '원' : '-'}</div>
          <div><strong>취소 수량:</strong> ${itemDetail.cancelledQuantity ?? '-'}</div>
          <div><strong>취소 금액:</strong> ${itemDetail.cancelledAmount ? itemDetail.cancelledAmount.toLocaleString() + '원' : '-'}</div>
          <div><strong>취소 일시:</strong> ${itemDetail.paymentDetailCancelledAt || '-'}</div>
          <div><strong>실패 사유:</strong> ${itemDetail.failReason || '-'}</div>
        </div>

        <div class="info-box">
          <h3>고객 & 배송 정보</h3>
          <div><strong>고객명:</strong> ${itemDetail.customerName || '-'}</div>
          <div><strong>고객 연락처:</strong> ${itemDetail.customerTel || '-'}</div>
          <hr>
          <div><strong>주소:</strong> ${itemDetail.shippingAddress || '-'}</div>
          <div><strong>도시:</strong> ${itemDetail.shippingCity || '-'}</div>
          <div><strong>구/군/리:</strong> ${itemDetail.shippingRegion || '-'}</div>
          <div><strong>우편번호:</strong> ${itemDetail.shippingPostNum || '-'}</div>
          <div><strong>도로명:</strong> ${itemDetail.shippingRoadNum || '-'}</div>
          <div><strong>수령인 이름:</strong> ${itemDetail.receiverName || '-'}</div>
          <div><strong>수령인 연락처:</strong> ${itemDetail.receiverTel || '-'}</div>
          <hr>
          <div><strong>배송 ID:</strong> ${itemDetail.shipmentId || '-'}</div>
          <div><strong>운송장 번호:</strong> ${itemDetail.trackingNumber || '-'}</div>
          <div><strong>택배사:</strong> ${itemDetail.courierCompany || '-'}</div>
          <div><strong>배송 상태:</strong> ${statusMapping[itemDetail.shipmentStatus] || itemDetail.shipmentStatus || '-'}</div>
          <div><strong>출고일:</strong> ${itemDetail.dispatchedAt || '-'}</div>
          <div><strong>배송완료일:</strong> ${itemDetail.deliveredAt || '-'}</div>
          <div><strong>예상도착일:</strong> ${itemDetail.estimatedDeliveryDate || '-'}</div>
        </div>

      </div>
    </div>
  `;
}


function renderItemDetailHistory(historyList) {
    const tbody = document.getElementById('item-history-body');
    if (!tbody) {
        console.error("item-history-body not found");
        return;
    }
    tbody.innerHTML = '';

    if (!historyList || historyList.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5">이력 정보가 없습니다.</td></tr>';
        return;
    }
    historyList.forEach(history => {
        const row = document.createElement('tr');
        row.innerHTML = `
      <td>${history.orderItemHistoryId || '-'}</td>
      <td>${history.orderItemDetailId || '-'}</td>
      <td>${history.oldQuantity ?? '-'}</td>
      <td>${history.newQuantity ?? '-'}</td>
      <td>${history.oldPrice ? history.oldPrice.toLocaleString() : '-'}</td>
      <td>${history.newPrice ? history.newPrice.toLocaleString() : '-'}</td>
      <td>${history.changeReason || '-'}</td>
      <td>${history.changedAt || '-'}</td>
      <td>${statusMapping[history.changeBy] || history.changeBy || '-'}</td>
      <td>${statusMapping[history.itemHistoryStatus] || history.itemHistoryStatus || '-'}</td>
    `;
        tbody.appendChild(row);
    });
}

let currentPage = 1;
let totalPages = 9;
let groupStart = 1;
const groupSize = 10;

function renderPageNumbers() {
    const container = document.getElementById('pageNumbers');
    if (!container) return;
    container.innerHTML = '';

    const end = Math.min(groupStart + groupSize - 1, totalPages);
    for (let page = groupStart; page <= end; page++) {
        const btn = document.createElement('button');
        btn.textContent = page;
        btn.classList.add('page-number');
        if (page === currentPage) {
            btn.classList.add('active');
        }
        btn.addEventListener('click', () => {
            currentPage = page;
            const itemDetailId = sessionStorage.getItem('selectedOrderItemDetailId');
            fetchOrderItemDetailPage(itemDetailId, currentPage, 10);
        });
        container.appendChild(btn);
    }
    togglePaginationButtons();
}

function togglePaginationButtons() {
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');

    if (prevBtn) {
        prevBtn.style.display = groupStart > 1 ? 'inline-block' : 'none';
    }
    if (nextBtn) {
        nextBtn.style.display = (groupStart + groupSize <= totalPages) ? 'inline-block' : 'none';
    }
}

function bindPaginationEvents() {
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');

    if (prevBtn) {
        prevBtn.onclick = () => {
            if (groupStart > 1) {
                groupStart = Math.max(1, groupStart - groupSize);
                currentPage = groupStart;
                const itemDetailId = sessionStorage.getItem('selectedOrderItemDetailId');
                fetchOrderItemDetailPage(itemDetailId, currentPage, 10);
                renderPageNumbers();
            }
        };
    }
    if (nextBtn) {
        nextBtn.onclick = () => {
            if (groupStart + groupSize <= totalPages) {
                groupStart += groupSize;
                currentPage = groupStart;
                const itemDetailId = sessionStorage.getItem('selectedOrderItemDetailId');
                fetchOrderItemDetailPage(itemDetailId, currentPage, 10);
                renderPageNumbers();
            }
        };
    }
    togglePaginationButtons();
}
