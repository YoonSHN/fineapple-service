from app.models.chat import ChatState
from app.crud.refund import (
    get_payment_id_by_order_code,
    get_payment_total_amount,
    request_refund,
    get_refund_history,
    format_refunds_for_llm
)
import logging

logger = logging.getLogger(__name__)

async def payment_node(state: ChatState) -> ChatState:
    """
    order_code, refund_amount, refund_reason을 기반으로 환불 요청을 처리하는 노드
    """
    if not state.order_code or not state.refund_amount or not state.refund_reason or not state.user_id:
        return state.copy(update={"answer": "환불 요청을 위한 정보가 부족합니다. 주문번호, 사유, 금액을 다시 확인해주세요."})

    payment_id = await get_payment_id_by_order_code(state.order_code, state.user_id)
    if not payment_id:
        return state.copy(update={"answer": "해당 결제건은 회원님의 결제가 아닙니다."})

    total_amount = await get_payment_total_amount(payment_id)
    if state.refund_amount > total_amount:
        return state.copy(update={"answer": f"환불 금액이 결제 금액({total_amount:,.0f}원)을 초과할 수 없습니다."})

    await request_refund(
        payment_id=payment_id,
        refund_total_amount=state.refund_amount,
        refund_reason=state.refund_reason
    )
    return state.copy(update={"answer": f"{state.order_code} 주문의 환불 요청이 정상적으로 접수되었습니다."})