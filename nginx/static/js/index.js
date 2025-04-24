const submenuItems = document.querySelectorAll('.nav-menu .has-submenu');
const overlay = document.querySelector('.overlay');
const dropdown = document.getElementById("global-dropdown");
const content = document.getElementById("dropdown-content");
const cartIcon = document.querySelector('.local-mall');
const searchIcon = document.querySelector('.material-icons[data-menu="search"]');
let hideTimer = null;

//  정적 메뉴 데이터
const staticMenuData = {
  store: {
    menu: `<h4>Apple Store</h4><ul><li>오프라인 스토어</li><li>매장 찾기</li><li>예약 구매</li></ul>`,
    info: `<h4>최신 제품</h4><p>새로운 제품과 프로모션을 만나보세요.</p>`
  },
  search: {
    menu: `
      <div class="search-dropdown-input" style="display: flex; gap: 8px; align-items: center;">
        <input type="text" placeholder="apple.com 검색하기" class="search-input-field" />
        <button class="search-icon-btn">
          <span class="material-icons">search</span>
        </button>
      </div>
      <h4>빠른 링크</h4>
      <ul>
        <li>Apple Store Online에서 쇼핑하기</li>
        <li>Apple Vision Pro</li>
        <li>AirPods</li>
        <li>Apple Intelligence</li>
        <li>Apple Trade In</li>
      </ul>
    `,
    info: `<h4>도움말</h4><p>문제가 발생하면 지원을 받으세요.</p>`
  },
  cart: {
    menu: '', // 로그인 여부에 따라 다르게 조건 채움
    info: `<h4>고객 계정</h4><p>주문 내역과 관심 상품을 확인해보세요.</p>`
  }
};

//  동적 메뉴 데이터 로더
async function getMenuData(key) {
  if (staticMenuData[key]) {
    return staticMenuData[key];
  }

  try {
    const res = await fetch(`/api/v1/categories/tree?path=${key}`);
    const categories = await res.json();
    const root = categories.find(c => c.parentId === null);
    const children = categories.filter(c => c.parentId === root.categoryId);

    return {
      menu: `
        <h4>${root.name}</h4>
        <ul> 
            ${children.map(c => `<li><a href="/store/id/${c.categoryId}">${c.name}</a></li>`).join('')}
        </ul>
      `,
      info: `
        <h4>${root.name} 선택</h4>
        <p>${root.description || ''}</p>
      `
    };
  } catch (err) {
    console.error(`메뉴 로딩 실패: ${key}`, err);
    return {
      menu: '<p>메뉴를 불러올 수 없습니다.</p>',
      info: ''
    };
  }
}

//  애니메이션 처리
function applyStaggeredAnimation() {
  const items = content.querySelectorAll('.dropdown-menu li, .dropdown-info h4, .dropdown-info p');
  items.forEach((el, index) => {
    el.classList.remove('appear');
    setTimeout(() => el.classList.add('appear'), 50 * index);
  });
}

//  서브메뉴 hover 이벤트처리
submenuItems.forEach(item => {
  item.addEventListener('mouseenter', async () => {
    clearTimeout(hideTimer);
    const key = item.dataset.menu;
    const data = await getMenuData(key);

    content.innerHTML = `
      <div class="dropdown-menu">${data.menu}</div>
      <div class="dropdown-info">${data.info}</div>
    `;
    dropdown.classList.add('show');
    overlay.classList.add('show');
    applyStaggeredAnimation();
  });

  item.addEventListener('mouseleave', () => {
    hideTimer = setTimeout(() => {
      dropdown.classList.remove('show');
      overlay.classList.remove('show');
    }, 200);
  });
});

//  공통 드롭다운 토글
dropdown.addEventListener('mouseenter', () => {
  clearTimeout(hideTimer);
  dropdown.classList.add('show');
  overlay.classList.add('show');
});
dropdown.addEventListener('mouseleave', () => {
  hideTimer = setTimeout(() => {
    dropdown.classList.remove('show');
    overlay.classList.remove('show');
  }, 200);
});
overlay.addEventListener('click', () => {
  dropdown.classList.remove('show');
  overlay.classList.remove('show');
});

