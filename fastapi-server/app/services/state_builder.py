import logging
from typing import Optional
from app.models.chat import ChatRequest, ChatState
from app.services.LLMService import LLMService
from app.utills.followup import is_follow_up
from app.services.message_service import get_recent_messages

logger = logging.getLogger(__name__)

async def reconstruct_previous_state(user_id: int) -> Optional[ChatState]:
    logger.info(f"[reconstruct_previous_state] user_id: {user_id}")

    messages = get_recent_messages(user_id, limit=5)

    if not messages:
        logger.info("[reconstruct_previous_state] 이전 메시지 없음.")
        return None

    history = [{"role": msg["role"], "content": msg["content"]} for msg in messages]
    previous_intent = next((msg["intent"] for msg in reversed(messages)
                            if msg["role"] == "user" and msg["intent"]), "")
    logger.debug(f"[reconstruct_previous_state] 복원된 history: {history}")

    return ChatState(
        question="",
        intent=previous_intent,
        follow_up=False,
        user_id=user_id,
        history=history
    )


async def prepare_state(chat_request: ChatRequest, previous_state: Optional[ChatState]) -> ChatState:
    logger.info(f"[prepare_state] 사용자 질문: {chat_request.query}")

    llm_service = LLMService()

    # 이전 질문 추출
    previous_question = None
    if previous_state and previous_state.history:
        for msg in reversed(previous_state.history):
            if msg["role"] == "user":
                previous_question = msg["content"]
                break

    # 현재 질문 + 이전 질문 기반 의도 분류
    current_intent = await llm_service.classify_intent(chat_request.query, previous_question)
    logger.info(f"[prepare_state] 분류된 intent: {current_intent}")

    previous_intent = previous_state.intent if previous_state else None
    follow_up = is_follow_up(current_intent, previous_intent, chat_request.query)
    logger.info(f"[prepare_state] 이전 intent: {previous_intent}, follow_up: {follow_up}")

    history = previous_state.history if follow_up and previous_state else []
    logger.debug(f"[prepare_state] 적용될 history 길이: {len(history)}")

    return ChatState(
        question=chat_request.query,
        intent=current_intent,
        follow_up=follow_up,
        user_id=chat_request.user_id,
        history=history
    )

