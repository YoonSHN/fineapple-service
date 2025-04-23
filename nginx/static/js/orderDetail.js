import {statusMapping, statusClass} from "./utils.js";

let currentPage = 1;
let totalPages = 9;
let groupStart = 1;
const groupSize = 10;

export function initOrderDetail() {
    const orderId = sessionStorage.getItem('selectedOrderId');
    if (orderId) {
        fetchOrderDetails(orderId, 1, 10);
    }
    bindPaginationEvents();
}

export async function fetchOrderDetails(orderId, pageNum = 1, pageSize = 10) {
    try {
        const response = await fetch(`/api/v1/admin/${orderId}/orderItemDetails?pageNum=${pageNum}&pageSize=${pageSize}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();

        totalPages = (result.pages && result.pages >= 1) ? result.pages : 1;
        currentPage = result.pageNum ?? 1;

        renderOrderDetails(result.list);
        renderPageNumbers();
    } catch (error) {
        console.error('Error fetching order details:', error);
    }
}

function renderOrderDetails(details) {
    const tableBody = document.getElementById('orders-body');
    if (!tableBody) {
        console.error('주문 상세 테이블 바디(orders-body)를 찾을 수 없습니다.');
        return;
    }
    tableBody.innerHTML = '';
    if (!details || details.length === 0) {
        tableBody.innerHTML = '<tr><td colspan="12">주문 상세 정보가 없습니다.</td></tr>';
        return;
    }
    details.forEach(detail => {
        const row = `
      <tr>
          <td>${detail.orderCode || '-'}</td>
          <td>${detail.orderItemDetailId || '-'}</td>
          <td>${detail.imageUrl ? `<img src="${detail.imageUrl}" alt="상품 이미지" style="width: 70px; height: 50px; object-fit: contain; display: block; margin: 0 auto; border-radius: 6px;">` : '-'}</td>
          <td>${detail.tel || '-'}</td>
          <td>${detail.name || '-'}</td>
          <td>${detail.quantity || 0}</td>
          <td>${detail.price ? detail.price.toLocaleString() : '-'}</td>
          <td>${detail.discountPrice ? detail.discountPrice.toLocaleString() : '-'}</td>
          <td>${detail.couponApplied ? '예' : '아니오'}</td>
          <td>${detail.additionalPrice ? detail.additionalPrice.toLocaleString() : '-'}</td>
          <td>${detail.additional || '-'}</td>
          <td>${statusMapping[detail.itemStatus] || detail.itemStatus || '-'}</td>
          <td>
              <button class="view-item-detail-btn" data-detail-id="${detail.orderItemDetailId}">상세정보</button>
          </td>
      </tr>
    `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });
}

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
            const orderId = sessionStorage.getItem('selectedOrderId');
            fetchOrderDetails(orderId, currentPage, 10);
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
                const orderId = sessionStorage.getItem('selectedOrderId');
                fetchOrderDetails(orderId, currentPage, 10);
                renderPageNumbers();
            }
        };
    }
    if (nextBtn) {
        nextBtn.onclick = () => {
            if (groupStart + groupSize <= totalPages) {
                groupStart += groupSize;
                currentPage = groupStart;
                const orderId = sessionStorage.getItem('selectedOrderId');
                fetchOrderDetails(orderId, currentPage, 10);
                renderPageNumbers();
            }
        };
    }
    togglePaginationButtons();
}

export function fetchOrderStatusHistory(orderId, pageNum = 1, pageSize = 10) {
    fetch(`/api/v1/admin/${orderId}?pageNum=${pageNum}&pageSize=${pageSize}`)
        .then(response => {
            if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
            return response.json();
        })
        .then(result => {
            renderOrderStatusHistoryModal(result.list);
        })
        .catch(error => {
            console.error('Error fetching order status history:', error);
        });
}

function renderOrderStatusHistoryModal(statusList) {
    const modalEl = document.getElementById('orderStatusModal');
    if (!modalEl) {
        console.error('모달 요소가 없습니다. (#orderStatusModal)');
        return;
    }
    const tbody = modalEl.querySelector('tbody');
    if (!tbody) {
        console.error('모달 내부에 <tbody>가 없습니다.');
        return;
    }
    tbody.innerHTML = '';

    statusList.forEach(status => {
        const row = document.createElement('tr');
        row.innerHTML = `
      <td>${status.orderstatusId || '-'}</td>
      <td>${status.orderCode || ''}</td>
      <td>${statusMapping[status.orderStatusStatus] || status.orderStatusStatus || '-'}</td>
      <td>${statusMapping[status.paymentStatus] || status.paymentStatus || '-'}</td>
      <td>${status.updatedAt || '-'}</td>
    `;
        tbody.appendChild(row);
    });
    modalEl.style.display = 'block';
}