//  검색 아이콘
searchIcon.addEventListener('mouseenter', async () => {
  clearTimeout(hideTimer);
  const data = await getMenuData('search');
  content.innerHTML = `
    <div class="dropdown-menu">${data.menu}</div>
    <div class="dropdown-info">${data.info}</div>
  `;
  dropdown.classList.add('show');
  overlay.classList.add('show');
  applyStaggeredAnimation();
});
searchIcon.addEventListener('mouseleave', () => {
  hideTimer = setTimeout(() => {
    dropdown.classList.remove('show');
    overlay.classList.remove('show');
  }, 200);
});

//  장바구니 아이콘
cartIcon.addEventListener('mouseenter', async () => {
  clearTimeout(hideTimer);
  const data = await getMenuData('cart');
  content.innerHTML = `
    <div class="dropdown-menu">${data.menu}</div>
    <div class="dropdown-info">${data.info}</div>
  `;
  dropdown.classList.add('show');
  overlay.classList.add('show');
  applyStaggeredAnimation();
});
cartIcon.addEventListener('mouseleave', () => {
  hideTimer = setTimeout(() => {
    dropdown.classList.remove('show');
    overlay.classList.remove('show');
  }, 200);
});

//  장바구니 클릭 시 페이지 이동
cartIcon.addEventListener('click', () => {
  window.location.href = '/carts';
});

//  로그인 여부에 따라 장바구니 메뉴 채움
document.addEventListener("DOMContentLoaded", async () => {
  const isLoggedIn = document.documentElement.dataset.loggedIn === "true";
  const userId = document.getElementById("user-id-data").getAttribute('data-user-id');

  try {
    const res = await fetch(`/api/v1/carts/${userId}`);
    const cartData = await res.json();
    const itemCount = cartData.cartProducts?.length || 0;

    if (itemCount === 0) {
      staticMenuData.cart.menu = isLoggedIn
          ? `
      <h2 style="font-size: 16px; margin-bottom: 12px;">장바구니가 비어 있습니다.</h2>
      <p style="font-size: 13px; margin-bottom: 20px;">관심 항목을 확인하거나 주문을 진행해보세요.</p>
      <ul class="cart-options">
        <li><i class="material-icons">assignment</i> 주문</li>
        <li><i class="material-icons">favorite_border</i> 관심 목록</li>
        <li><i class="material-icons">settings</i><a href="/userDetail">내 정보</a></li>
        <li><i class="material-icons">logout</i>
          <a href="#" onclick="event.preventDefault(); document.getElementById('logout-form').submit();">로그아웃</a>
        </li>
      </ul>
      <form id="logout-form" method="POST" action="/logout" style="display:none;"></form>
    `
          : `
      <h2 style="font-size: 16px; margin-bottom: 12px;">장바구니가 비어 있습니다.</h2>
      <p style="font-size: 13px; margin-bottom: 20px;">저장해둔 항목이 있는지 확인하려면 <a href="/login">로그인</a>하세요.</p>
      <ul class="cart-options">
        <li><i class="material-icons">assignment</i><a href="/OrderItemDetailPage" style="margin-left: 4px;">주문</a></li>
        <li><i class="material-icons">favorite_border</i> 관심 목록</li>
      </ul>
    `;
    } else {
      staticMenuData.cart.menu = `
    <h2 style="font-size: 16px; margin-bottom: 12px;">장바구니에 ${itemCount}개의 항목이 있습니다.</h2>
    <p style="font-size: 13px; margin-bottom: 20px;">장바구니를 확인하거나 바로 주문할 수 있어요.</p>
    <a href="/carts" class="go-to-cart-button">장바구니 보기</a>

    ${isLoggedIn
          ? `
        <ul class="cart-options" style="margin-top: 20px;">
          <li><i class="material-icons">assignment</i> 주문</li>
          <li><i class="material-icons">favorite_border</i> 관심 목록</li>
          <li><i class="material-icons">settings</i><a href="/userDetail">내 정보</a></li>
          <li><i class="material-icons">logout</i>
            <a href="#" onclick="event.preventDefault(); document.getElementById('logout-form').submit();">로그아웃</a>
          </li>
        </ul>
        <form id="logout-form" method="POST" action="/logout" style="display:none;"></form>
      `
          : `
        <ul class="cart-options" style="margin-top: 20px;">
          <li><i class="material-icons">assignment</i><a href="/OrderItemDetailPage" style="margin-left: 4px;">주문</a></li>
          <li><i class="material-icons">favorite_border</i> 관심 목록</li>
          <li><i class="material-icons">login</i><a href="/login" style="margin-left: 4px;">로그인</a></li>
        </ul>
      `}
  `;
    }
  } catch (err) {
    console.error('장바구니 메뉴 로딩 실패:', err);
    staticMenuData.cart.menu = `<p>장바구니 정보를 불러오지 못했습니다.</p>`;
  }
});


