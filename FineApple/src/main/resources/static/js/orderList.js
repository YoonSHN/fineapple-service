import { statusMapping, statusClass } from './utils.js';

let currentPage = 1;
let totalPages = 1;
let groupStart = 1;
const groupSize = 10;

let lastSearchParams = {};

export function initOrderList() {
    window.addEventListener('popstate', () => {
        const params = new URLSearchParams(window.location.search);
        const page = parseInt(params.get('pageNum') || '1', 10);
        const size = parseInt(params.get('pageSize') || '10', 10);
        const searchParams = Object.fromEntries(params.entries());
        lastSearchParams = searchParams;
        fetchOrders(page, size, searchParams);
    });

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('pageNum') || '1', 10);
    const size = parseInt(params.get('pageSize') || '10', 10);
    const searchParams = Object.fromEntries(params.entries());
    lastSearchParams = searchParams;

    fetchOrders(page, size, searchParams);
    bindPaginationEvents();

    const searchBtn = document.getElementById('filterBtn');
    if (searchBtn) {
        searchBtn.addEventListener('click', () => {
            currentPage = 1;
            groupStart = 1;

            const searchKey = document.getElementById('search-key')?.value || '';
            const searchValue = document.getElementById('search-value')?.value.trim() || '';
            const orderStatusFilter = document.getElementById('orderStatusFilter')?.value || '';
            const startDate = document.getElementById('startDate')?.value || '';
            const endDate = document.getElementById('endDate')?.value || '';
            const sort = document.getElementById('sortDirectionFilter')?.value || '';

            if ((startDate && !endDate) || (!startDate && endDate)) {
                alert('날짜 검색 시 시작일과 종료일을 모두 입력해주세요.');
                return;
            }
            if (startDate && endDate && startDate > endDate) {
                alert('시작일은 종료일보다 이전이어야 합니다.');
                return;
            }
            if (searchValue.length > 100) {
                alert('검색어는 100자 이내로 입력해주세요.');
                return;
            }
            if ((searchKey === 'orderId' || searchKey === 'orderCode') && isNaN(searchValue)) {
                alert('숫자만 입력해주세요.');
                return;
            }

            const searchParams = {
                pageNum: currentPage,
                pageSize: 10,
                orderStatus: orderStatusFilter,
                sortDir: sort,
                startDate,
                endDate
            };

            if (searchKey && searchValue) {
                searchParams[searchKey] = searchValue;
            }

            const url = new URL(window.location.href);
            url.search = new URLSearchParams(searchParams).toString();
            history.pushState({}, '', url);

            fetchOrders(currentPage, 10, searchParams);
        });
    }
}

export async function fetchOrders(pageNum = 1, pageSize = 10, searchParams = null) {
    let params;
    if (searchParams) {
        params = new URLSearchParams(searchParams);
        lastSearchParams = { ...searchParams };
    } else {
        params = new URLSearchParams(lastSearchParams);
    }

    params.set('pageNum', pageNum);
    params.set('pageSize', pageSize);

    try {
        const response = await fetch(`/api/v1/admin/orders?${params.toString()}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();

        totalPages = result.pages && result.pages >= 1 ? result.pages : 1;
        currentPage = result.pageNum ?? 1;

        renderOrdersTable(result.list);
        renderPageNumbers();
    } catch (error) {
        console.error('Error fetching orders:', error);
    }
}

function renderOrdersTable(orders) {
    const tableBody = document.querySelector('.orders-table tbody');
    if (!tableBody) return;

    tableBody.innerHTML = '';
    orders.forEach(order => {
        const createdAt = order.createdAt ? new Date(order.createdAt).toLocaleString() : '';
        const updatedAt = order.updatedAt ? new Date(order.updatedAt).toLocaleString() : '';

        const row = `
            <tr>
                <td>${order.orderId}</td>
                <td class="order-code" data-order-id="${order.orderId}">${order.orderCode}</td>
                <td>${order.tel}</td>
                <td>${order.totalPrice?.toLocaleString() ?? '-'}</td>
                <td>
                    <span class="status-badge ${statusClass(order.orderStatus)}">
                        ${statusMapping[order.orderStatus] || order.orderStatus}
                    </span>
                </td>
                <td>${order.isCancelled ? 'YES' : 'NO'}</td>
                <td>${createdAt}</td>
                <td>${updatedAt}</td>
                <td>
                    <button class="view-details-btn" data-order-id="${order.orderId}">상세보기</button>
                    <button class="view-status-btn" data-order-id="${order.orderId}">상태보기</button>
                </td>
            </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });
}

function renderPageNumbers() {
    const container = document.getElementById('pageNumbers');
    if (!container) return;


    groupStart = Math.floor((currentPage - 1) / groupSize) * groupSize + 1;

    container.innerHTML = '';
    const end = Math.min(groupStart + groupSize - 1, totalPages);
    for (let page = groupStart; page <= end; page++) {
        const btn = document.createElement('button');
        btn.textContent = page;
        btn.classList.add('page-number');
        if (page === currentPage) btn.classList.add('active');
        btn.addEventListener('click', () => {
            currentPage = page;
            fetchOrders(currentPage, 10, lastSearchParams);
        });
        container.appendChild(btn);
    }

    togglePaginationButtons();
}

function togglePaginationButtons() {
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');
    if (prevBtn) prevBtn.style.display = groupStart > 1 ? 'inline-block' : 'none';
    if (nextBtn) nextBtn.style.display = groupStart + groupSize <= totalPages ? 'inline-block' : 'none';
}

function bindPaginationEvents() {
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');

    if (prevBtn) {
        prevBtn.onclick = () => {
            if (groupStart > 1) {
                groupStart = Math.max(1, groupStart - groupSize);
                currentPage = groupStart;
                fetchOrders(currentPage, 10, lastSearchParams);
                renderPageNumbers();
            }
        };
    }

    if (nextBtn) {
        nextBtn.onclick = () => {
            if (groupStart + groupSize <= totalPages) {
                groupStart += groupSize;
                currentPage = groupStart;
                fetchOrders(currentPage, 10, lastSearchParams);
                renderPageNumbers();
            }
        };
    }

    togglePaginationButtons();
}