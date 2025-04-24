from app.services.LLMService import LLMService
from app.models.chat import ChatState

# 자연스러운 일상 대화 또는 잡담 키워드 정의
CASUAL_KEYWORDS = [
    "안녕", "고마워", "잘 지내", "너 뭐야", "화났어",
    "짜증나", "도와줘", "심심해"
]

llm_service = LLMService()

async def fallback_node(state: ChatState) -> ChatState:
    """
    잡담이나 비정형 질문에 대한 자연스러운 응답 생성
    - 가능한 경우 LLM으로 유연한 대화 시도
    - 일반적인 도움말도 함께 제공
    """
    try:
        question = state.question.strip()

        # 잡담 키워드가 포함된 경우 → LLM 직접 응답
        if any(keyword in question for keyword in CASUAL_KEYWORDS):
            response = await llm_service.llm.ainvoke(question)
            return state.copy(update={"answer": response.content.strip()})

        # 그 외는 추천 가능한 주제 안내 메시지 제공
        suggestions = [
            "배송/택배", "상품 추천", "환불/반품", "주문 취소",
            "상품 문의", "이벤트/할인", "회원 정보", "장바구니", "결제/구매"
        ]
        answer = (
            "죄송합니다. 아직 그 질문에 대한 정확한 답변은 준비되지 않았습니다.\n"
            "아래 항목 중 하나에 대해 물어보시면 도와드릴 수 있어요:\n" +
            "\n".join(f"- {item}" for item in suggestions)
        )
        return state.copy(update={"answer": answer})

    except Exception as e:
        print(f"[fallback_node] 오류 발생: {e}")
        return state.copy(update={"answer": "죄송합니다. 예기치 못한 오류가 발생했습니다."})