new Swiper('.product-icons__swiper', {
  slidesPerView: 8,
  spaceBetween: 20,
  loop: true,
  autoplay: { delay: 3000 },
  navigation: {
    nextEl: '.product-icons .swiper-button-next',
    prevEl: '.product-icons .swiper-button-prev',
  },
  breakpoints: {
    768: { slidesPerView: 5 },
    1024: { slidesPerView: 8 }
  }
});

// 스와이퍼 작동
new Swiper('.new-products__swiper', {
  slidesPerView: 'auto',
  spaceBetween: 20,
  loop: false,
  navigation: {
    nextEl: '.new-products .swiper-button-next',
    prevEl: '.new-products .swiper-button-prev',
  },
  breakpoints: {
    768: { slidesPerView: 2 },
    1024: { slidesPerView: 3 },
    1280: { slidesPerView: 4 }
  }
});

new Swiper('.benefit-swiper', {
  slidesPerView: 2,
  spaceBetween: 20,
  loop: false,
  navigation: {
    nextEl: '.benefit .swiper-button-next',
    prevEl: '.benefit .swiper-button-prev',
  },
  breakpoints: {
    768: { slidesPerView: 2 },
    1024: { slidesPerView: 3 },
    1280: { slidesPerView: 4 }
  }
});


// 배송정보 모달 열기 및 데이터 로드 함수
function openShipmentModal(shipmentId) {
  fetch(`/api/v1/shipments/${shipmentId}`)
      .then(response => {
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.json();
      })
      .then(data => {
        // 배송정보 데이터로 모달 내용 업데이트
        document.getElementById('shipmentStatus').textContent = data.deliveryStatus || '-';
        document.getElementById('trackingNumber').textContent = data.trackingNumber || '-';
        const trackingUrlLink = document.getElementById('trackingUrl');
        trackingUrlLink.textContent = data.trackingUrl || '-';
        trackingUrlLink.href = data.trackingUrl || '#';
        document.getElementById('estimatedDeliveryDate').textContent = data.estimatedDeliveryDate
            ? new Date(data.estimatedDeliveryDate).toLocaleString()
            : '-';
        // 모달 열기
        document.getElementById('shipmentModal').style.display = 'block';
      })
      .catch(error => console.error('Error fetching shipment details:', error));
}

// 모달 닫기 이벤트 바인딩
document.getElementById('closeShipmentModalBtn')?.addEventListener('click', () => {
  document.getElementById('shipmentModal').style.display = 'none';
});

// 예: 장바구니 메뉴 내 "주문" 버튼 클릭 시 모달 열기 (메뉴 데이터 수정 또는 별도 바인딩)
const cartOrderBtn = document.querySelector('.cart-order-btn'); // 예를 들어, 해당 버튼에 class "cart-order-btn"을 지정
if (cartOrderBtn) {
  cartOrderBtn.addEventListener('click', () => {
    // shipmentId를 sessionStorage나 다른 방식으로 미리 저장해둔 경우 사용
    const shipmentId = sessionStorage.getItem('shipmentId');
    if (shipmentId) {
      openShipmentModal(shipmentId);
    } else {
      console.error('No shipmentId found in sessionStorage.');
    }
  });
}