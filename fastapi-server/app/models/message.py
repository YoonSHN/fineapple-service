from pydantic import BaseModel
from typing import Literal, Optional


class Message(BaseModel):
    user_id: int
    role: Literal["user", "assistant"]
    content: str
    intent: Optional[str] = None