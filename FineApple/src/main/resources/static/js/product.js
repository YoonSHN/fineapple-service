let currentPage = 1;
let totalPages = 1;
let groupStart = 1;
const groupSize = 10;

let lastSearchParams = {};

export function initProduct() {
    initProductOptionEvents();
    initDeleteBtn();
    initProductOptionDeleteBtn();


    window.addEventListener('popstate', () => {
        const params = new URLSearchParams(window.location.search);
        const page = parseInt(params.get('pageNum') || '1', 10);
        const size = parseInt(params.get('pageSize') || '10', 10);
        const searchParams = Object.fromEntries(params.entries());
        lastSearchParams = searchParams;
        fetchProductList(page, size, searchParams);

        document.getElementById('product-add-btn')?.addEventListener('click', () => {
            document.getElementById('productAddModal').style.display = 'block';
        });

        document.getElementById('closeProductAddModalBtn')?.addEventListener('click', () => {
            document.getElementById('productAddModal').style.display = 'none';
        });



        document.getElementById('productAddForm')?.addEventListener('submit', async (e) => {
            document.getElementById('productAddForm')?.addEventListener('submit', async (e) => {
                e.preventDefault();

                const form = e.target;
                const formData = new FormData();

                const productDto = {
                    name: form.name.value,
                    description: form.description.value,
                    price: Number(form.price.value),
                    categoryId: Number(form.categoryId.value),
                    isActive: true,
                    targetReleaseDate: new Date().toISOString(),
                    actualReleaseDate: null,
                    saleStartDate: new Date().toISOString(),
                    saleStopDate: null,
                    saleRestartDate: null,
                    saleEndDate: null,
                    saleStatus: form.saleStatus.value
                };

                const imageDto = {
                    altText: form.altText.value,
                    productMain: form.productMain.value === 'true'
                };

                const file = form.file.files[0];
                if (!file) {
                    alert('이미지를 선택해주세요.');
                    return;
                }

                formData.append('product', JSON.stringify(productDto));
                formData.append('imageDto', JSON.stringify(imageDto));
                formData.append('file', file);
                try {
                    const res = await fetch('/api/products/upload', {
                        method: 'POST',
                        body: formData
                    });

                    if (!res.ok) throw new Error('등록 실패');
                    alert('상품 등록 성공!');
                    form.reset();
                    document.getElementById('productAddModal').style.display = 'none';

                    // 상품 목록 갱신
                    fetchProductList(currentPage, 10, lastSearchParams);
                } catch (err) {
                    alert('등록 중 오류가 발생했습니다.');
                    console.error(err);
                }
            });

        });
    });

    const params = new URLSearchParams(window.location.search);
    const page = parseInt(params.get('pageNum') || '1', 10);
    const size = parseInt(params.get('pageSize') || '10', 10);
    const searchParams = Object.fromEntries(params.entries());
    lastSearchParams = searchParams;

    fetchProductList(page, size, searchParams);
    bindPaginationEvents();

    const searchBtn = document.getElementById('product-search-btn');
    if (searchBtn) {
        searchBtn.addEventListener('click', () => {
            currentPage = 1;
            groupStart = 1;

            const productName = document.querySelector('.search-box input[name="productName"]')?.value.trim() || '';
            const isActive = document.querySelector('.search-box select[name="isActive"]')?.value || '';
            const saleStatus = document.querySelector('.search-box select[name="saleStatus"]')?.value || '';
            const sortDir = document.querySelector('.search-box select[name="sortDir"]')?.value || '';
            const startDate = document.querySelector('.search-box input[name="startDate"]')?.value || '';
            const endDate = document.querySelector('.search-box input[name="endDate"]')?.value || '';

            if (/['"%;]/.test(productName)) {
                alert('상품명에 특수문자 (\' " % ;) 는 사용할 수 없습니다.');
                return;
            }

            if (startDate && endDate && startDate > endDate) {
                alert('시작일은 종료일보다 이전이어야 합니다.');
                return;
            }

            if (productName.length > 100) {
                alert('상품명은 100자 이내입니다.');
                return;
            }

            const searchParams = {
                pageNum: currentPage,
                pageSize: 10,
                name: productName,
                isActive,
                saleStatus,
                sortDir,
                startDate,
                endDate
            };

            const url = new URL(window.location.href);
            url.search = new URLSearchParams(searchParams).toString();
            history.pushState({}, '', url);

            fetchProductList(currentPage, 10, searchParams);
        });
    }

    // 등록 모달 오픈 시 상품 드롭다운 로딩
    const modal = document.getElementById('registerStockModal');
    const openBtn = document.getElementById('openRegisterModalBtn');
    const closeBtn = document.getElementById('closeRegisterModalBtn');

    if (openBtn && modal) {
        openBtn.addEventListener('click', () => {
            modal.style.display = 'block';
            loadProductOptions();
        });
    }

    if (closeBtn && modal) {
        closeBtn.addEventListener('click', () => {
            modal.style.display = 'none';
        });
    }

    // 바깥 클릭 시 모달 닫기
    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
        }
    });
}

