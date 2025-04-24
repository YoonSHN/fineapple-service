from fastapi import FastAPI, Request, HTTPException
from fastapi.responses import JSONResponse
from starlette.middleware.sessions import SessionMiddleware
from fastapi.middleware.cors import CORSMiddleware

from app.api.predict_router import router as predict_router
from app.api.chat_router import router as chat_router
from app.api.auth_router import router as auth_router
from app.api.recommend_router import router as recommend_router
from dotenv import load_dotenv
import logging
from app.database.db import database


logger = logging.getLogger(__name__)
load_dotenv()

app = FastAPI(
    title="LLM 기반 챗봇 API",
    description="의도 분류 + RAG + Solar 기반 챗봇 API입니다.",
    version="1.0"
)

app.add_middleware(
    SessionMiddleware, #type: ignore
    secret_key="your-super-secret-key"
)

app.add_middleware(
    CORSMiddleware,  # type: ignore
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(chat_router, prefix="/api")
app.include_router(auth_router)
app.include_router(predict_router)
app.include_router(recommend_router)
# app.include_router(agent_router, prefix="/api") #미사용



@app.exception_handler(Exception)
async def global_exception_handler(request: Request, err: Exception):
    logger.exception(f"[예외 발생] 요청 경로: {request.url} | 에러: {err}")
    return JSONResponse(
        status_code=500,
        content={"detail": "서버 내부 오류가 발생했습니다. 잠시 후 다시 시도해주세요."},
    )

@app.on_event("startup")
async def startup():
    await database.connect()

@app.on_event("shutdown")
async def shutdown():
    await database.disconnect()


@app.get("/me")
def get_me(request: Request):
    user_id = request.session.get("user_id")
    if not user_id:
        raise HTTPException(status_code=401, detail="로그인이 필요합니다.")

    return {"user_id": user_id}
