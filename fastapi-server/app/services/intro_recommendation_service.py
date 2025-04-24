import logging
from app.services.LLMIntroService import LLMIntroService
from app.services.persnal_user_recommender import personal_recommend
from app.services.recommendation_prompt_formatter import format_recommendation_prompt

class IntroRecommendationService:
    def __init__(self):
        self.llm_intro = LLMIntroService()

    async def generate_intro_message(self, user_id: str) -> str:
        recommendation_data = await personal_recommend(user_id)
        if not recommendation_data:
            return "최근 본 상품이 없어 추천이 어렵습니다."

        product_id = recommendation_data["product_id"]
        recommended_items = recommendation_data["recommended_items"]
        mid_prompt = format_recommendation_prompt(product_id, recommended_items)

        prompt = f"""
당신은 IT 제품 전문 쇼핑몰의 AI 쇼핑 도우미입니다.
사용자가 최근 본 상품을 바탕으로 맞춤형 추천 메시지를 제공합니다.
추천 메시지는 부드럽고 고객 친화적인 자연어 문장으로 구성되어야 합니다.

아래는 추천 관련 데이터입니다:
{mid_prompt}

이 정보를 바탕으로 고객에게 전달할 추천 메시지를 자연스럽게 생성해 주세요.
문장은 2~4줄 이내로 작성하며, 너무 기계적이거나 리스트 형식이 아니어야 합니다.
"""
        return await self.llm_intro.call_llm(prompt)