export async function fetchProductList(pageNum = 1, pageSize = 10, searchParams = null) {
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
        const response = await fetch(`/api/v1/admin/products?${params.toString()}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        const result = await response.json();
        totalPages = result.pages && result.pages >= 1 ? result.pages : 1;
        currentPage = result.pageNum ?? 1;
        renderProductTable(result.list);
        renderPageNumbers();
    } catch (error) {
        console.error('Error fetching product list:', error);
    }
}

function renderProductTable(products) {
    const tableBody = document.querySelector('.product-table tbody');
    if (!tableBody) return;
    tableBody.innerHTML = '';

    products.forEach(product => {
        const createdAt = product.createdAt ? new Date(product.createdAt).toLocaleString() : '';
        const updatedAt = product.updatedAt ? new Date(product.updatedAt).toLocaleString() : '';
        const row = `
            <tr>
                <td>${product.productId ?? ''}</td>
                <td>${product.productImageUrl ? `<img src="${product.productImageUrl}" alt="상품 이미지" style="width: 80px; height: 50px; object-fit: contain; border-radius: 6px;">` : '-'}</td>
                <td>${product.productName ?? ''}</td>
                <td>${product.categoryName ?? '-'}</td>
                <td>${product.price?.toLocaleString() ?? ''}</td>
                <td>${createdAt}</td>
                <td>${updatedAt}</td>
                <td>
                    <button class="view-product-detail-btn" data-product-id="${product.productId ?? ''}">
                        상세보기
                    </button>
                </td>
                <td>
                    <button class="view-product-option-add-btn" data-product-id="${product.productId ?? ''}">
                        옵션 추가
                    </button>
                </td>
            </tr>
        `;
        tableBody.insertAdjacentHTML('beforeend', row);
    });

    tableBody.querySelectorAll('.view-product-detail-btn').forEach(btn => {
        btn.addEventListener('click', async () => {
            const productId = btn.getAttribute('data-product-id');
            if (productId) {
                try {
                    const res = await fetch(`/api/v1/admin/products/${productId}`);
                    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
                    const productData = await res.json();
                    const realProduct = Array.isArray(productData) && productData.length > 0
                        ? productData[0]
                        : productData;
                    showProductDetailModal(realProduct);
                } catch (error) {
                    console.error('Error fetching product detail:', error);
                }
            }
        });
    });
}

function renderPageNumbers() {
    const container = document.getElementById('product-pageNumbers');
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
            fetchProductList(currentPage, 10);
        });
        container.appendChild(btn);
    }
    togglePaginationButtons();
}

function togglePaginationButtons() {
    const prevBtn = document.getElementById('product-prevPage');
    const nextBtn = document.getElementById('product-nextPage');
    if (prevBtn) prevBtn.style.display = groupStart > 1 ? 'inline-block' : 'none';
    if (nextBtn) nextBtn.style.display = groupStart + groupSize <= totalPages ? 'inline-block' : 'none';
}

function bindPaginationEvents() {
    const prevBtn = document.getElementById('product-prevPage');
    const nextBtn = document.getElementById('product-nextPage');
    if (prevBtn) {
        prevBtn.onclick = () => {
            if (groupStart > 1) {
                groupStart = Math.max(1, groupStart - groupSize);
                currentPage = groupStart;
                fetchProductList(currentPage, 10);
                renderPageNumbers();
            }
        };
    }
    if (nextBtn) {
        nextBtn.onclick = () => {
            if (groupStart + groupSize <= totalPages) {
                groupStart += groupSize;
                currentPage = groupStart;
                fetchProductList(currentPage, 10);
                renderPageNumbers();
            }
        };
    }
    togglePaginationButtons();
}

function showProductDetailModal(product) {
    const setText = (id, value) => {
        const el = document.getElementById(id);
        if (el) el.textContent = value ?? '-';
    };
    setText('modalProductId', product.productId);
    setText('modalProductName', product.productName);
    setText('modalPrice', product.price?.toLocaleString());
    setText('modalSaleStatus', product.saleStatus?.toLocaleString());
    setText('modalCategoryName', product.categoryName?.toLocaleString());
    setText('modalDescription', product.description);
    setText('modalCreatedAt', product.createdAt ? new Date(product.createdAt).toLocaleString() : '-');
    setText('modalUpdatedAt', product.updatedAt ? new Date(product.updatedAt).toLocaleString() : '-');
    setText('modalIsActive', product.isActive);
    if (Array.isArray(product.options)) renderProductOptions(product.options);

    document.getElementById('productDetailModal').style.display = 'block';

    const closeBtn = document.getElementById('closeProductDetailModalBtn');
    if (closeBtn) {
        closeBtn.onclick = () => {
            document.getElementById('productDetailModal').style.display = 'none';
        };
    }
}

function renderProductOptions(options) {
    const tbody = document.getElementById('productOptionTableBody');
    if (!tbody) return;
    tbody.innerHTML = '';

    options.forEach(opt => {
        const row = `
      <tr data-option-id="${opt.optionId}">
        <td>${opt.optionId ?? ''}</td>
        <td>${opt.optionName ?? ''}</td>
        <td>${opt.optionValue ?? ''}</td>
        <td>${opt.additionalPrice?.toLocaleString() ?? ''}</td>
      </tr>
    `;
        tbody.insertAdjacentHTML('beforeend', row);
    });

    tbody.querySelectorAll('tr').forEach(row => {
        row.addEventListener('click', () => {
            tbody.querySelectorAll('tr').forEach(r => r.classList.remove('selected'));
            row.classList.add('selected');
        });
    });
}




document.getElementById('productAddForm')?.addEventListener('submit', async (e) => {
    e.preventDefault();

    const form = e.target;
    const formData = new FormData();

    const productDto = {
        name: form.name.value,
        description: form.description.value,
        price: Number(form.price.value),
        categoryId: Number(form.categoryId.value),
        isActive: true,
        targetReleaseDate: new Date().toISOString(),
        actualReleaseDate: null,
        saleStartDate: new Date().toISOString(),
        saleStopDate: null,
        saleRestartDate: null,
        saleEndDate: null,
        saleStatus: form.saleStatus.value
    };

    const imageDto = {
        altText: form.altText.value,
        productMain: form.productMain.value === 'true'
    };

    const file = form.file.files[0];
    if (!file) {
        alert('이미지 선택');
        return;
    }

    const allowedExtensions = ['jpg', 'jpeg', 'png', 'webp'];
    const fileName = file.name;
    const fileExtension = fileName.split('.').pop()?.toLowerCase();

    if (!allowedExtensions.includes(fileExtension)) {
        alert(`이미지 파일만 업로드 가능합니다. 허용된 확장자: ${allowedExtensions.join(', ')}`);
        return;
    }

    formData.append('product', JSON.stringify(productDto));
    formData.append('imageDto', JSON.stringify(imageDto));
    formData.append('file', file);

    try {
        const res = await fetch('/api/v1/admin/products/upload', {
            method: 'POST',
            body: formData
        });

        if (!res.ok) throw new Error('등록 실패함');
        alert('상품 등록 성공함');
        form.reset();
        document.getElementById('productAddModal').style.display = 'none';


        fetchProductList(currentPage, 10, lastSearchParams);
    } catch (err) {
        alert('등록 중 오류가 발생');
        console.error(err);
    }
});

function initProductOptionEvents() {
    const optionModal = document.getElementById('productOptionAddModal');
    const optionForm = document.getElementById('productOptionAddForm');
    const closeOptionModalBtn = document.getElementById('closeProductOptionAddModalBtn');

    document.body.addEventListener('click', (e) => {
        if (e.target.classList.contains('view-product-option-add-btn')) {
            const productId = e.target.getAttribute('data-product-id');
            document.getElementById('optionProductId').value = productId;
            optionModal.style.display = 'block';
        }
    });

    closeOptionModalBtn?.addEventListener('click', () => {
        optionModal.style.display = 'none';
    });

    window.addEventListener('click', (e) => {
        if (e.target === optionModal) {
            optionModal.style.display = 'none';
        }
    });

    optionForm?.addEventListener('submit', async (e) => {
        e.preventDefault();

        const form = e.target;
        const productId = document.getElementById('optionProductId')?.value;
        const optionName = form.optionName.value.trim();
        const optionValue = form.optionValue.value.trim();
        const additionalPrice = parseFloat(form.additionalPrice.value);

        if (!optionName || !optionValue || isNaN(additionalPrice)) {
            alert('모든 항목을 정확히 입력해주세요.');
            return;
        }

        try {
            const res = await fetch(`/api/v1/admin/products/${productId}/options`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ optionName, optionValue, additionalPrice })
            });

            if (!res.ok) throw new Error('옵션 등록 실패');
            alert('옵션이 등록되었습니다.');
            form.reset();
            optionModal.style.display = 'none';

            const detailRes = await fetch(`/api/v1/admin/products/${productId}`);
            const product = await detailRes.json();
            const realProduct = Array.isArray(product) ? product[0] : product;
            showProductDetailModal(realProduct);
        } catch (err) {
            alert('옵션 등록 중 오류가 발생했습니다.');
            console.error(err);
        }
    });
}


function initDeleteBtn() {
    document.getElementById('toggleProductActiveBtn')?.addEventListener('click', async () => {
        const productId = document.getElementById('modalProductId')?.textContent;
        if (!productId) return;

        if (!confirm('상품 상태를 변경하시겠습니까?')) return;

        try {
            const res = await fetch(`/api/v1/admin/products/${productId}/toggle-active`, {
                method: 'PATCH'
            });

            if (!res.ok) throw new Error('상태 변경 실패');

            alert('상품 상태가 변경되었습니다.');

            const detailRes = await fetch(`/api/v1/admin/products/${productId}`);
            const product = await detailRes.json();
            const realProduct = Array.isArray(product) ? product[0] : product;
            showProductDetailModal(realProduct);
        } catch (err) {
            alert('상태 변경 중 오류가 발생했습니다.');
            console.error(err);
        }
    });


}


function initProductOptionDeleteBtn() {
    document.getElementById('deleteProductOptionBtn')?.addEventListener('click', async () => {
        const selectedRow = document.querySelector('#productOptionTableBody tr.selected');
        if (!selectedRow) {
            alert('삭제할 옵션을 선택해주세요.');
            return;
        }

        const optionId = selectedRow.getAttribute('data-option-id');
        const productId = document.getElementById('modalProductId')?.textContent;

        if (!confirm('정말 이 옵션을 삭제하시겠습니까?')) return;

        try {
            const res = await fetch(`/api/v1/admin/products/${productId}/options/${optionId}`, {
                method: 'DELETE'
            });

            if (!res.ok) throw new Error('옵션 삭제 실패');
            alert('옵션이 삭제되었습니다.');


            const detailRes = await fetch(`/api/v1/admin/products/${productId}`);
            const product = await detailRes.json();
            const realProduct = Array.isArray(product) ? product[0] : product;
            await showProductDetailModal(realProduct);
        } catch (err) {
            alert('옵션 삭제 중 오류가 발생했습니다.');
            console.error(err);
        }
    });

}

async function enterEditMode(productId) {
    try {
        const res = await fetch(`/api/v1/admin/products/${productId}`);
        if (!res.ok) throw new Error('상품 정보 조회 실패');

        const productData = await res.json();
        const product = Array.isArray(productData) ? productData[0] : productData;

        const input = (id, value, type = "text") => {
            const el = document.getElementById(id);
            if (el) {
                el.innerHTML = `<input type="${type}" id="edit-${id}" value="${value ?? ''}" />`;
            }
        };

        input("modalProductName", product.productName);
        input("modalDescription", product.description);
        input("modalPrice", product.price, "number");

        const saleStatusTd = document.getElementById("modalSaleStatus");
        if (saleStatusTd) {
            saleStatusTd.innerHTML = `
        <select id="edit-modalSaleStatus">
          <option value="PR0101" ${product.saleStatus === 'PR0101' ? 'selected' : ''}>판매 중</option>
          <option value="PR0102" ${product.saleStatus === 'PR0102' ? 'selected' : ''}>판매 중지</option>
          <option value="PR0103" ${product.saleStatus === 'PR0103' ? 'selected' : ''}>판매 예정</option>
        </select>
      `;
        }


        const imgRow = document.getElementById("modalProductImageUpload");
        if (imgRow) {
            imgRow.innerHTML = `<input type="file" id="edit-product-image" accept="image/*" />`;
        }


        const editBtnContainer = document.getElementById("editModeBtn");
        if (editBtnContainer) {
            editBtnContainer.style.display = "none";
        }

        let actionControls = document.getElementById("edit-controls");
        if (!actionControls) {
            actionControls = document.createElement("div");
            actionControls.id = "edit-controls";
            const parent = document.querySelector("#productDetailModal .modal-content > div:last-of-type");
            parent.appendChild(actionControls);
        }

        actionControls.innerHTML = `
      <button id="saveProductBtn" class="modal-submit-btn">수정 완료</button>
      <button id="cancelEditBtn" class="modal-delete-btn">취소</button>
    `;
        actionControls.style.display = "flex";
        actionControls.style.justifyContent = "flex-end";
        actionControls.style.gap = "10px";
        actionControls.style.marginTop = "0";

        document.getElementById("saveProductBtn").onclick = () => saveProduct(product.productId);
        document.getElementById("cancelEditBtn").onclick = () => cancelEdit(product);
    } catch (err) {
        alert('수정 모드 진입 실패');
        console.error(err);
    }
}



// 수정 저장 요청
async function saveProduct(productId) {
    const formData = new FormData();

    const updateDto = {
        name: document.getElementById("edit-modalProductName")?.value,
        description: document.getElementById("edit-modalDescription")?.value,
        price: parseInt(document.getElementById("edit-modalPrice")?.value),
        saleStatus: document.getElementById("edit-modalSaleStatus")?.value,
        isActive: true, // 혹시 수정에서 비활성화 기능도 포함하면 추가변경예정
        productMain: true
    };

    formData.append("product", JSON.stringify(updateDto));

    const imageInput = document.getElementById("edit-product-image");
    const imageFile = imageInput?.files?.[0];
    if (imageFile) {
        formData.append("file", imageFile);
    }

    try {
        const res = await fetch(`/api/v1/admin/products/${productId}/update`, {
            method: "PATCH",
            body: formData
        });

        if (!res.ok) throw new Error("수정 실패");
        alert("상품 수정 성공!");
        document.getElementById("productDetailModal").style.display = "none";
        fetchProductList(currentPage, 10, lastSearchParams);
    } catch (err) {
        alert("수정 중 오류 발생");
        console.error(err);
    }
}

// 수정 취소 시 원래 상세보기로 되돌림
function cancelEdit(product) {
    showProductDetailModal(product);
    const controls = document.getElementById("edit-controls");
    if (controls) controls.remove();
}

// td - input 변환 유틸 함수
function convertTdToInput(id, value, type = "text") {
    const td = document.getElementById(id);
    if (td) {
        td.innerHTML = `<input type="${type}" id="edit-${id}" value="${value ?? ''}" />`;
    }
}

document.getElementById('product-add-btn')?.addEventListener('click', () => {
    const modal = document.getElementById('productAddModal');
    if (modal) modal.style.display = 'block';
    loadCategories()
});

document.getElementById('closeProductAddModalBtn')?.addEventListener('click', () => {
    const modal = document.getElementById('productAddModal');
    if (modal) modal.style.display = 'none';
});

async function loadCategories() {
    try {
        const res = await fetch("/api/v1/categories");
        const data = await res.json();

        const select = document.getElementById("categorySelect");
        if (!select) return;


        select.innerHTML = `<option value="">카테고리를 선택하세요</option>`;

        data.forEach(category => {
            const option = document.createElement("option");
            option.value = category.categoryId;
            option.textContent = category.name;
            select.appendChild(option);
        });
    } catch (err) {
        console.error("카테고리 불러오기 실패:", err);
    }
}

window.enterEditMode = enterEditMode;
window.convertTdToInput = convertTdToInput;


