export function initStockList(storeId) {
    fetchInventory(storeId);
    bindLogoutEvent();
    bindRegisterForm(storeId);
    bindStockModalEvents();
    bindStockRegisterModalEvents();
}

async function fetchInventory(storeId) {
    try {
        const response = await fetch(`/api/v1/admin/inventory/${storeId}/stock`, {
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const stockList = await response.json();
        renderInventoryTable(stockList);
    } catch (err) {
        console.error('재고 목록 불러오기 실패:', err);
    }
}

function renderInventoryTable(stockList) {
    const tbody = document.getElementById('inventory-body');
    if (!tbody) return;

    tbody.innerHTML = '';

    if (stockList.length === 0) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">재고가 없습니다.</td></tr>`;
        return;
    }

    stockList.forEach(stock => {
        const row = `
            <tr>
                <td>${stock.productId}</td>
                <td>${stock.quantity}</td>
                <td>${stock.stockStatus}</td>
                <td>${stock.codeName}</td>
                <td>${stock.updatedAt}</td>
                <td>${stock.isRestockRequired ? '예' : '아니오'}</td>
                <td>
                     <button class="view-stock-detail-btn" data-store-id="${stock.storeId}" data-product-id="${stock.productId}">상세 보기 </button>
                </td>
            </tr>
        `;
        tbody.insertAdjacentHTML('beforeend', row);
    });
}

function bindRegisterForm(storeId) {
    const form = document.getElementById('register-stock-form');
    if (!form) return;

    form.storeId.value = storeId;

    form.addEventListener('submit', async e => {
        e.preventDefault();
        const formData = new FormData(form);
        const stock = Object.fromEntries(formData.entries());
        stock.storeId = parseInt(storeId);

        stock.stockStatus = "ST0301";

        const safety = Number(stock.safetyStockLevel);
        const min = Number(stock.minStockLevel);
        if (safety && min && safety >= min) {
            alert('❌ 안전재고는 최소 재고보다 작아야 합니다.');
            return;
        }

        try {
            const response = await fetch(`/api/v1/admin/inventory/${storeId}/stock`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(stock)
            });

            if (!response.ok) {
                const errText = await response.text(); // 📌 서버에서 보낸 메시지 받기
                throw new Error(errText);
            }

            alert('✅ 재고가 등록되었습니다.');
            fetchInventory(storeId);
            form.reset();
            document.getElementById('registerStockModal').style.display = 'none';

        } catch (err) {
            console.error('재고 등록 실패:', err);
            alert(err.message); // 📌 여기서 사용자에게 상세 메시지 출력
        }
    });
}

function bindLogoutEvent() {
    document.getElementById('logout-link')?.addEventListener('click', function (e) {
        e.preventDefault();
        document.getElementById('logout-form')?.submit();
    });
}


document.addEventListener('click', async (e) => {
    const btn = e.target.closest('.view-stock-detail-btn');
    if (!btn) return;

    const storeId = btn.getAttribute('data-store-id');
    const productId = btn.getAttribute('data-product-id');

    try {
        const response = await fetch(`/api/v1/admin/inventory/${storeId}/stock/${productId}`);
        if (!response.ok) throw new Error(`상세 조회 실패: ${response.status}`);

        const detail = await response.json();
        showStockDetailModal(detail);
    } catch (err) {
        console.error('재고 상세 정보 가져오기 실패:', err);
        alert('상세 정보를 불러오는 중 오류가 발생했습니다.');
    }
});

