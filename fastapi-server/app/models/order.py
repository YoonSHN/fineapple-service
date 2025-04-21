from pydantic import BaseModel
from typing import Optional
from datetime import datetime

class OrderInfo(BaseModel):
    order_id: int
    order_code: str
    total_price: float
    status: str
    created_at: datetime
    delivery_status: Optional[str] = None
    tracking_number: Optional[str] = None

class Order:
    def __init__(self, order_id: str, user_id: int, status: str, items: list):
        self.order_id = order_id
        self.user_id = user_id
        self.status = status
        self.items = items

    def to_dict(self):
        return {
            "order_id": self.order_id,
            "user_id": self.user_id,
            "status": self.status,
            "items": self.items
        } 