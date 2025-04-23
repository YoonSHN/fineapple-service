import { statusMapping, statusClass } from './utils.js';

export let currentPage = 1;
export let totalPages = 1;
export let groupStart = 1;
const groupSize = 10;

let lastSearchParams = {};

export function initUserProfileHistory() {
    window.addEventListener('popstate', () => {
        const params = new URLSearchParams(window.location.search);
        const page = parseInt(params.get('pageNum') || '1', 10);
        const size = parseInt(params.get('pageSize') || '10', 10);
        const searchParams = Object.fromEntries(params.entries());
        lastSearchParams = searchParams;
        fetchProfileHistory(page, size, searchParams);
    });

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('pageNum') || '1', 10);
    const size = parseInt(params.get('pageSize') || '10', 10);
    const searchParams = Object.fromEntries(params.entries());
    lastSearchParams = searchParams;

    fetchProfileHistory(page, size, searchParams);
    bindPaginationEvents();

    const searchBtn = document.getElementById('profile-history-search-btn');
    if (searchBtn) {
        searchBtn.addEventListener('click', () => {
            currentPage = 1;
            groupStart = 1;

            const key = document.getElementById('search-key')?.value || '';
            const value = document.getElementById('search-value')?.value.trim() || '';

            if (key && value) {
                if (value.length > 100) {
                    alert('검색어는 100자 이내로 입력해주세요.');
                    return;
                }

                if (key === 'userId' && isNaN(value)) {
                    alert('로그인아이디는 숫자만 입력해주세요.');
                    return;
                }

                if (/['"%;]/.test(value)) {
                    alert('검색어에 특수문자는 사용할 수 없습니다.');
                    return;
                }
            }

            const searchParams = {
                pageNum: currentPage,
                pageSize: 10,
            };
            if (key && value) {
                searchParams[key] = value;
            }

            const url = new URL(window.location.href);
            url.search = new URLSearchParams(searchParams).toString();
            history.pushState({}, '', url);
            lastSearchParams = searchParams;

            fetchProfileHistory(currentPage, 10, searchParams);
        });
    }
}

export async function fetchProfileHistory(pageNum = 1, pageSize = 10, searchParams = null) {
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
        const response = await fetch(`/api/v1/admin/users/history/profile?${params.toString()}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();

        totalPages = result.pages >= 1 ? result.pages : 1;
        currentPage = result.pageNum ?? 1;
        renderProfileHistoryTable(result.list);
        renderPageNumbers();
    } catch (error) {
        console.error('Error fetching user profile history:', error);
    }
}

function renderProfileHistoryTable(histories) {
    const tableBody = document.querySelector('.profile-history-table tbody');
    if (!tableBody) return;
    tableBody.innerHTML = '';

    histories.forEach(history => {
        const row = `
      <tr>
        <td>${history.profileHistoryId}</td>
        <td>${history.userId}</td>
        <td>${history.fieldChanged}</td>
        <td>${history.previousValue}</td>
        <td>${history.newValue}</td>
        <td>${history.changedBy}</td>
        <td>${new Date(history.changedAt).toLocaleString()}</td>
      </tr>
    `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });
}

function renderPageNumbers() {
    const container = document.getElementById('profile-history-pageNumbers');
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
            fetchProfileHistory(currentPage, 10);
        });
        container.appendChild(btn);
    }
    togglePaginationButtons();
}

function togglePaginationButtons() {
    const prevBtn = document.getElementById('profile-history-prevPage');
    const nextBtn = document.getElementById('profile-history-nextPage');

    if (prevBtn) prevBtn.style.display = groupStart > 1 ? 'inline-block' : 'none';
    if (nextBtn) nextBtn.style.display = groupStart + groupSize <= totalPages ? 'inline-block' : 'none';
}

function bindPaginationEvents() {
    const prevBtn = document.getElementById('profile-history-prevPage');
    const nextBtn = document.getElementById('profile-history-nextPage');

    if (prevBtn) {
        prevBtn.onclick = () => {
            if (groupStart > 1) {
                groupStart = Math.max(1, groupStart - groupSize);
                currentPage = groupStart;
                fetchProfileHistory(currentPage, 10);
                renderPageNumbers();
            }
        };
    }
    if (nextBtn) {
        nextBtn.onclick = () => {
            if (groupStart + groupSize <= totalPages) {
                groupStart += groupSize;
                currentPage = groupStart;
                fetchProfileHistory(currentPage, 10);
                renderPageNumbers();
            }
        };
    }
    togglePaginationButtons();
}
