from typing import Optional

def is_follow_up(current_intent: str, previous_intent: Optional[str], question: Optional[str] = "") -> bool:
    """
    현재의도와 이전의도를 비교해서 이 의도가 이전과 같은 이어지는 맥락인지 파악하는 함수
    :param current_intent: 현재 의도
    :param previous_intent: 이전 의도
    :param question: 질문
    :return:
    """
    if not previous_intent:
        return False

    if current_intent == previous_intent:
        return True

    intent_groups = [
        {"주문조회", "주문상태", "배송조회", "배송", "거래내역", "주문번호", "주문취소","주문상세"},
        {"결제", "환불", "결제수단", "할인", "영수증", "세금계산서"},
        {"반품", "교환", "환불요청", "수거", "회수", "교환요청"},
        {"계정", "로그인", "비밀번호", "회원탈퇴", "포인트", "적립", "등급"},
        {"제품비교", "재고", "스펙", "호환성", "사양", "브랜드", "추천", "가격", "결제","제품추천"},
        {"인사", "감사", "잡담", "이벤트", "운영시간"},
        {"비관련", "광고", "스팸"},
    ]

    for group in intent_groups:
        if current_intent in group and previous_intent in group:
            return True


    if question:
        if len(question.strip()) < 5:
            return True

    return False
