from langgraph.graph import StateGraph, END
from app.models.chat import ChatState
from app.services.langgraph_nodes import (
    analyze_question,
    retrieve_docs,
    retrieve_order_info,
    generate_answer,
)

class GraphService:
    """
    LangGraph 서비스
    """
    def __init__(self):
        self.graph = self._build_graph()


    def _build_graph(self):
        builder = StateGraph(ChatState)


        builder.add_node("analyze_question", analyze_question)
        builder.add_node("retrieve_docs", retrieve_docs)
        builder.add_node("retrieve_order_info", retrieve_order_info)
        builder.add_node("generate_answer", generate_answer)


        builder.set_entry_point("analyze_question")


        builder.add_conditional_edges(
            "analyze_question",
            lambda state: state.intent,
            {
                # 주문/배송 관련
                "주문조회": "retrieve_order_info",
                "배송조회": "retrieve_order_info",
                "배송": "retrieve_order_info",
                "주문취소": "retrieve_order_info",
                "환불": "retrieve_order_info",
                "교환": "retrieve_order_info",
                "반품": "retrieve_order_info",
                "결제": "retrieve_order_info",
                "포인트": "retrieve_order_info",
                "계정": "retrieve_order_info",
                "거래내역": "retrieve_order_info",
                "주문상세": "retrieve_order_info",

                # 제품 관련
                "제품비교": "retrieve_docs",
                "제품추천": "retrieve_docs",
                "고객센터": "retrieve_docs",

                # 대화 응답
                "잡담": "generate_answer",
                "비관련": "generate_answer",
                "unknown": "generate_answer"
            }
        )

        builder.add_edge("retrieve_order_info", "retrieve_docs")
        builder.add_edge("retrieve_docs", "generate_answer")
        builder.add_edge("generate_answer", END)

        return builder.compile()

    async def run(self, state: ChatState) -> ChatState:
        result = await self.graph.ainvoke(state)
        return ChatState(**result)
