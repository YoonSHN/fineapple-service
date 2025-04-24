// Format date
function formatDate(dateString) {
    if (!dateString) return "N/A";
    const date = new Date(dateString);
    const options = { day: 'numeric', month: 'short', year: 'numeric', hour: '2-digit', minute: '2-digit' };
    return date.toLocaleDateString('ko-KR', options);
}

// Get status badge HTML
function getStatusBadgeHTML(status) {
    let classes = "status-badge ";
    let text = status;

    switch(status) {
        case "PENDING":
            classes += "status-pending";
            text = "대기중";
            break;
        case "IN_PROGRESS":
            classes += "status-in-progress";
            text = "처리중";
            break;
        case "COMPLETED":
            classes += "status-solved";
            text = "완료";
            break;
        default:
            classes += "status-pending";
    }

    return `<span class="${classes}">${text}</span>`;
}

// Render inquiry detail
function renderInquiryDetail(inquiry) {
    if (!inquiry) {
        return getNotFoundHTML();
    }

    // Main inquiry card
    const mainCard = `
    <div class="inquiry-card">
      <!-- Header -->
      <div class="inquiry-header">
        <h1 class="inquiry-title">${inquiry.subject}</h1>
        <div class="inquiry-meta">
          <span>문의 유형: ${inquiry.inquiryType}</span>
          ${getStatusBadgeHTML(inquiry.inquiryStatus)}
        </div>
      </div>
      
      <!-- Ticket Details -->
      <div class="ticket-details">
        <h2 class="section-title">문의 세부 정보</h2>
        <div class="details-grid">
          <div>
            <div class="detail-item">
              <div class="detail-label">작성자</div>
              <div class="detail-value bold">${inquiry.name}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">이메일</div>
              <div class="detail-value">${inquiry.email}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">작성일</div>
              <div class="detail-value">${formatDate(inquiry.createdAt)}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">최종 수정일</div>
              <div class="detail-value">${formatDate(inquiry.updatedAt)}</div>
            </div>
          </div>
          <div>
            <div class="detail-item">
              <div class="detail-label">상태</div>
              <div class="detail-value">${getStatusBadgeHTML(inquiry.inquiryStatus)}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">처리 담당자</div>
              <div class="detail-value">${inquiry.assignedTo || 'N/A'}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">처리 완료자</div>
              <div class="detail-value">${inquiry.resolvedBy || 'N/A'}</div>
            </div>
            <div class="detail-item">
              <div class="detail-label">답변 예정일</div>
              <div class="detail-value">${formatDate(inquiry.responseDueDate)}</div>
            </div>
          </div>
        </div>
      </div>
      
      <!-- Content -->
      <div class="inquiry-content">
        <div class="user-info">
          <div class="avatar">
            ${inquiry.name.charAt(0)}
          </div>
          <div class="user-meta">
            <div class="user-name">${inquiry.name}</div>
            <div class="post-date">${formatDate(inquiry.createdAt)}</div>
          </div>
        </div>
        <div class="message-content">
          ${inquiry.content.replace(/\n/g, '<br>')}
        </div>
      </div>
    </div>
  `;

    return mainCard;
}

// Generate not found HTML
function getNotFoundHTML() {
    return `
    <div class="not-found">
      <p class="not-found-text">문의를 찾을 수 없습니다.</p>
      <a href="javascript:history.back()" class="back-button">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
          <polyline points="15 18 9 12 15 6"></polyline>
        </svg>
        <span>뒤로 가기</span>
      </a>
    </div>
  `;
}

// Mock data based on the Inquiry DTO provided from Java
const mockInquiryDTO = {
    subject: "라이선스 구매 관련 문의",
    content: "IntelliJ IDEA Ultimate 라이선스 구매 방법을 알고 싶습니다. 학생 할인이 적용되나요?\n\n대학교 이메일을 가지고 있는데, 이를 통해 라이선스를 받을 수 있나요? 할인된 가격으로 구매할 수 있는 방법이 있다면 알려주세요.",
    createdAt: "2025-04-01T09:30:00",
    updatedAt: "2025-04-01T09:30:00",
    inquiryStatus: "IN_PROGRESS",
    inquiryType: "라이선스 문의",
    email: "chulsoo.kim@example.com",
    name: "김철수",
    assignedTo: "이지원",
    resolvedBy: null,
    responseDueDate: "2025-04-08T09:30:00"
};

// Initialize the app when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    const contentContainer = document.getElementById('content-container');

    // Get inquiry ID from URL
    const urlParams = new URLSearchParams(window.location.search);
    let inquiryId = urlParams.get('id');

    // In a real application, you would fetch data based on the inquiryId
    // For demo purposes, we're using mock data

    console.log(`Fetching inquiry details for ID: ${inquiryId}`);

    // Simulate loading data
    setTimeout(function() {
        contentContainer.innerHTML = renderInquiryDetail(mockInquiryDTO);
    }, 800);
});
