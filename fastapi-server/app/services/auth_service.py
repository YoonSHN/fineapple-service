import logging

from sqlalchemy.orm import Session
from sqlalchemy import text
from passlib.context import CryptContext
from typing import Optional
from pydantic import EmailStr
import logging

logger = logging.getLogger(__name__)

class AuthService:
    def __init__(self, db: Session):
        self.db = db
        self.pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

    async def authenticate_user(self, email: EmailStr, password: str) -> Optional[dict]:
        """
        이메일과 비밀번호로 유저를 인증합니다.
        """
        query = text("""
            SELECT user_id, email, password
            FROM User
            WHERE email = :email
            LIMIT 1
        """)
        result = self.db.execute(query, {"email": email})
        row = result.fetchone()
        if not row:
            logger.info("이메일이 존재하지 않음")
            return None

        if not self.verify_password(password, row.password):
            logger.info("비밀번호 불일치")
            return None

        return {
            "id": row.user_id,
            "email": row.email,
        }

    def verify_password(self, plain_password: str, hashed_password: str) -> bool:
        return self.pwd_context.verify(plain_password, hashed_password)
