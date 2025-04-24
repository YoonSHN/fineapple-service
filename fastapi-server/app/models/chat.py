from pydantic import BaseModel
from typing import Optional, List, Dict

class ChatState(BaseModel):
    question: str
    context: str = ""
    answer: str = ""
    follow_up: bool = False
    intent: str = ""
    user_id: Optional[int] = None

    # 선택 정보 (주문 관련)
    order_info: Optional[List[Dict]] = None
    order_code: Optional[str] = None
    order_detail: Optional[Dict] = None

    # 히스토리/유형
    type: Optional[str] = None
    history: Optional[List[dict]] = []

    # 선택 정보 (상품 관련)
    products: Optional[List[Dict]] = None
    category_name: Optional[str] = None
    keyword: Optional[str] = None
    with_children: Optional[bool] = False

    # 선택 정보 (환불 관련)
    refund_reason: Optional[str] = None
    refund_amount: Optional[float] = None

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