function showStockDetailModal(detail) {
    // 기본 텍스트 영역 초기화
    document.getElementById('detailStoreId').textContent = detail.storeId;
    document.getElementById('detailProductId').textContent = detail.productId;
    document.getElementById('detailQuantity').textContent = detail.quantity;
    document.getElementById('detailStockStatus').textContent = detail.stockStatus;
    document.getElementById('detailMinStockLevel').textContent = detail.minStockLevel;
    document.getElementById('detailMaxStockLevel').textContent = detail.maxStockLevel;
    document.getElementById('detailSafetyStockLevel').textContent = detail.safetyStockLevel ?? '-';
    document.getElementById('detailStockInQuantity').textContent = detail.stockInQuantity;
    document.getElementById('detailStockOutQuantity').textContent = detail.stockOutQuantity;
    document.getElementById('detailLastRestockDate').textContent = detail.lastRestockDate ?? '-';
    document.getElementById('detailFirstStockInDate').textContent = detail.firstStockInDate ?? '-';
    document.getElementById('detailLastStockOutDate').textContent = detail.lastStockOutDate ?? '-';
    document.getElementById('detailIsRestockRequired').textContent = detail.isRestockRequired ? '예' : '아니오';

    // 수정 input도 같이 초기화
    document.getElementById('inputQuantity').value = detail.quantity ?? '';
    document.getElementById('inputStockStatus').value = detail.stockStatus ?? '';
    document.getElementById('inputMinStockLevel').value = detail.minStockLevel ?? '';
    document.getElementById('inputMaxStockLevel').value = detail.maxStockLevel ?? '';
    document.getElementById('inputSafetyStockLevel').value = detail.safetyStockLevel ?? '';

    // 모달 열기
    document.getElementById('stockDetailModal').style.display = 'block';

    // 항상 보기 모드로 초기화
    toggleEditable(false);
}




function bindStockModalEvents() {
    const closeBtn = document.getElementById('closeStockDetailModalBtn');
    const editBtn = document.getElementById('editStockBtn');
    const saveBtn = document.getElementById('saveStockBtn');

    if (closeBtn) {
        closeBtn.addEventListener('click', () => {
            document.getElementById('stockDetailModal').style.display = 'none';
        });
    }

    // ✅ 수정 버튼 클릭 시 input 표시
    if (editBtn) {
        editBtn.addEventListener('click', () => {
            toggleEditable(true);
        });
    }

    // ✅ 저장 버튼 클릭 시 PATCH 요청
    if (saveBtn) {
        saveBtn.addEventListener('click', async () => {
            await saveStockDetail();
        });
    }
}

function toggleEditable(editMode) {
    const toggleFields = [
        ['detailQuantity', 'inputQuantity'],
        ['detailStockStatus', 'inputStockStatus'],
        ['detailMinStockLevel', 'inputMinStockLevel'],
        ['detailMaxStockLevel', 'inputMaxStockLevel'],
        ['detailSafetyStockLevel', 'inputSafetyStockLevel']
    ];

    toggleFields.forEach(([spanId, inputId]) => {
        document.getElementById(spanId).style.display = editMode ? 'none' : 'inline';
        document.getElementById(inputId).style.display = editMode ? 'inline' : 'none';
        if (editMode) {
            document.getElementById(inputId).value = document.getElementById(spanId).textContent;
        }
    });

    document.getElementById('editStockBtn').style.display = editMode ? 'none' : 'inline-block';
    document.getElementById('saveStockBtn').style.display = editMode ? 'inline-block' : 'none';
}


function bindStockRegisterModalEvents() { //재고등록
    const openBtn = document.getElementById('openRegisterModalBtn');
    const closeBtn = document.getElementById('closeRegisterModalBtn');
    const modal = document.getElementById('registerStockModal');

    if (!openBtn || !closeBtn || !modal) return;

    openBtn.addEventListener('click', () => {
        modal.style.display = 'block';
        loadProductOptions();
    });

    closeBtn.addEventListener('click', () => {
        modal.style.display = 'none';
    });

    // 모달 바깥 클릭 시 닫기
    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });
}
export async function loadProductOptions() {
    try {
        const response = await fetch('/api/v1/admin/products?pageNum=1&pageSize=100');
        if (!response.ok) throw new Error(`상품 목록 불러오기 실패! 상태코드: ${response.status}`);
        const data = await response.json();

        const select = document.getElementById('productSelect');
        if (!select) return;

        select.innerHTML = '<option value="">상품을 선택하세요</option>';

        if (!Array.isArray(data.list)) return;

        data.list.forEach(product => {
            const option = document.createElement('option');
            option.value = product.productId;
            option.textContent = `${product.productId} - ${product.productName}`;
            select.appendChild(option);
        });
    } catch (err) {
        console.error('❌ 상품 옵션 로딩 실패:', err);
        alert('상품 목록을 불러오는 데 실패했습니다.');
    }
}

