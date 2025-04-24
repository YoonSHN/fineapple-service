from langgraph.graph import StateGraph, END
from app.models.chat import ChatState
from app.services.langgraph_nodes import (
    analyze_question,
    retrieve_docs,
    retrieve_order_info,
    generate_answer,
    run_cart_tool,
    run_delivery_tool,
    run_delivery_tool_rag,
    run_refund_tool,
    run_recommendation_tool,
    run_product_inquiry_tool,
    run_user_info_tool,
    run_payment_tool,
    run_event_tool,
    run_fallback_tool,
)




class GraphService:
    """
    LangGraph 기반 대화 처리 서비스
    """
    def __init__(self):
        self.graph = self._build_graph()

    def _build_graph(self):
        builder = StateGraph(ChatState)

        # 노드 등록
        builder.add_node("analyze_question", analyze_question)
        builder.add_node("retrieve_docs", retrieve_docs)
        builder.add_node("retrieve_order_info", retrieve_order_info)
        builder.add_node("generate_answer", generate_answer)
        builder.add_node("cart_node", run_cart_tool)
        builder.add_node("delivery_node", run_delivery_tool)
        builder.add_node("delivery_tool_rag", run_delivery_tool_rag)
        builder.add_node("refund_node", run_refund_tool)
        builder.add_node("recommendation_node", run_recommendation_tool)
        builder.add_node("product_inquiry_node", run_product_inquiry_tool)
        builder.add_node("fallback_node", run_fallback_tool)
        builder.add_node("event_node", run_event_tool)
        builder.add_node("user_info_node", run_user_info_tool)
        builder.add_node("payment_node", run_payment_tool)

        # 시작 노드 설정
        builder.set_entry_point("analyze_question")

        # 분기 처리
        builder.add_conditional_edges(
            "analyze_question",
            lambda state: state.intent,
            {
                # 주문/배송 관련
                "주문조회": "retrieve_order_info",
                "배송조회": "retrieve_order_info",
                "배송": "delivery_tool_rag",
                "주문취소": "refund_node",
                "환불": "refund_node",
                "교환": "refund_node",
                "반품": "refund_node",
                "결제": "payment_node",
                "포인트": "user_info_node",
                "계정": "user_info_node",
                "거래내역": "retrieve_order_info",
                "주문상세": "retrieve_order_info",
                "보증": "retrieve_docs",
                "영수증/세금계산서": "payment_node",
                "재입고문의": "retrieve_docs",
                "설치/사용법": "retrieve_docs",
                "업데이트/펌웨어": "retrieve_docs",

                # 제품 관련
                "제품비교": "retrieve_docs",
                "제품추천": "recommendation_node",
                "고객센터": "retrieve_docs",

                # 기타
                "장바구니": "cart_node",
                "이벤트": "event_node",

                # 대화 응답
                "잡담": "generate_answer",
                "비관련": "generate_answer",
                "unknown": "generate_answer",
            }
        )

        # 노드 흐름 연결
        builder.add_edge("retrieve_order_info", "retrieve_docs")
        builder.add_edge("retrieve_docs", "generate_answer")
        builder.add_edge("cart_node", "generate_answer")
        builder.add_edge("delivery_node", "generate_answer")
        builder.add_edge("refund_node", "generate_answer")
        builder.add_edge("product_inquiry_node", "generate_answer")
        builder.add_edge("user_info_node", "generate_answer")
        builder.add_edge("payment_node", "generate_answer")
        builder.add_edge("event_node", "generate_answer")
        builder.add_edge("recommendation_node", END)
        builder.add_edge("generate_answer", END)
        builder.add_edge("fallback_node", END)

        return builder.compile()

    async def run(self, state: ChatState) -> ChatState:
        result = await self.graph.ainvoke(state)
        return ChatState(**result)
