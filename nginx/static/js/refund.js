//  수정된 refund.js 전체 코드 변경예정
import { statusMapping, statusClass } from './utils.js';

let currentPage = 1;
let totalPages = 1;
let groupStart = 1;
const groupSize = 10;

let lastSearchParams = {};

export function initRefund() {
    // 페이지 로드시 모달 숨김 (자동 노출 방지)
    const refundModal = document.getElementById('refundDetailModal');
    if (refundModal) refundModal.style.display = 'none';

    window.addEventListener('popstate', () => {
        const params = new URLSearchParams(window.location.search);
        const page = parseInt(params.get('pageNum') || '1', 10);
        const size = parseInt(params.get('pageSize') || '10', 10);
        const searchParams = Object.fromEntries(params.entries());
        lastSearchParams = searchParams;
        fetchRefund(page, size, searchParams);
    });

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('pageNum') || '1', 10);
    const size = parseInt(params.get('pageSize') || '10', 10);
    const searchParams = Object.fromEntries(params.entries());
    lastSearchParams = searchParams;

    fetchRefund(page, size, searchParams);
    bindPaginationEvents();
    bindRefundDetailModalEvents(); // 환불 모달 변경 예정

    const searchBtn = document.getElementById('refund-search-btn');
    if (searchBtn) {
        searchBtn.addEventListener('click', () => {
            const paymentIdInput = document.querySelector('input[name="paymentId"]');
            const paymentId = paymentIdInput?.value.trim();
            const refundStatus = document.querySelector('select[name="refundStatus"]')?.value || '';
            const sortDir = document.querySelector('select[name="sortDir"]')?.value || '';
            const startDate = document.querySelector('input[name="startDate"]')?.value || '';
            const endDate = document.querySelector('input[name="endDate"]')?.value || '';

            if (paymentId && isNaN(paymentId)) {
                alert('결제 ID는 숫자만 입력할 수 있습니다.');
                paymentIdInput.focus();
                return;
            }

            if (startDate && endDate && startDate > endDate) {
                alert('시작일은 종료일보다 이전이어야 합니다.');
                return;
            }

            currentPage = 1;
            groupStart = 1;

            const searchParams = {
                pageNum: currentPage,
                pageSize: 10,
                paymentId,
                refundStatus,
                sortDir,
                startDate,
                endDate
            };

            const url = new URL(window.location.href);
            url.search = new URLSearchParams(searchParams).toString();
            history.pushState({}, '', url);

            fetchRefund(currentPage, 10, searchParams);
        });
    }
}

export async function fetchRefund(pageNum = 1, pageSize = 10, searchParams = null) {
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
        const response = await fetch(`/api/v1/admin/refund?${params.toString()}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();

        totalPages = result.pages && result.pages >= 1 ? result.pages : 1;
        currentPage = result.pageNum ?? 1;

        renderRefundTable(result.list);
        renderPageNumbers();
    } catch (error) {
        console.error('Error fetching refund list:', error);
    }
}

function renderRefundTable(refunds) {
    const tableBody = document.querySelector('.refund-table tbody');
    if (!tableBody) return;
    tableBody.innerHTML = '';

    refunds.forEach(refund => {
        const requestedAt = refund.requestedAt ? new Date(refund.requestedAt).toLocaleString() : '';
        const approvedAt = refund.approvedAt ? new Date(refund.approvedAt).toLocaleString() : '';
        const statusText = statusMapping[refund.refundStatus] ?? refund.refundStatus;
        const refundAmount = refund.refundTotalAmount?.toLocaleString() ?? '0';
        const refundReason = refund.refundReason ?? '-';
        const pgCode = refund.pgResponseCode ?? '-';
        const failReason = refund.refundFailReason ?? '-';

        const row = `
            <tr>
                <td>${refund.refundId}</td>
                <td>${statusText}</td>
                <td>${refund.paymentId}</td>
                <td>${requestedAt}</td>
                <td>${approvedAt}</td>
                <td>${refundAmount}</td>
                <td>${refundReason}</td>
                <td>${pgCode}</td>
                <td>${failReason}</td>
                <td>
                    <button class="view-refund-detail-btn" data-refund-id="${refund.refundId}">상세보기</button>
                </td>
            </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });

    tableBody.querySelectorAll('.view-refund-detail-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const refundId = btn.getAttribute('data-refund-id');
            try {
                const response = await fetch(`/api/v1/admin/refund/${refundId}`);
                if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
                const refundData = await response.json();
                showRefundDetailModal(refundData);
            } catch (error) {
                console.error('Error fetching refund detail:', error);
            }
        });
    });
}

