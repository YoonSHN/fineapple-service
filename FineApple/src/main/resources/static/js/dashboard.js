import {statusMapping, statusClass, progressColor} from './utils.js';
import {loadContent} from './main.js';

export async function initDashboard() {

    fetchOrdersDash();
    fetchSalesData();
    fetchInventoryData();
    const salesData = await fetchSalesDataChart();
    console.log("Fetched salesData for chart:", salesData);
    renderSalesChart(salesData);
}

async function fetchOrdersDash(pageNum = 1, pageSize = 4) {
    try {
        const response = await fetch(`/api/v1/admin/orders?pageNum=${pageNum}&pageSize=${pageSize}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();
        renderOrdersDashTable(result.list);
    } catch (error) {
        console.error('Error fetching orders (dashboard):', error);
    }
}

function renderOrdersDashTable(orders) {
    const tableBody = document.querySelector('.orders-table tbody');
    if (!tableBody) {
        console.error('Orders table body not found');
        return;
    }
    tableBody.innerHTML = '';
    orders.forEach(order => {
        const row = `
      <tr>
          <td>${order.orderCode}</td>
          <td>${order.tel}</td>
          <td>
            <span class="status-badge ${statusClass(order.orderStatus)}">
              ${statusMapping[order.orderStatus] || order.orderStatus}
            </span>
          </td>
          <td>${order.totalPrice}</td>
      </tr>
    `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });
}

async function fetchSalesData() {
    try {
        const response = await fetch('/api/v1/admin/sales');
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const data = await response.json();
        renderSalesData(data);
    } catch (error) {
        console.error('Error fetching sales data:', error);
    }
}

function renderSalesData(data) {
    const totalSalesEl = document.getElementById('total-sales');
    const orderCountEl = document.getElementById('order-count');
    const visitorsEl = document.getElementById('visitors');
    const lowStockEl = document.getElementById('low-stock');
    if (totalSalesEl) totalSalesEl.textContent = `$${data.totalSales}`;
    if (orderCountEl) orderCountEl.textContent = data.orderCount;
    if (visitorsEl) visitorsEl.textContent = data.visitors;
    if (lowStockEl) lowStockEl.textContent = data.lowStock;

    document.getElementById('sales-trend').innerHTML = trendText(data.salesGrowthRate);
    document.getElementById('order-trend').innerHTML = trendText(data.orderGrowthRate);
    document.getElementById('visitor-trend').innerHTML = trendText(data.visitorGrowthRate);

    function trendText(rate) {
        if (rate > 0) {
            return `<span class="material-icons trend-icon">trending_up</span> 이번 주 +${rate}%`;
        } else if (rate < 0) {
            return `<span class="material-icons trend-icon">trending_down</span> 이번 주 ${rate}%`;
        } else {
            return `<span class="material-icons trend-icon">trending_flat</span> 이번 주 0%`;
        }
    }

    updateTrend('sales-trend', data.salesGrowthRate);
    updateTrend('order-trend', data.orderGrowthRate);
    updateTrend('visitor-trend', data.visitorGrowthRate);

    function updateTrend(elId, rate) {
        const el = document.getElementById(elId);
        if (!el) return;

        if (rate > 0) {
            el.className = 'stats-trend positive';
            el.innerHTML = `<span class="material-icons trend-icon">trending_up</span> 이번 주 +${rate}%`;
        } else if (rate < 0) {
            el.className = 'stats-trend negative';
            el.innerHTML = `<span class="material-icons trend-icon">trending_down</span> 이번 주 ${rate}%`;
        } else {
            el.className = 'stats-trend neutral';
            el.innerHTML = `<span class="material-icons trend-icon">trending_flat</span> 이번 주 0%`;
        }
    }

}

async function fetchInventoryData() {
    try {
        const response = await fetch('/api/v1/admin/inventory/store', {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const data = await response.json();
        renderInventoryData(data);
    } catch (error) {
        console.error('Error fetching inventory data:', error);
    }
}

function renderInventoryData(items) {
    const inventoryBody = document.getElementById('inventory-body');
    if (!inventoryBody) {
        console.error('Inventory table body not found');
        return;
    }
    inventoryBody.innerHTML = '';
    items.forEach(item => {
        const row = `
      <tr>
                <td>${item.storeId}</td>
                <td>${item.name}</td>
                <td>${item.location}</td>
                <td>${item.storeNumber}</td>
                <td>
                    <button class="view-stock-btn" data-store-id="${item.storeId}">재고보기</button>
                </td>
            </tr>
    `;
        inventoryBody.insertAdjacentHTML('beforeend', row);
    });
}

document.addEventListener('click', function (e) {
    const panelLink = e.target.closest('a.panel-link');
    if (panelLink) {
        e.preventDefault();
        const url = panelLink.getAttribute('href');
        loadContent(url).then(() => {
            window.history.pushState({url}, '', url);
        });
    }
});

async function fetchSalesDataChart() {
    try {
        const response = await fetch('/api/v1/admin/sales-data');
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();
        console.log('fetchSalesDataChart result:', result);
        if (Array.isArray(result)) {
            return result;
        } else if (result.salesData && Array.isArray(result.salesData)) {
            return result.salesData;
        } else {
            console.error('Unexpected response structure:', result);
            return [];
        }
    } catch (error) {
        console.error('Error fetching sales data for chart:', error);
        return [];
    }
}


function renderSalesChart(salesData) {

    if (!Array.isArray(salesData)) {
        console.error('salesData is not an array:', salesData);
        return;
    }


    const weekdays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
    const chartData = weekdays.map(day => {
        const record = salesData.find(item => item.weekday === day);
        return record ? record.totalAmount : 0;
    });
    const labels = ['월', '화', '수', '목', '금', '토', '일'];

    const canvas = document.getElementById('salesChart');
    if (!canvas) {
        console.error('salesChart canvas not found');
        return;
    }
    const ctx = canvas.getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: '매출',
                data: chartData,
                borderColor: 'rgba(75, 192, 192, 1)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}
