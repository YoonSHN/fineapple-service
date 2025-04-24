from typing import Optional
import random
from langchain_upstage import ChatUpstage
from app.utills.product_cache import PRODUCT_CACHE
from app.models.chat import ChatState
import logging
from dotenv import load_dotenv
import re
from typing import List, Dict
from langchain.callbacks.tracers import LangChainTracer

load_dotenv()

logger = logging.getLogger(__name__)

tracer = LangChainTracer()


class LLMService:
    def __init__(self):
        self.llm = ChatUpstage(
            model="solar-pro",
            callbacks=[tracer]
        )

    async def classify_intent(self, question: str, previous_question: Optional[str] = None) -> str:
        """
        현재 질문과 이전 질문(있다면)을 기반으로 의도를 분류합니다.
        """
        prompt = f"""
        [현재 질문]
        {question}
        """

        if previous_question:
            prompt += f"\n\n[이전 질문 또는 대화 문맥]\n{previous_question}"

        prompt += """
아래는 IT 쇼핑몰 고객센터에서 분류하는 질문 의도 목록입니다:

- 주문조회: 내가 주문한 건수, 주문 상태 확인, 주문 번호 확인 등
- 배송조회: 배송 상태, 배송 위치, 배송 추적
- 배송: 일반 배송 문의, 배송비, 택배사 등
- 주문취소: 주문 취소, 변경 요청
- 환불: 환불 요청, 환불 절차, 환불 지연 등
- 교환: 교환 요청, 교환 절차, 제품 불량 등
- 반품: 반품 요청, 수거, 회수 등
- 결제: 결제 수단, 결제 오류, 영수증, 세금계산서
- 포인트: 적립금, 포인트 사용, 멤버십 혜택
- 계정: 로그인, 회원가입, 비밀번호, 탈퇴
- 비관련: 쇼핑몰과 무관한 정보 요청 또는 주제 (예: 여행지 추천, 영화 이야기, 날씨, 게임, 연애상담, 광고, 스팸 등)
- 제품추천: 고객이 IT 쇼핑몰에서 구매 가능한 제품 (예: 노트북, 이어폰, 스마트폰 등)을 추천해달라고 요청하는 경우
- 제품비교: 이미 특정 제품들을 언급하며, 두 개 이상을 비교해달라는 경우
- 거래내역: 결제 기록, 과거 주문 내역
- 장바구니: 장바구니에 담긴 상품 목록, 수량 확인, 삭제 요청 등
- 이벤트: 진행 중인 프로모션, 할인 행사, 이벤트 문의
- 주문상세: 특정 주문에 포함된 상품 목록, 수량, 배송지 주소 등
- 고객센터: 고객센터 전화번호, 상담 가능 시간, 상담사 연결 등
- 잡담: 인사, 감사, 간단한 감정 표현 (예: "안녕하세요", "고마워요", "좋은 하루 되세요")
- 보증: 제품 보증 기간, A/S, 품질보증 관련 문의
- 영수증/세금계산서: 주문 건에 대한 영수증 발급, 세금계산서 요청, 지출 증빙용 자료 문의
- 재입고문의: 품절된 제품의 재입고 여부 및 시점, 알림 요청 등에 대한 문의
- 설치/사용법: 제품 설치, 초기 설정, 앱 연동, 사용 설명서에 대한 문의
- 업데이트/펌웨어: 제품 소프트웨어 또는 펌웨어 업데이트 방법, 주기, 버전 문의

---

**지금부터 할 일:**
- 사용자의 질문을 보고 위 목록에서 가장 적절한 **의도명(항목명)**을 하나만 선택하세요.

**다음 지침을 반드시 따르세요:**
1. 출력은 반드시 **위 목록 중 괄호 앞에 있는 항목 이름(예: 주문조회)** 중 하나로만 하세요.
2. 설명, 이유, 예시는 쓰지 마세요. **의도명 하나만 출력**하세요.
3. 절대로 사용자의 질문 문장을 그대로 따라 적지 마세요. 예: "주문번호알려줘" →  틀림
4. 목록에 없거나 불명확한 경우에는 **반드시 `비관련` 또는 `unknown`**으로 출력하세요.
5. 형식 예시:
        """

        known_intents = {
            "주문조회", "배송조회", "배송", "주문취소", "환불", "교환", "반품", "보증",
            "결제", "포인트", "계정", "거래내역", "주문상세", "제품추천", "고객센터", "영수증/세금계산서",
            "제품비교", "잡담", "비관련", "장바구니", "이벤트", "업데이트/펌웨어", "재입고문의", "unknown"
        }

        try:
            response = await self.llm.ainvoke(prompt)
            intent = response.content.strip()

            if intent not in known_intents:
                logger.warning(f"[classify_intent] 알 수 없는 intent '{intent}' → 'unknown'으로 대체")
                intent = "unknown"

            return intent
        except Exception as e:
            logger.warning("의도 분류 실패")
            return "unknown"

    async def generate_response(self, state: ChatState) -> str:
        """
        질문 + 문맥 + 문서 + 주문 정보 기반으로 적절하고 간결한 응답 생성
        """
        try:
            # 1. 짧거나 무의미한 입력 필터링
            if len(state.question.strip()) < 3:
                return "안녕하세요! 무엇을 도와드릴까요?"

            if state.intent == "잡담":
                responses = [
                    "저희는 FineApple 제품 관련 고객센터이지만, 간단한 대화는 언제든 환영입니다 :)",
                    "상담 외에도 이렇게 인사해주셔서 감사합니다!",
                    "도움이 필요하시면 언제든 말씀해주세요. 대화도 환영이에요!"
                ]
                return random.choice(responses)

            if state.intent in ("비관련", "unknown"):
                return "죄송하지만, 해당 내용은 저희 고객센터의 상담 범위를 벗어난 질문입니다."

            if state.intent in ("주문조회", "주문상세") and isinstance(state.order_info, list) and len(state.order_info) == 0:
                logger.info("[generate_answer] 주문이 0건입니다. 고정 응답 처리")
                return "현재 고객님의 주문 내역이 없습니다. 새로운 주문이 등록되면 안내해드릴게요."

            messages = []

            # 2. 시스템 프롬프트
            messages.append({
                "role": "system",
                "content": (
                    "당신은 FineApple브랜드 IT 쇼핑몰 고객센터 상담원입니다. "
                    "사용자의 질문에 대해 **관련된 내용만 간결하고 친절하게** 답변하세요. "
                    "불필요한 설명이나 여러 주제 나열은 삼가세요. "
                    "Apple, Dell, HP, ASUS, 삼성 등 외부 브랜드 제품은 절대 언급하지 마세요. "
                    "FineApple 제품 중에서만 추천하고 안내하세요."
                )
            })

            # 3. 이전 대화 이력 포함
            if state.history:
                messages.extend(state.history)

            # 4. 사용자 질문 + 문맥 + 주문 정보 구성
            user_prompt = state.question

            if state.context and "관련 문서를 찾을 수 없습니다" not in state.context:
                user_prompt += f"\n\n[문서 정보]\n{state.context}"

            if state.order_info:
                user_prompt += f"\n\n[주문 정보]\n{state.order_info}"

            if state.order_detail:
                user_prompt += f"\n\n[주문 상세 정보]\n{state.order_detail}"

            messages.append({"role": "user", "content": user_prompt})

            response = await self.llm.ainvoke(messages)
            return response.content.strip()

        except Exception as e:
            logger.exception("답변 생성 실패")
            return "죄송합니다. 답변을 생성하지 못했습니다."