export function initStockHistory() {
    fetchStockHistory();
    bindLogoutEvent(); // 기존 로그아웃 로직 재사용 가능
}
async function fetchStockHistory(pageNum = 1, pageSize = 10, keyword = '') {
    try {
        const response = await fetch(`/api/v1/admin/stock/history?keyword=${keyword}&pageNum=${pageNum}&pageSize=${pageSize}`);
        if (!response.ok) throw new Error(`HTTP ${response.status}`);
        const result = await response.json();

        renderStockHistoryTable(result.list);
        renderPagination(result.pageNum, result.pages, keyword);
    } catch (err) {
        console.error('📦 재고 입출고 내역 로딩 실패:', err);
    }
}

function renderStockHistoryTable(historyList) {
    const tbody = document.getElementById('stock-history-body');
    if (!tbody) return;

    tbody.innerHTML = '';

    if (!historyList.length) {
        tbody.innerHTML = `<tr><td colspan="7" style="text-align:center;">내역이 없습니다.</td></tr>`;
        return;
    }

    historyList.forEach(history => {
        const row = `
        <tr>
            <td>${history.stockChangeId ?? '-'}</td>
            <td>${history.storeName ?? '-'}</td>
            <td>${history.productName ?? '-'}</td>
            <td>${history.type === 'IN' ? '입고' : history.type === 'OUT' ? '출고' : '기타'}</td>
            <td>${history.previousStock ?? '-'}</td>
            <td>${history.newStock ?? '-'}</td>
            <td>${history.memo ?? '-'}</td>
        </tr>
    `;
        tbody.insertAdjacentHTML('beforeend', row);
    });
}

async function saveStockDetail() {
    const storeId = document.getElementById('detailStoreId').textContent;
    const productId = document.getElementById('detailProductId').textContent;

    const updatedData = {
        quantity: Number(document.getElementById('inputQuantity').value),
        stockStatus: document.getElementById('inputStockStatus').value,
        minStockLevel: Number(document.getElementById('inputMinStockLevel').value),
        maxStockLevel: Number(document.getElementById('inputMaxStockLevel').value),
        safetyStockLevel: Number(document.getElementById('inputSafetyStockLevel').value)
    };

    try {
        const res = await fetch(`/api/v1/admin/inventory/${storeId}/stock/${productId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedData)
        });

        if (!res.ok) throw new Error(`수정 실패: ${res.status}`);
        alert('✅ 재고가 수정되었습니다.');
        document.getElementById('stockDetailModal').style.display = 'none';

        fetchInventory(storeId);
    } catch (err) {
        console.error('❌ 재고 수정 실패:', err);
        alert('재고 수정 중 오류 발생');
    }

}

function renderPagination(currentPage, totalPages, keyword) {
    const container = document.getElementById("pagination-container");
    if (!container) return;

    container.innerHTML = '';

    if (totalPages <= 1) return;

    if (currentPage > 1) {
        const prev = document.createElement("button");
        prev.textContent = "이전";
        prev.addEventListener("click", () => fetchStockHistory(currentPage - 1, 10, keyword));
        container.appendChild(prev);
    }

    for (let i = 1; i <= totalPages; i++) {
        const btn = document.createElement("button");
        btn.textContent = i;
        if (i === currentPage) {
            btn.disabled = true;
        }
        btn.addEventListener("click", () => fetchStockHistory(i, 10, keyword));
        container.appendChild(btn);
    }

    if (currentPage < totalPages) {
        const next = document.createElement("button");
        next.textContent = "다음";
        next.addEventListener("click", () => fetchStockHistory(currentPage + 1, 10, keyword));
        container.appendChild(next);
    }
}