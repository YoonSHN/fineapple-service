from fastapi import APIRouter, Request, Depends, HTTPException
from pydantic import BaseModel, EmailStr
from sqlalchemy.orm import Session
from app.database.session import get_db
from app.services.auth_service import AuthService
from app.models.user import UserLogin


router = APIRouter()

@router.post("/login")
async def login(
    request: Request,
    login_data: UserLogin,
    db: Session = Depends(get_db)
):

    auth_service = AuthService(db)
    user = await auth_service.authenticate_user(login_data.email, login_data.password)

    if not user:
        raise HTTPException(status_code=401, detail="이메일 또는 비밀번호가 올바르지 않습니다.")

    request.session["user_id"] = user["id"]

    return {
        "message": "로그인 성공",
        "user_id": user["id"],
        "email": user["email"]
    }

@router.post("/logout")
def logout(request: Request):
    request.session.clear()
    return {"message": "로그아웃 완료"}