
from sqlalchemy.orm import Session
from sqlalchemy import text
from typing import List, Optional



def get_user_orders(db: Session, user_id: int) -> List[dict]:
    """
    사용자의 주문 목록을 가져옵니다.
    """
    query = """
    SELECT 
        o.order_id,
        o.order_code,
        o.total_price,
        o.order_status AS status,
        o.created_at,
        s.delivery_status,
        s.tracking_number
    FROM Orders o
    LEFT JOIN Shipment s ON o.order_id = s.order_id
    WHERE o.user_id = :user_id
    ORDER BY o.created_at DESC
    """

    result = db.execute(text(query), {"user_id": user_id})
    orders = []
    for row in result:
        orders.append({
            "order_id": row.order_id,
            "order_code": row.order_code,
            "total_price": float(row.total_price),
            "status": row.status,
            "created_at": row.created_at.strftime("%Y-%m-%d %H:%M:%S"),
            "delivery_status": row.delivery_status,
            "tracking_number": row.tracking_number
        })
    return orders


def get_order_details(db: Session, order_id: int, user_id: int) -> Optional[dict]:
    """
    주문 상세 정보 조회 (주문 정보 + 배송 + 결제 + 주문상품 목록 포함)
    """
    # 주문, 배송, 결제 정보
    order_query = """
    SELECT 
        o.order_id,
        o.order_code,
        o.total_price,
        o.discount_price,
        o.order_status,
        o.payment_method,
        o.created_at,
        s.tracking_number,
        s.delivery_status,
        s.courier_company,
        s.estimated_delivery_date,
        s.address,
        a.name AS recipient_name,
        a.tel AS recipient_tel,
        a.post_num,
        a.road_num,
        a.city,
        a.region
    FROM Orders o
    LEFT JOIN Shipment s ON o.order_id = s.order_id
    LEFT JOIN Address a ON o.delivery_id = a.delivery_id
    WHERE o.order_id = :order_id AND o.user_id = :user_id  
    """

    order_result = db.execute(text(order_query), {"order_id": order_id, "user_id": user_id}).fetchone()
    if not order_result:
        return None

    # 상품 목록
    item_query = """
    SELECT 
        od.name AS product_name,
        od.quantity,
        od.price,
        od.discount_price,
        po.option_name,
        po.option_value,
        od.additional_price
    FROM OrderItemDetail od
    LEFT JOIN ProductOption po ON od.option_id = po.option_id
    WHERE od.order_id = :order_id
    """

    item_result = db.execute(text(item_query), {"order_id": order_id}).fetchall()
    items = []
    for row in item_result:
        items.append({
            "product_name": row.product_name,
            "quantity": row.quantity,
            "price": float(row.price),
            "discount_price": float(row.discount_price) if row.discount_price else 0,
            "option": {
                "name": row.option_name,
                "value": row.option_value
            },
            "additional_price": float(row.additional_price) if row.additional_price else 0
        })

    # 최종 반환
    return {
        "order_id": order_result.order_id,
        "order_code": order_result.order_code,
        "total_price": float(order_result.total_price),
        "discount_price": float(order_result.discount_price) if order_result.discount_price else 0,
        "status": order_result.order_status,
        "payment_method": order_result.payment_method,
        "created_at": order_result.created_at.strftime("%Y-%m-%d %H:%M:%S"),
        "shipment": {
            "tracking_number": order_result.tracking_number,
            "delivery_status": order_result.delivery_status,
            "courier_company": order_result.courier_company,
            "estimated_delivery_date": order_result.estimated_delivery_date.strftime("%Y-%m-%d"),
            "address": f"{order_result.post_num} {order_result.road_num} {order_result.city} {order_result.region}",
            "recipient": {
                "name": order_result.recipient_name,
                "tel": order_result.recipient_tel
            }
        },
        "items": items
    }


# class OrderService:
#     def __init__(self, db):
#         self.db = db
#
#     def get_user_orders(self, user_id: int) -> list:
#         from app.database.session import get_db
#         db = next(get_db())
#         orders = []
#
#         raw_orders = db.query(f"SELECT * FROM orders WHERE user_id = {user_id}")
#
#         for raw_order in raw_orders:
#             order = Order(
#                 order_id=raw_order['order_id'],
#                 user_id=raw_order['user_id'],
#                 status=raw_order['status'],
#                 items=raw_order['items']
#             )
#             orders.append(order)
#
#         return orders
