import logging
from langchain_upstage import ChatUpstage
from dotenv import load_dotenv

load_dotenv()
logger = logging.getLogger(__name__)

class LLMIntroService:
    def __init__(self):
        self.llm = ChatUpstage(
            model="solar-pro"
        )

    async def call_llm(self, prompt: str) -> str:
        try:
            response = await self.llm.ainvoke(prompt)
            return response.content.strip()
        except Exception as e:
            logger.error(f"[LLMIntro] 추천 메시지 생성 실패: {e}")
            return "추천 메시지를 가져오는 데 실패했습니다."