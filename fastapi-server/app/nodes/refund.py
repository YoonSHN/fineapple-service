from app.nodes.rag_retrieve_node import rag_retrieve_node
from app.nodes.rag_generate import rag_generate_node
from app.models.chat import ChatState

REFUND_KEYWORDS = ["환불", "반품"]

async def refund_node(state: ChatState) -> ChatState:
    try:
        question = state.question.strip() if state.question else ""

        if not question:
            return state.copy(update={
                "answer": "질문이 감지되지 않았습니다. 다시 시도해 주세요."
            })

        if any(kw in question for kw in REFUND_KEYWORDS):
            state = await rag_retrieve_node(state)
            return await rag_generate_node(state)

        answer = (
            "환불 관련 질문이 감지되지 않았습니다. 예: '환불 기간이 어떻게 되나요?'처럼 질문해보세요.\n"
            "도움이 필요한 항목은 아래와 같아요:\n"
            "- 환불/반품\n- 배송/택배\n- 결제/구매\n- 상품 문의"
        )
        return state.copy(update={"answer": answer})

    except Exception as e:
        print(f"[refund_node] 오류: {str(e)}")
        return state.copy(update={
            "answer": "죄송합니다. 환불 정보를 제공하는 중 오류가 발생했습니다."
        })
