import logging
from app.models.chat import ChatState
from app.crud.order import get_orders, format_orders_for_llm, get_order_id_by_code
from app.crud.order_item import get_order_items, format_order_items_for_llm
from app.retriever.vector import load_vector_db
from app.services.LLMService import LLMService

logger = logging.getLogger(__name__)
vector_store = load_vector_db()
llm_service = LLMService()

#주문 관련 질문 처리 노드
async def order_node(state: ChatState) -> ChatState:
    user_id = state.user_id
    question = state.question.strip()

    if not user_id:
        return state.copy(update={"answer": "주문 조회를 위해 먼저 로그인해주세요."})

    # 정책' 키워드가 포함된 경우 → RAG 문서 검색
    policy_keywords = ["정책", "취소 정책", "배송 정책", "주문 정책", "환불 조건", "교환 기준"]
    if any(kw in question for kw in policy_keywords):
        try:
            docs = await vector_store.asimilarity_search(question, k=3)
            context = "\n\n".join([doc.page_content for doc in docs]) if docs else "관련 문서를 찾을 수 없습니다."
            state.context = context
            answer = await llm_service.generate_response(state)
            return state.copy(update={"answer": answer})
        except Exception as e:
            logger.warning(f"[order_node-RAG] 문서 검색 실패: {e}")
            return state.copy(update={"answer": "주문 관련 정책을 불러오는 중 오류가 발생했습니다."})

    #  상세 조회: 주문코드가 있는 경우
    order_code = getattr(state, "order_code", None)
    if order_code:
        order_id = await get_order_id_by_code(order_code, user_id)
        if not order_id:
            return state.copy(update={"answer": f"주문번호 {order_code}가 존재하지 않거나 접근 권한이 없습니다."})
        items = await get_order_items(order_id)
        answer = format_order_items_for_llm(items)
        return state.copy(update={
            "answer": answer,
            "order_detail": items,
            "type": "order_detail"
        })

    #  일반 목록 조회
    orders = await get_orders(user_id)
    answer = format_orders_for_llm(orders)
    return state.copy(update={
        "answer": answer,
        "order_info": orders,
        "type": "order_list"
    })