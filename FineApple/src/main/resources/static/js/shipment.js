// document.addEventListener('DOMContentLoaded', () => {
//     // shipment 페이지일 경우에만 실행
//     if (window.location.pathname === '/shipment') {
//         fetchShipments();
//     }
// });
//
// async function fetchShipments() {
//     try {
//         const res = await fetch('/api/v1/shipments');
//         if (!res.ok) throw new Error(`HTTP ${res.status}`);
//         const shipmentList = await res.json();
//         renderShipmentTable(shipmentList);
//     } catch (err) {
//         console.error('🚨 배송 정보 불러오기 실패:', err);
//     }
// }
//
// function renderShipmentTable(data) {
//     const tbody = document.getElementById('shipmentTableBody');
//     tbody.innerHTML = ''; // 초기화
//
//     if (!data.length) {
//         tbody.innerHTML = `<tr><td colspan="10" style="text-align:center;">배송 정보가 없습니다.</td></tr>`;
//         return;
//     }
//
//     data.forEach(s => {
//         const row = `
//             <tr>
//                 <td>${s.shipmentId}</td>
//                 <td>${s.trackingNumber}</td>
//                 <td><a href="${s.trackingUrl}" target="_blank">${s.trackingUrl}</a></td>
//                 <td>${formatDate(s.estimatedDeliveryDate)}</td>
//                 <td>${formatDate(s.dispatchedAt)}</td>
//                 <td>${formatDate(s.deliveredAt)}</td>
//                 <td>${s.shippingCost}</td>
//                 <td>${s.deliveryAgentName ?? '(없음)'}</td>
//                 <td>${s.city} ${s.region} ${s.roadNum} ${s.address}</td>
//                 <td>${s.storeName}</td>
//             </tr>
//         `;
//         tbody.insertAdjacentHTML('beforeend', row);
//     });
// }
//
// function formatDate(dateString) {
//     if (!dateString) return '-';
//     return new Date(dateString).toLocaleString(); // yyyy-MM-dd HH:mm
// }
//
//
// async function loadShipmentPage() {
//     try {
//         const res = await fetch('/shipment');
//         if (!res.ok) throw new Error(`HTML 페이지 로딩 실패: ${res.status}`);
//         const html = await res.text();
//         const parser = new DOMParser();
//         const doc = parser.parseFromString(html, 'text/html');
//         const newMain = doc.querySelector('main');
//         const currentMain = document.querySelector('main');
//
//         if (newMain && currentMain) {
//             currentMain.replaceWith(newMain);
//             fetchShipments(); // 새 main 영역에 shipment 데이터 렌더링
//         }
//     } catch (error) {
//         console.error('🚨 배송 페이지 불러오기 실패:', error);
//     }
// }
//
// // 배송 아이콘 클릭 시 동적 로딩 연결
// document.querySelector('.local-mall')?.addEventListener('click', (e) => {
//     e.preventDefault();
//     loadShipmentPage();
// });