function renderPageNumbers() {
    const container = document.getElementById('refund-pageNumbers');
    if (!container) return;
    container.innerHTML = '';

    const end = Math.min(groupStart + groupSize - 1, totalPages);
    for (let page = groupStart; page <= end; page++) {
        const btn = document.createElement('button');
        btn.textContent = page;
        btn.classList.add('page-number');
        if (page === currentPage) btn.classList.add('active');
        btn.addEventListener('click', () => {
            currentPage = page;
            fetchRefund(currentPage, 10);
        });
        container.appendChild(btn);
    }

    togglePaginationButtons();
}

function togglePaginationButtons() {
    const prevBtn = document.getElementById('refund-prevPage');
    const nextBtn = document.getElementById('refund-nextPage');
    if (prevBtn) prevBtn.style.display = groupStart > 1 ? 'inline-block' : 'none';
    if (nextBtn) nextBtn.style.display = groupStart + groupSize <= totalPages ? 'inline-block' : 'none';
}

function bindPaginationEvents() {
    const prevBtn = document.getElementById('refund-prevPage');
    const nextBtn = document.getElementById('refund-nextPage');

    if (prevBtn) {
        prevBtn.onclick = () => {
            if (groupStart > 1) {
                groupStart = Math.max(1, groupStart - groupSize);
                currentPage = groupStart;
                fetchRefund(currentPage, 10);
                renderPageNumbers();
            }
        };
    }

    if (nextBtn) {
        nextBtn.onclick = () => {
            if (groupStart + groupSize <= totalPages) {
                groupStart += groupSize;
                currentPage = groupStart;
                fetchRefund(currentPage, 10);
                renderPageNumbers();
            }
        };
    }

    togglePaginationButtons();
}

function showRefundDetailModal(refund) {
    const setText = (id, value) => {
        const el = document.getElementById(id);
        if (el) el.textContent = value ?? '-';
    };

    setText('modalRefundId', refund.refundId);
    setText('modalRefundStatus', refund.refundStatus);
    setText('modalPaymentId', refund.paymentId);
    setText('modalRefundTotalAmount', refund.refundTotalAmount?.toLocaleString() + '원');
    setText('modalRequestedAt', refund.requestedAt);
    setText('modalApprovedAt', refund.approvedAt);
    setText('modalRefundReason', refund.refundReason);
    setText('modalPgResponseCode', refund.pgResponseCode);
    setText('modalRefundFailReason', refund.refundFailReason);

    renderRefundDetailsTable(refund.details ?? []);

    // ✅ 모달은 상세보기 버튼 클릭 시에만 열림
    document.getElementById('refundDetailModal').style.display = 'block';
}

function renderRefundDetailsTable(details = []) {
    const tbody = document.getElementById('refundDetailHistoryBody');
    if (!tbody) return;
    tbody.innerHTML = '';

    details.forEach(detail => {
        const row = `
            <tr>
                <td>${detail.refundTransaction}</td>
                <td>${detail.issueStatus}</td>
                <td>${detail.approvedTime ?? '-'}</td>
                <td>${detail.approvedNumber ?? '-'}</td>
                <td>${detail.requestPrice?.toLocaleString() ?? '0'}원</td>
                <td>${detail.remainingPrice?.toLocaleString() ?? '0'}원</td>
                <td>${detail.refundBankName ?? '-'}</td>
                <td>${detail.refundBankCode ?? '-'}</td>
            </tr>
        `;
        tbody.insertAdjacentHTML('beforeend', row);
    });
}

function bindRefundDetailModalEvents() {
    const closeBtn = document.getElementById('closeRefundDetailModalBtn');
    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            const modal = document.getElementById('refundDetailModal');
            if (modal) modal.style.display = 'none';
        });
    }
}