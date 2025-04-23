export function initSalesReport() {
    loadSalesKpi();
    loadDailyRevenue();
}

document.addEventListener("DOMContentLoaded", initSalesReport);

async function loadSalesKpi() {
    const [actualRes, predictRes] = await Promise.all([
        fetch('/api/analytics/orders/monthly-revenue'),
        fetch('/api/analytics/predicted/monthly-revenue')
    ]);

    const actual = await actualRes.json();
    const predicted = await predictRes.json();

    const sorted = actual.sort((a, b) => new Date(a.date) - new Date(b.date));
    const current = sorted.at(-1);
    const previous = sorted.at(-2);
    const predictedNext = predicted[0];

    const curr = current.value ;
    const prev = previous.value;
    const pred = predictedNext.value;

    const diff = ((curr - prev) / prev * 100).toFixed(1);
    const predDiff = ((pred - curr) / curr * 100).toFixed(1);

    const currentEl = document.getElementById("current-sales");

    const predictedEl = document.getElementById("predicted-sales");


    currentEl.textContent = `${curr.toLocaleString()} 원 (${diff >= 0 ? "▲" : "▼"} ${Math.abs(diff)}%)`;
    currentEl.className = "kpi-value " + getKpiClass(diff);
    currentEl.title = getAnomalyTitle(diff);

    predictedEl.textContent = `${pred.toLocaleString()} 원 (${predDiff >= 0 ? "▲" : "▼"} ${Math.abs(predDiff)}%)`;
    predictedEl.className = "kpi-value " + getKpiClass(predDiff);
    predictedEl.title = getAnomalyTitle(predDiff);

    renderSalesChart(sorted, predicted);
}

function renderSalesChart(actual, predicted) {
    const labels = [...actual.map(d => d.date.slice(0, 7)), ...predicted.map(d => d.date.slice(0, 7))];
    const actualValues = actual.map(d => d.value / 100000000);
    const predictedValues = new Array(actual.length).fill(null).concat(predicted.map(d => d.value / 100000000));

    const ctx = document.getElementById('salesTrendChart').getContext('2d');
    if (window.salesChartInstance) window.salesChartInstance.destroy();

    window.salesChartInstance = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [
                {
                    label: '실제 매출 (억원)',
                    data: actualValues,
                    borderColor: 'rgba(54, 162, 235, 1)',
                    backgroundColor: 'rgba(54, 162, 235, 0.1)',
                    borderWidth: 2,
                    tension: 0.3
                },
                {
                    label: '예측 매출 (억원)',
                    data: predictedValues,
                    borderColor: 'rgba(255, 99, 132, 1)',
                    backgroundColor: 'rgba(255, 99, 132, 0.1)',
                    borderDash: [5, 5],
                    borderWidth: 2,
                    tension: 0.3
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: '실제 vs 예측 매출 추이'
                }
            },
            scales: {
                y: {
                    beginAtZero: false,
                    ticks: {
                        callback: val => `${val} 억`
                    }
                }
            }
        }
    });
}

async function loadDailyRevenue() {
    const [actualRes, predictRes] = await Promise.all([
        fetch("/api/analytics/orders/daily-revenue"),
        fetch("/api/analytics/predicted/daily-revenue")
    ]);

    const actual = await actualRes.json();
    const predicted = await predictRes.json();


    const recentActual = actual.slice(-7);

    const upcomingPredicted = predicted.slice(0, 7);

    const labels = [...recentActual.map(d => d.date), ...upcomingPredicted.map(d => d.date)];
    const actualValues = recentActual.map(d => d.value);
    const predictedValues = new Array(recentActual.length).fill(null).concat(upcomingPredicted.map(d => d.value));

    const ctx = document.getElementById("dailyRevenueChart").getContext("2d");
    if (window.dailyChartInstance) window.dailyChartInstance.destroy();

    window.dailyChartInstance = new Chart(ctx, {
        type: "line",
        data: {
            labels,
            datasets: [
                {
                    label: "실제 매출",
                    data: actualValues,
                    borderColor: "#3e95cd",
                    fill: false,
                    tension: 0.3
                },
                {
                    label: "예측 매출",
                    data: predictedValues,
                    borderColor: "#e74c3c",
                    borderDash: [5, 5],
                    fill: false,
                    tension: 0.3
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: "📅 최근 7일 + 향후 7일 매출"
                },
                tooltip: { enabled: true },
                legend: { display: true }
            },
            scales: {
                x: {
                    ticks: {
                        autoSkip: true,
                        maxTicksLimit: 7
                    }
                },
                y: { beginAtZero: false }
            }
        }
    });


    const today = recentActual.at(-1).value;
    const yesterday = recentActual.at(-2)?.value ?? today;
    const tomorrow = upcomingPredicted[0].value;

    const todayDiff = (((today - yesterday) / yesterday) * 100).toFixed(1);
    const tomorrowDiff = (((tomorrow - today) / today) * 100).toFixed(1);

    const todayEl = document.getElementById("today-revenue");
    const tomorrowEl = document.getElementById("tomorrow-revenue");

    todayEl.textContent = `${today.toLocaleString()} 원 (${todayDiff > 0 ? "▲" : "▼"} ${Math.abs(todayDiff)}%)`;
    tomorrowEl.textContent = `${tomorrow.toLocaleString()} 원 (${tomorrowDiff > 0 ? "▲" : "▼"} ${Math.abs(tomorrowDiff)}%)`;

    todayEl.className = "kpi-value " + getKpiClass(todayDiff);
    tomorrowEl.className = "kpi-value " + getKpiClass(tomorrowDiff);

    if (isAnomaly(tomorrowDiff)) {
        tomorrowEl.classList.add("anomaly");
        tomorrowEl.title = "예측값이 최근 평균 대비 이상치입니다.";
    }
}


function getKpiClass(diff) {
    const d = parseFloat(diff);
    if (d >= 10) return "hot";
    if (d <= -10) return "cold";
    return "normal";
}

function isAnomaly(diff) {
    return Math.abs(parseFloat(diff)) > 30;
}

function getAnomalyTitle(diff) {
    return isAnomaly(diff) ? "예측값이 최근 평균 대비 이상치입니다." : "";
}
