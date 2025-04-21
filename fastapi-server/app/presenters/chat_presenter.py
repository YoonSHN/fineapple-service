from app.models.chat import ChatResponse, ChatState


class ChatPresenter:
    def format_response(self, state: dict) -> ChatResponse:
        response = ChatResponse(
            content=state["answer"],
            intent=state["intent"],
            order_info=state.get("order_info"),
            success=True,
            type=None,
            products=None
        )

        if state["intent"] in ("제품비교", "제품추천"):
            response.type = "comparison" if state["intent"] == "제품비교" else "recommendation"
            response.products = state.get("products", [])

        return response