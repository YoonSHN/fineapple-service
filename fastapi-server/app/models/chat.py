from pydantic import BaseModel
from typing import Optional, List, Dict

class ChatState(BaseModel):
    question: str
    context: str = ""
    answer: str = ""
    follow_up: bool = False
    intent: str = ""
    user_id: Optional[int] = None
    order_info: Optional[List[Dict]] = None
    order_code: Optional[str] = None
    order_detail: Optional[Dict] = None
    type: Optional[str] = None
    history: Optional[List[dict]] = []
    products: Optional[List[Dict]] = None

class ChatRequest(BaseModel):
    query: str
    user_id: Optional[int] = None


class ChatResponse(BaseModel):
    content: str
    intent: str
    order_info: Optional[List[Dict]] = None
    success: bool = True
    type: Optional[str] = None
    products: Optional[List[Dict]] = None