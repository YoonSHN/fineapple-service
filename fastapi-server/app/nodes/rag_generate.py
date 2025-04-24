
from app.services.LLMService import LLMService
from app.models.chat import ChatState
import logging

logger = logging.getLogger(__name__)
llm = LLMService()

async def rag_generate_node(state: ChatState) -> ChatState:
    """
    1) 이전에 채워진 state.context 와 state.question 을
    2) LLMService.generate_response 에 보내서 answer 생성
    """
    try:
        answer = await llm.generate_response(state)
        return state.copy(update={"answer": answer})
    except Exception as e:
        logger.warning(f"[rag_generate_node] 오류: {e}")
        return state.copy(update={"answer": "답변 생성 중 오류가 발생했습니다."})
