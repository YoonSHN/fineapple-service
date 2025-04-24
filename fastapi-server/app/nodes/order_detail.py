from app.crud.order import get_order_id_by_code, is_user_order
from app.crud.order_item import get_order_items, format_order_items_for_llm
from app.models.chat import ChatState
import logging

logger = logging.getLogger(__name__)

async def order_detail_node(state: ChatState) -> ChatState:
    """
    order_code를 기반으로 로그인한 사용자의 주문 상세 내역을 조회하고 응답 메시지를 생성합니다.
    """
    logger.info(f"[order_detail_node] user_id: {state.user_id}, order_code: {state.order_code}")

    if not state.user_id or not state.order_code:
        logger.warning("[order_detail_node] user_id 또는 order_code 없음")
        return state.copy(update={"answer": "주문 상세 조회를 위해 로그인 정보와 주문번호가 필요합니다."})

    order_id = await get_order_id_by_code(state.order_code, state.user_id)
    if not order_id:
        logger.warning("[order_detail_node] 사용자의 주문 아님")
        return state.copy(update={"answer": "해당 주문은 회원님의 주문이 아닙니다."})

    items = await get_order_items(order_id)
    message = format_order_items_for_llm(items)
    logger.info(f"[order_detail_node] 주문 상세 조회 성공 - 항목 수: {len(items)}")

    return state.copy(update={"answer": message, "order_detail": {"order_id": order_id, "items": items}})