import { statusMapping, statusClass } from './utils.js';

let currentPage = 1;
let totalPages = 1;
let groupStart = 1;
const groupSize = 10;

let lastSearchParams = {};

export function initUserList() {
    // ✅ 페이지 로드시 유저 모달 숨김 (자동 노출 방지)
    const userModal = document.getElementById('userDetailModal');
    if (userModal) userModal.style.display = 'none';

    window.addEventListener('popstate', () => {
        const params = new URLSearchParams(window.location.search);
        const page = parseInt(params.get('pageNum') || '1', 10);
        const size = parseInt(params.get('pageSize') || '10', 10);
        const searchParams = Object.fromEntries(params.entries());
        lastSearchParams = searchParams;
        fetchUsers(page, size, searchParams);
    });

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('pageNum') || '1', 10);
    const size = parseInt(params.get('pageSize') || '10', 10);
    const searchParams = Object.fromEntries(params.entries());
    lastSearchParams = searchParams;

    fetchUsers(page, size, searchParams);
    bindPaginationEvents();

    const searchBtn = document.getElementById('user-search-btn');
    if (searchBtn) {
        searchBtn.addEventListener('click', () => {
            currentPage = 1;
            groupStart = 1;

            const email = document.querySelector('.search-box input[name="email"]')?.value || '';
            const isActive = document.querySelector('.search-box select[name="isActive"]')?.value || '';
            const sortDir = document.querySelector('.search-box select[name="sortDir"]')?.value || '';
            const startDate = document.querySelector('.search-box input[name="startDate"]')?.value || '';
            const endDate = document.querySelector('.search-box input[name="endDate"]')?.value || '';

            if (startDate && endDate && startDate > endDate) {
                alert('시작일은 종료일보다 이전이어야 합니다.');
                return;
            }

            if (email.length > 100) {
                alert('이메일은 100자 이내로 입력해주세요.');
                return;
            }

            const searchParams = {
                pageNum: currentPage,
                pageSize: 10,
                email,
                isActive,
                sortDir,
                startDate,
                endDate
            };

            const url = new URL(window.location.href);
            url.search = new URLSearchParams(searchParams).toString();
            history.pushState({}, '', url);

            fetchUsers(currentPage, 10, searchParams);
        });
    }
}

