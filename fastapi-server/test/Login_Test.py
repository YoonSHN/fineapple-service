from app.services.auth_service import AuthService
from app.database.session import SessionLocal
from app.models.user import UserLogin
import asyncio


async def test_login():
    db = SessionLocal()
    auth_service = AuthService(db)

    login_input = UserLogin(email="1234@co.kr", password="123")
    user = await auth_service.authenticate_user(
        email=login_input.email,
        password=login_input.password
    )

    if user:
        print("로그인 성공:", user)
    else:
        print("로그인 실패")


asyncio.run(test_login())
