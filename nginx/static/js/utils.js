// 특정 상태코드르 화면에 한글로 바인딩
export const statusMapping = {
    LO0101: '로그인 성공',
    LO0102: '로그인 실패',
    LO0103: '로그인 실패',
    ME0101: '고객',
    ME0102: '매니저',
    ME0103: '최고 관리자 또는 관리자 역할',
    ME0201: '고객 또는 관리자 활성화',
    ME0202: '고객 또는 관리자 탈퇴',
    ME0203: '고객 또는 관리자 일시정지',
    ME0301: '홈',
    ME0302: '사무실 또는 회사',
    ME0303: '기타',
    ME0401: '재직',
    ME0402: '휴직',
    ME0403: '퇴사',
    OR0101: '주문 접수',
    OR0102: '주문 완료',
    OR0103: '주문 취소 요청',
    OR0104: '주문 취소 완료',
    OR0201: '결제 대기 ',
    OR0202: '결제 완료 ',
    OR0203: '결제 취소 요청 ',
    OR0204: '결제 실패 ',
    OR0301: '환불 대기 ',
    OR0302: '환불 승인 ',
    OR0303: '환불 거절 ',
    OR0304: '환불 완료 ',
    OR0305: '환불 취소 ',
    OR0401: '주문 완료',
    OR0402: '주문 일부 취소',
    OR0403: '주문 취소 ',
    OR0501: '카드 결제',
    OR0502: '계좌 이체',
    SH0101: '배송 준비',
    SH0102: '배송 중',
    SH0103: '배송 완료',
    SH0104: '배송 지연',
    IN0101: '일반 대기',
    IN0102: '진행 중 ',
    IN0103: '해결 완료 ',
    IN0201: '주문 문의',
    IN0202: '배송 문의',
    IN0203: '환불 문의',
    IN0204: '기타 문의',
    IN0301: '문의 낮은 우선순위',
    IN0302: '문의 중간 우선순위',
    IN0303: '문의 높은 우선순위',
    IN0401: '한국어',
    IN0402: '미국(또는 영어)',
    DC0101: '정률할인',
    DC0102: '정액할인',
    PR0101: '품절',
    PR0102: '단종 ',
    PR0103: '판매 중지',
    PR0104: '출시 후 판매 중',
    PR0105: '사전 예약',
    PR0106: '판매 대기',
    PR0201: '카테고리 노출 중',
    PR0202: '카테고리 폐지',
    PR0203: '카테고리 일시적 숨김',
    ST0101: '매장',
    ST0102: '창고',
    ST0201: '매장 영업 중',
    ST0202: '매장 오픈 예정',
    ST0203: '매장 준비중',
    ST0204: '매장 이전 예정',
    ST0205: '매장 폐점',
    ST0301: '재고 있는',
    ST0302: '재고 없는',
    ST0401: '출시 입고',
    ST0402: '재입고',
    ST0403: '반품 입고',
    ST0404: '이동 입고',
    ST0501: '판매',
    ST0502: '폐기',
    ST0503: '샘플 제공',
    ST0601: '물류 이동 출발',
    ST0602: '물류 이동중',
    ST0603: '물류 도착',
    ST0701: '물류 이동 승인 대기중',
    ST0702: '물류 이동 승인',
    ST0703: '물류 이동 반려',
    ST0801: '차량 이동',
    ST0802: '오토바이 이동',
    ST0803: '도보 이동',
    true: '활성',
    false: '비활성'
};

// css 추가
export function statusClass(status) {
    switch (status) {
        case 'OR0101':
            return 'ORDER_PLACED';
        case 'OR0102':
            return 'ORDER_COMPLETED';
        case 'OR0201':
            return 'PAYMENT_PENDING';
        case 'OR0202':
            return 'PAYMENT_COMPLETED';
        case 'OR0204':
            return 'PAYMENT_FAILED';
        case 'SH0101':
            return 'SH_PREPARING';
        case 'SH0102':
            return 'SH_IN_TRANSIT';
        case 'SH0103':
            return 'SH_DELIVERED';
        case 'OR0104':
            return 'ORDER_CANCEL_COMPLETED';
        case 'OR0403':
            return 'CANCELLED';
        default:
            return '';
    }
}

// 대시보드 프로그래스 바 컬러
export function progressColor(item) {
    if (item.progress >= 75) return 'green';
    else if (item.progress >= 50) return 'yellow';
    else return 'red';
}
