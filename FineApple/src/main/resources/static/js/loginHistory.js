export let currentPage = 1;
export let totalPages = 1;
export let groupStart = 1;
const groupSize = 10;

let lastSearchParams = {};

export function initLoginHistory() {
    window.addEventListener('popstate', () => {
        const params = new URLSearchParams(window.location.search);
        const page = parseInt(params.get('pageNum') || '1', 10);
        const size = parseInt(params.get('pageSize') || '10', 10);
        const searchParams = Object.fromEntries(params.entries());
        lastSearchParams = searchParams;
        fetchLoginHistory(page, size, searchParams);
    });

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('pageNum') || '1', 10);
    const size = parseInt(params.get('pageSize') || '10', 10);
    const searchParams = Object.fromEntries(params.entries());
    lastSearchParams = searchParams;

    fetchLoginHistory(page, size, searchParams);
    bindPaginationEvents();

    const searchBtn = document.getElementById('login-history-search-btn');
    if (searchBtn) {
        searchBtn.addEventListener('click', () => {
            currentPage = 1;
            groupStart = 1;

            const userId = document.querySelector('.login-history-search-box input[name="userId"]')?.value.trim() || '';
            const ipAddress = document.querySelector('.login-history-search-box input[name="ipAddress"]')?.value.trim() || '';
            const loginStatus = document.querySelector('.login-history-search-box select[name="loginStatus"]')?.value || '';
            const startDate = document.querySelector('.login-history-search-box input[name="startDate"]')?.value || '';
            const endDate = document.querySelector('.login-history-search-box input[name="endDate"]')?.value || '';

            if (startDate && endDate && startDate > endDate) {
                alert('시작일은 종료일보다 이전이어야 합니다.');
                return;
            }

            if (userId && isNaN(userId)) {
                alert('사용자 ID는 숫자만 입력할 수 있습니다.');
                return;
            }

            if (ipAddress && /[^0-9.]/.test(ipAddress)) {
                alert('IP 주소는 숫자와 점(.)만 입력할 수 있습니다.');
                return;
            }

            const searchParams = {
                userId,
                ipAddress,
                loginStatus,
                startDate,
                endDate,
                pageNum: currentPage,
                pageSize: 10
            };

            const url = new URL(window.location.href);
            url.search = new URLSearchParams(searchParams).toString();
            history.pushState({}, '', url);

            fetchLoginHistory(currentPage, 10, searchParams);
        });
    }
}

export async function fetchLoginHistory(pageNum = 1, pageSize = 10, searchParams = null) {
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
        const response = await fetch(`/api/v1/admin/users/history/login?${params.toString()}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();

        totalPages = result.pages && result.pages >= 1 ? result.pages : 1;
        currentPage = result.pageNum ?? 1;
        renderLoginHistoryTable(result.list);
        renderPageNumbers();
    } catch (error) {
        console.error('Error fetching login history:', error);
    }
}

function renderLoginHistoryTable(histories) {
    const tableBody = document.querySelector('.login-history-table tbody');
    if (!tableBody) {
        console.error('로그인 히스토리 테이블을 찾을 수 없습니다.');
        return;
    }
    tableBody.innerHTML = '';

    histories.forEach(history => {
        const row = `
      <tr>
        <td>${history.loginHistoryId}</td>
        <td>${history.userId}</td>
        <td>${new Date(history.loginTime).toLocaleString()}</td>
        <td>${history.ipAddress}</td>
        <td>${history.deviceInfo}</td>
        <td>${history.loginStatus}</td>
      </tr>
    `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });
}

function renderPageNumbers() {
    const container = document.getElementById('login-history-pageNumbers');
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
            fetchLoginHistory(currentPage, 10);
        });
        container.appendChild(btn);
    }
    togglePaginationButtons();
}

function togglePaginationButtons() {
    const prevBtn = document.getElementById('login-history-prevPage');
    const nextBtn = document.getElementById('login-history-nextPage');
    if (prevBtn) prevBtn.style.display = groupStart > 1 ? 'inline-block' : 'none';
    if (nextBtn) nextBtn.style.display = groupStart + groupSize <= totalPages ? 'inline-block' : 'none';
}

function bindPaginationEvents() {
    const prevBtn = document.getElementById('login-history-prevPage');
    const nextBtn = document.getElementById('login-history-nextPage');

    if (prevBtn) {
        prevBtn.onclick = () => {
            if (groupStart > 1) {
                groupStart = Math.max(1, groupStart - groupSize);
                currentPage = groupStart;
                fetchLoginHistory(currentPage, 10);
                renderPageNumbers();
            }
        };
    }

    if (nextBtn) {
        nextBtn.onclick = () => {
            if (groupStart + groupSize <= totalPages) {
                groupStart += groupSize;
                currentPage = groupStart;
                fetchLoginHistory(currentPage, 10);
                renderPageNumbers();
            }
        };
    }

    togglePaginationButtons();
}
