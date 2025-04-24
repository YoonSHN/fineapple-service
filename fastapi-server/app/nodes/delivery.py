from app.crud.delivery import get_shipment_status, format_shipments_for_llm
from app.models.chat import ChatState

import logging

logger = logging.getLogger(__name__)

async def delivery_node(state: ChatState) -> ChatState:
    """
    세션 인증된 user_id를 기반으로 배송 조회 (ChatState 사용)
    """
    logger.info(f"[delivery_node] user_id: {state.user_id}")

    shipments = await get_shipment_status(state.user_id)
    message = format_shipments_for_llm(shipments)
    logger.info(f"[delivery_node] 응답 생성 완료")
    return state.copy(update={"answer": message})



from app.crud.delivery import get_shipment_status, format_shipments_for_llm
from app.models.chat import ChatState
import logging

from app.nodes.rag_generate import rag_generate_node
from app.nodes.rag_retrieve_node import rag_retrieve_node

logger = logging.getLogger(__name__)

async def delivery_node_rag(state: ChatState) -> ChatState:
    question = state.question.strip()
    user_id = state.user_id

    if not user_id:
        return state.copy(update={
            "answer": "배송 조회를 위해서는 먼저 로그인 해주세요."
        })

    # 정책 관련 키워드 → RAG 응답
    policy_keywords = ["배송비", "배송 기간", "배송정책", "배송비용", "배송 소요", "배송 걸리는","주문한 상품 언제 오는지"]
    if any(kw in question for kw in policy_keywords):
        state = await rag_retrieve_node(state)  # 문서 검색
        return await rag_generate_node(state)  # LLM 응답 생성

    # DB에서 배송 현황 조회
    try:
        shipment_list = await get_shipment_status(user_id)
        formatted = format_shipments_for_llm(shipment_list)
        return state.copy(update={
            "answer": formatted,
            "shipment_list": shipment_list
        })
    except Exception as e:
        logger.exception("[delivery_node] 배송 정보 조회 실패")
        return state.copy(update={
            "answer": "죄송합니다. 배송 정보를 조회하는 중 문제가 발생했습니다."
        })