from app.crud.order import get_orders, format_orders_for_llm
from app.models.chat import ChatState
import logging

logger = logging.getLogger(__name__)

async def order_node(state: ChatState) -> ChatState:
    """
    세션 인증된 user_id를 기반으로 주문 조회 (ChatState 사용)
    """
    logger.info(f"[order_node] user_id: {state.user_id}")

    orders = await get_orders(state.user_id)  # state.user_id로 접근
    logger.info(f"[order_node] 주문 수: {len(orders)}")

    if not orders:
        message = "현재 주문 내역이 없습니다."
    else:
        message = format_orders_for_llm(orders)

    return state.copy(update={
        "answer": message,
        "order_info": orders
    })
