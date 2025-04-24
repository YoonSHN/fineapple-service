from app.models.chat import ChatState
from app.nodes.rag_generate import rag_generate_node
from app.nodes.rag_retrieve_node import rag_retrieve_node
import logging

logger = logging.getLogger(__name__)

async def recommendation_node(state: ChatState) -> ChatState:
    """
    사용자 질문 기반으로 RAG에서 상품 문서를 찾아 LLM이 추천 생성
    """
    try:
        # Step 1. 문서 검색
        state = await rag_retrieve_node(state)

        # Step 2. LLM 답변 생성
        state = await rag_generate_node(state)

        return state

    except Exception as e:
        logger.warning(f"[recommendation_node] 오류 발생: {e}")
        return state.copy(update={"answer": "상품 추천 중 오류가 발생했습니다."})