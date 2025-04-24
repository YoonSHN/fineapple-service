import logging
from app.services.LLMService import LLMService
from app.models.chat import ChatState


llm_service = LLMService()

async def intent_classifier_node(state: ChatState) -> ChatState:
    """
    LangGraph의 첫 번째 노드 - 사용자 질문에서 intent 추출
    """
    try:
        question = state.question
        print(f"[DEBUG] 질문: {question}")

        # LLM 기반 intent 분류 호출
        intent = await llm_service.classify_intent(question)
        print(f"[DEBUG] 분류된 intent: {intent}")

        return state.copy(update={"intent": intent})

    except Exception as e:
        print(f"[intent_classifier_node] 오류 발생: {e}")
        return state.copy(update={"intent": "fallback"})
