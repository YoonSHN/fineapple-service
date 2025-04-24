# app/llm/nodes/cart.py

from app.crud.cart import get_cart_items_by_user_id
from app.models.chat import ChatState
from app.nodes.rag_generate import rag_generate_node
from app.nodes.rag_retrieve_node import rag_retrieve_node


async def cart_node(state: ChatState) -> ChatState:
    question = state.question.strip()
    user_id = state.user_id

    if not user_id:
        return state.copy(update={
            "answer": "로그인이 필요합니다. 먼저 로그인 해주세요.",
        })

    # RAG로 처리할 장바구니 정책 관련 키워드
    policy_keywords = ["유효 기간", "자동 삭제", "장바구니 정책", "장바구니 관련 질문", "담을 수 있는 상품 개수"]
    if any(kw in question for kw in policy_keywords):
        state = await rag_retrieve_node(state)  # 문서 검색
        return await rag_generate_node(state)  # LLM 응답 생성

    # DB에서 장바구니 조회
    cart_items = await get_cart_items_by_user_id(user_id)

    if not cart_items:
        response = "장바구니에 담긴 상품이 없습니다."
    else:
        item_list = "\n".join([
            f"- {item['product_name']} (수량: {item['quantity']}, 가격: {item['price']}원)"
            for item in cart_items
        ])
        response = f"현재 장바구니에 담긴 상품 목록입니다:\n{item_list}"

    return state.copy(update={
        "answer": response,
        "cart_items": cart_items
    })