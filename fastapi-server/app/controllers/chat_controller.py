import logging
from typing import List
import re

from app.services.LLMService import LLMService
from app.presenters.chat_presenter import ChatPresenter
from app.models.chat import ChatRequest, ChatResponse, ChatState
from app.services.graph_service import GraphService
from app.services.message_service import save_message
from app.models.message import Message
from app.services.state_builder import prepare_state, reconstruct_previous_state
from app.utills.extract_products_from_context import extract_product_names_from_llm_answer, find_products_by_name
from app.utills.product_cache import PRODUCT_CACHE


class ChatController:
    def __init__(self, llm_service=None, presenter=None):
        self.llm_service = llm_service or LLMService()
        self.presenter = presenter or ChatPresenter()
        self.graph_service = GraphService()

    async def process_chat(self, request: ChatRequest) -> ChatResponse:
        # 1. 이전 대화 기록 기반 상태 복원
        previous_state = await reconstruct_previous_state(request.user_id)

        # 2. 상태 구성
        state = await prepare_state(request, previous_state)

        # 3. LangGraph 실행
        final_state = await self.graph_service.run(state)

        def extract_product_names_from_markdown(md_text: str) -> List[str]:
            return re.findall(r'##\s*\d+\.\s*(.+)', md_text)
        md_text = open("./data/docs/fineapple-products.md", encoding="utf-8").read()
        known_products = extract_product_names_from_markdown(md_text)

        if final_state.intent in ("제품추천", "제품비교"):
            names = extract_product_names_from_llm_answer(final_state.answer, known_products)
            if final_state.intent == "제품추천":
                names = names[:3]
            elif final_state.intent == "제품비교":
                names = names[:2]
            logging.info(f"[제품 이름 추출됨] → {names}")
            products = find_products_by_name(names, PRODUCT_CACHE)
            logging.info(PRODUCT_CACHE)
            logging.info(f"[매칭된 제품] → {products}")
            final_state.products = products
            final_state.type = "recommendation" if final_state.intent == "제품추천" else "comparison"

        # 4. 메시지 저장
        save_message(Message(
            user_id=request.user_id,
            role="user",
            content=request.query,
            intent=state.intent
        ))

        save_message(Message(
            user_id=request.user_id,
            role="assistant",
            content=final_state.answer,
            intent=state.intent
        ))

        # 5. 응답 반환
        return self.presenter.format_response(final_state.model_dump())
