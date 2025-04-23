// HTML 페이지 일부만 프래그먼트로 불러와서 spa로 처리
// 일부 클릭 이벤트에서 ID를 임시로 저장해 모듈에 전달
let defaultRoute = '/dashboard';

document.addEventListener('DOMContentLoaded', initPage);

// 브라우저 실행시 초기화
window.addEventListener('popstate', (event) => {
    const route = event.state?.route || defaultRoute;
    loadContent(route);
});
// 브라우저 뒤로  앞으로가기 시 실행될 이벤트 핸들러
document.addEventListener('DOMContentLoaded', () => {
    if (window.location.pathname !== '/admin') {
        window.history.replaceState({ route: defaultRoute }, '', '/admin');
    }
    const initialRoute = history.state?.route || defaultRoute;
    loadContent(initialRoute);
});

// 초기화 함수 이벤트 핸들 바인딩
function initPage() {
    rebindGlobalEvents();
}


// 페이지 안에 주요 요소 바인딩 리펙토링 필요
function rebindGlobalEvents() {
    const sidebar = document.querySelector('.sidebar');
    if (sidebar) {
        sidebar.removeEventListener('click', sidebarClickHandler);
        sidebar.addEventListener('click', sidebarClickHandler);
    }

    const filterBtn = document.getElementById('filterBtn');
    if (filterBtn) {
        filterBtn.removeEventListener('click', filterHandler);
        filterBtn.addEventListener('click', filterHandler);
    }

    const ordersBody = document.getElementById('orders-body');
    if (ordersBody) {
        ordersBody.removeEventListener('click', handleOrderTableClick);
        ordersBody.addEventListener('click', handleOrderTableClick);
    }

    const stockTable = document.getElementById('stores-body');
    if (stockTable) {
        stockTable.removeEventListener('click', handleStockTableClick);
        stockTable.addEventListener('click', handleStockTableClick);
    }

    const closeStatusModalBtn = document.getElementById('closeStatusModalBtn');
    if (closeStatusModalBtn) {
        closeStatusModalBtn.removeEventListener('click', closeOrderStatusModal);
        closeStatusModalBtn.addEventListener('click', closeOrderStatusModal);
    }

    const closeRefundModalBtn = document.getElementById('closeRefundDetailModalBtn');
    if (closeRefundModalBtn) {
        closeRefundModalBtn.removeEventListener('click', closeRefundModal);
        closeRefundModalBtn.addEventListener('click', closeRefundModal);
    }

    const userSearchBtn = document.getElementById('user-search-btn');
    if (userSearchBtn) {
        userSearchBtn.removeEventListener('click', handleUserSearch);
        userSearchBtn.addEventListener('click', handleUserSearch);
    }
}

function sidebarClickHandler(e) {
    const link = e.target.closest('a.nav-link, a.submenu-link');
    if (!link) return;

    const route = link.getAttribute('data-route') || link.getAttribute('href');
    if (!route || route === '#') return;

    e.preventDefault();
    loadContent(route);
    window.history.pushState({ route: route }, '', '/admin');
}

function filterHandler() {
    import('./orderList.js').then(module => module.fetchOrders(1, 10));
}

function handleStockTableClick(e) {
    const stockBtn = e.target.closest('.view-stock-btn');
    if (!stockBtn) return;

    const storeId = stockBtn.getAttribute('data-store-id');
    if (!storeId) return;

    sessionStorage.setItem('selectedStoreId', storeId);

    loadContent('/inventory/stockList')
        .then(() => {
            import('./stockList.js')
                .then(module => module.initStockList(storeId));
        })
        .catch(err => console.error('재고 페이지 로딩 실패:', err));
}

