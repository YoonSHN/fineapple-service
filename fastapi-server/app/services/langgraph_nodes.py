import logging
from app.models.chat import ChatState
from app.utills.extract import extract_order_code
from app.services.order_service import get_user_orders, get_order_details
from app.database.session import get_db

from app.tools.chat_tools import (
    class_intent_tool,
    retrieve_relevant_docs,
    generate_response_tool,
)

logger = logging.getLogger(__name__)

async def analyze_question(state: ChatState) -> ChatState:
    logger.info(f"[analyze_question] 사용자 질문: {state.question}")
    intent = await class_intent_tool(state.question)
    order_code = extract_order_code(state.question)
    logger.info(f"[analyze_question] 분류된 intent: {intent}, 추출된 주문코드: {order_code}")
    return state.copy(update={"intent": intent, "order_code": order_code})

async def retrieve_docs(state: ChatState) -> ChatState:
    logger.info(f"[retrieve_docs] 문서 검색 시작 - intent: {state.intent}")
    context = await retrieve_relevant_docs(state)
    if context and "관련 문서를 찾을 수 없습니다" not in context:
        logger.info(f"[retrieve_docs] 문서 검색 성공 - 길이: {len(context)}")
    else:
        logger.warning("[retrieve_docs] 관련 문서를 찾지 못함")
    return state.copy(update={"context": context})

async def retrieve_order_info(state: ChatState) -> ChatState:
    logger.info(f"[retrieve_order_info] intent: {state.intent}, user_id: {state.user_id}")
    if state.intent not in ["주문조회", "배송조회", "주문상태","주문상세"]:
        logger.info("[retrieve_order_info] 주문 관련 아님, 생략")
        return state

    if not state.user_id:
        logger.warning("[retrieve_order_info] user_id 없음, 생략")
        return state

    db = next(get_db())
    orders = get_user_orders(db, state.user_id)
    logger.info(f"[retrieve_order_info] 전체 주문 수: {len(orders)}")

    if state.order_code:
        orders = [o for o in orders if str(o["order_code"]) == state.order_code]
        logger.info(f"[retrieve_order_info] 필터링된 주문 수: {len(orders)}")

    order_detail = None
    if state.intent == "주문상세" and orders:
        order_id = orders[0].get("order_id")
        order_detail = get_order_details(db, order_id, state.user_id)
        logger.info(f"[retrieve_order_info] 주문 상세 조회 - order_id: {order_id}")


    return state.copy(update={
        "order_info": orders,
        "order_detail": order_detail
    })

async def generate_answer(state: ChatState) -> ChatState:
    logger.info(f"[generate_answer] 응답 생성 시작 - intent: {state.intent}")
    if state.context:
        logger.debug(f"[generate_answer] context 길이: {len(state.context)}")
    if state.order_info:
        logger.debug(f"[generate_answer] 주문 정보 포함됨")

    answer = await generate_response_tool(state)
    logger.info(f"[generate_answer] 응답 생성 완료 - 길이: {len(answer)}")
    return state.copy(update={"answer": answer})
