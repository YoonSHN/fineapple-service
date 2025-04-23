export function initStoreList() {
    fetchStores(); // 페이지 로드 시 자동 호출
    bindLogoutEvent();
}

async function fetchStores() {
    try {
        const response = await fetch('/api/v1/admin/inventory/store', {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        renderStoreTable(data || []);
    } catch (error) {
        console.error('스토어 목록 불러오기 실패:', error);
    }
}

function renderStoreTable(stores) {
    const tbody = document.getElementById('stores-body');
    if (!tbody) return;

    tbody.innerHTML = '';

    if (stores.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align: center;">스토어가 없습니다.</td>
            </tr>
        `;
        return;
    }

    stores.forEach(store => {
        const row = `
            <tr>
                <td>${store.storeId}</td>
                <td>${store.name}</td>
                <td>${store.location}</td>
                <td>${store.storeNumber}</td>
                <td>
                    <button class="view-stock-btn" data-store-id="${store.storeId}">재고보기</button>
                </td>
            </tr>
        `;
        tbody.insertAdjacentHTML('beforeend', row);
    });
}

function bindLogoutEvent() {
    const logoutLink = document.getElementById('logout-link');
    const logoutForm = document.getElementById('logout-form');

    if (logoutLink && logoutForm) {
        logoutLink.addEventListener('click', function (e) {
            e.preventDefault();
            logoutForm.submit();
        });
    }
}