// 사이드바 클릭 시 경로 추출 후 컨텐츠 로딩
function handleOrderTableClick(e) {
    const detailBtn = e.target.closest('.view-details-btn');
    if (detailBtn) {
        const orderId = detailBtn.getAttribute('data-order-id');
        sessionStorage.setItem('selectedOrderId', orderId);
        loadContent('/orders/detail-list').then(() => {
            import('./orderDetail.js').then(module => module.initOrderDetail());
        });
        return;
    }

    const statusBtn = e.target.closest('.view-status-btn');
    if (statusBtn) {
        const orderId = statusBtn.getAttribute('data-order-id');
        import('./orderDetail.js').then(module => module.fetchOrderStatusHistory(orderId, 1, 10));
        return;
    }

    const itemDetailBtn = e.target.closest('.view-item-detail-btn');
    if (itemDetailBtn) {
        const itemDetailId = itemDetailBtn.getAttribute('data-detail-id');
        sessionStorage.setItem('selectedOrderItemDetailId', itemDetailId);
        loadContent('/orders/item-detail');
        return;
    }
}

function handleUserSearch() {
    import('./userList.js').then(module => module.fetchUsers(1, 10));
}

function closeOrderStatusModal() {
    const modalEl = document.getElementById('orderStatusModal');
    if (modalEl) modalEl.style.display = 'none';
}

function closeRefundModal() {
    const modalEl = document.getElementById('refundDetailModal');
    if (modalEl) modalEl.style.display = 'none';
}


// 페이지 콘텐츠 로드 및 모듈 로딩
export async function loadContent(route) {
    try {
        const response = await fetch(route);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const html = await response.text();

        const tempContainer = document.createElement('div');
        tempContainer.innerHTML = html;

        const newContent = tempContainer.querySelector('#main-content');
        const mainContent = document.getElementById('main-content');

        if (!mainContent) {
            console.error('Main content container not found');
            return;
        }

        mainContent.innerHTML = newContent ? newContent.innerHTML : html;

        let pageType = mainContent.querySelector('[data-page]')?.dataset.page;
        if (!pageType) {
            const pageMapping = {
                '/orders/detail-list': 'orderdetaillist',
                '/orders/list': 'orderlist',
                '/orders/item-detail-list': 'orderitemdetail',
                '/users/list': 'userlist',
                '/users/profile-history': 'userprofilehistory',
                '/users/login-history': 'loginhistory',
                '/analytics/report': 'analyticsReport',
                '/refund': 'refund',
                '/products/list': 'product',
                '/inventory/store': 'storelist',
                '/inventory/stockList': 'stockList',
                '/inventory/history': 'stockhistory'
            };
            const matchedKey = Object.keys(pageMapping).find(key => route.includes(key));
            pageType = matchedKey ? pageMapping[matchedKey] : 'dashboard';
            if (mainContent.firstElementChild) {
                mainContent.firstElementChild.setAttribute('data-page', pageType);
            }
        }

        await loadModuleByPageType(pageType);
        rebindGlobalEvents();
    } catch (error) {
        console.error('Error loading content:', error);
    }
}

// 각 페이지 데이터 타입에 따른 동적 모듈 import 및 초기화 함수 호출
async function loadModuleByPageType(pageType) {
    switch (pageType) {
        case 'dashboard':
            return import('./dashboard.js').then(m => m.initDashboard());
        case 'orderlist':
            return import('./orderList.js').then(m => m.initOrderList());
        case 'orderdetaillist':
            return import('./orderDetail.js').then(m => m.initOrderDetail());
        case 'orderitemdetail':
            return import('./orderItemDetail.js').then(m => m.initOrderItemDetail());
        case 'userlist':
            return import('./userList.js').then(m => {
                m.initUserList();
                if (typeof m.bindUserDetailModalEvents === 'function') {
                    m.bindUserDetailModalEvents();
                }
            });
        case 'userprofilehistory':
            return import('./userProfileHistory.js').then(m => m.initUserProfileHistory());
        case 'product':
            return import('./product.js').then(m => m.initProduct());
        case 'loginhistory':
            return import('./loginHistory.js').then(m => m.initLoginHistory());
        case 'analyticsReport':
            return import('./report.js').then(m => m.initSalesReport());
        case 'storelist':
            return import('./storeList.js').then(m => m.initStoreList());
        case 'refund':
            return import('./refund.js').then(m => m.initRefund());
        case 'stockhistory':
            return import('./stockList.js').then(m => m.initStockHistory());
        default:
            console.warn('알 수 없는 페이지 유형:', pageType);
    }
}