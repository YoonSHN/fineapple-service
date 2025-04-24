from fastapi import APIRouter, HTTPException
from app.models.chat import ChatRequest
from app.controllers.chat_controller import ChatController
from app.services.LLMService import LLMService
from app.presenters.chat_presenter import ChatPresenter
from fastapi import Request
from app.services.intro_recommendation_service import IntroRecommendationService


router = APIRouter()
controller = ChatController(llm_service=LLMService(),presenter= ChatPresenter())

@router.post("/chat")
async def chat_bot(body: ChatRequest, request: Request):
    user_id = request.session.get("user_id")
    if not user_id:
        raise HTTPException(status_code=401, detail="로그인이 필요합니다.")
    chat_request = ChatRequest(query=body.query, user_id=user_id)
    return await controller.process_chat(chat_request)

@router.get("/chat/intro")
async def recommend_chat_intro(request: Request):
    try:
        user_id = request.session.get("user_id")
        if user_id is None:
            raise ValueError("user_id is None")
    except Exception as e:

        raise HTTPException(status_code=401, detail="로그인이 필요합니다")

    service = IntroRecommendationService()
    message = await service.generate_intro_message(user_id)

    return {"message": message}