export async function fetchUsers(pageNum = 1, pageSize = 10, searchParams = null) {
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
        const response = await fetch(`/api/v1/admin/users/search?${params.toString()}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();

        totalPages = result.pages && result.pages >= 1 ? result.pages : 1;
        currentPage = result.pageNum ?? 1;
        renderUserTable(result.list);
        renderPageNumbers();
    } catch (error) {
        console.error('Error fetching users (user list):', error);
    }
}

function renderUserTable(users) {
    const tableBody = document.querySelector('.users-table tbody');
    if (!tableBody) return;

    tableBody.innerHTML = '';
    users.forEach(user => {
        const createDate = user.createDate ? new Date(user.createDate).toLocaleString() : '';
        const updatedAt = user.updatedAt ? new Date(user.updatedAt).toLocaleString() : '';
        const activeText = user.isActive === true ? '활성' : '비활성';
        const buttonHtml = user.userType === 'GUEST'
            ? ''
            : `<button class="view-user-detail-btn" data-user-id="${user.userId}">상세보기</button>`;
        const row = `
      <tr>
        <td>${user.userId}</td>
        <td>${user.email}</td>
        <td>${createDate}</td>
        <td>${updatedAt}</td>
        <td>${activeText}</td>
        <td>${buttonHtml}</td>
      </tr>
    `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });

    tableBody.querySelectorAll('.view-user-detail-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const userId = btn.getAttribute('data-user-id');
            try {
                const response = await fetch(`/api/v1/admin/users/${userId}`);
                if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
                const userData = await response.json();
                showUserDetailModal(userData);
            } catch (error) {
                console.error('Error fetching user detail:', error);
            }
        });
    });
}

function renderPageNumbers() {
    const container = document.getElementById('user-pageNumbers');
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
            fetchUsers(currentPage, 10);
        });
        container.appendChild(btn);
    }
    togglePaginationButtons();
}

function togglePaginationButtons() {
    const prevBtn = document.getElementById('user-prevPage');
    const nextBtn = document.getElementById('user-nextPage');
    if (prevBtn) prevBtn.style.display = groupStart > 1 ? 'inline-block' : 'none';
    if (nextBtn) nextBtn.style.display = groupStart + groupSize <= totalPages ? 'inline-block' : 'none';
}

function bindPaginationEvents() {
    const prevBtn = document.getElementById('user-prevPage');
    const nextBtn = document.getElementById('user-nextPage');

    if (prevBtn) {
        prevBtn.onclick = () => {
            if (groupStart > 1) {
                groupStart = Math.max(1, groupStart - groupSize);
                currentPage = groupStart;
                fetchUsers(currentPage, 10);
                renderPageNumbers();
            }
        };
    }

    if (nextBtn) {
        nextBtn.onclick = () => {
            if (groupStart + groupSize <= totalPages) {
                groupStart += groupSize;
                currentPage = groupStart;
                fetchUsers(currentPage, 10);
                renderPageNumbers();
            }
        };
    }

    togglePaginationButtons();
}

export function showUserDetailModal(user) {
    document.getElementById('modalUserId').textContent = user.userId;
    document.getElementById('modalEmail').textContent = user.email;
    document.getElementById('modalCreateDate').textContent = user.createDate;
    document.getElementById('modalUpdatedAt').textContent = user.updatedAt;
    document.getElementById('modalName').textContent = user.name;
    document.getElementById('modalTel').textContent = user.tel;
    document.getElementById('modalBirth').textContent = user.birth;
    document.getElementById('modalUserRole').textContent = user.userRole;
    document.getElementById('modalUserStatus').textContent = user.userStatus;
    document.getElementById('modalDeliveryId').textContent = user.deliveryId;
    document.getElementById('modalAddress').textContent = user.address;
    document.getElementById('modalCity').textContent = user.city;
    document.getElementById('modalCountry').textContent = user.country;
    document.getElementById('modalRegion').textContent = user.region;
    document.getElementById('modalPostNum').textContent = user.postNum;
    document.getElementById('modalActive').textContent = user.active ? '활성' : '비활성';
    document.getElementById('userDetailModal').style.display = 'block';
    bindUserDetailModalEvents();
}

export function bindUserDetailModalEvents() {
    const closeBtn = document.getElementById('closeUserDetailModalBtn');
    const toggleBtn = document.getElementById('toggleActiveBtn');

    if (closeBtn) {
        closeBtn.onclick = () => {
            document.getElementById('userDetailModal').style.display = 'none';
        };
    }

    if (toggleBtn) {
        toggleBtn.onclick = async () => {
            const activeCell = document.getElementById('modalActive');
            const userId = document.getElementById('modalUserId').textContent;
            let newStatus = activeCell.textContent !== '활성';
            activeCell.textContent = statusMapping[newStatus];
            activeCell.className = statusClass[newStatus];
            await updateUserActiveStatus(userId, newStatus);
            fetchUsers(currentPage, 10);
        };
    }
}

export async function updateUserActiveStatus(userId, isActive) {
    const csrfToken = document.getElementById('csrfToken')?.value;
    try {
        const response = await fetch(`/api/v1/admin/users/${userId}?isActive=${isActive}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'X-CSRF-TOKEN': csrfToken
            },
            body: JSON.stringify({})
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
    } catch (error) {
        console.error('Error updating user active status:', error);
    }
}

document.getElementById('logout-link')?.addEventListener('click', function (e) {
    e.preventDefault();
    document.getElementById('logout-form').submit();
});