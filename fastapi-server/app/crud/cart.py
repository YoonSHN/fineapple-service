from typing import List
from app.database.db import database

async def get_cart_items_by_user_id(user_id: int) -> List[dict]:
    # 1. 최신 cart_id 조회
    cart_id_query = """
        SELECT cart_id 
        FROM Cart 
        WHERE user_id = :user_id 
        ORDER BY created_at DESC 
        LIMIT 1
    """

    cart_row = await database.fetch_one(cart_id_query, values={"user_id": user_id})
    if not cart_row:
        return []

    cart_id = cart_row["cart_id"]

    # 2. 해당 cart_id 기준으로 상품 목록 조회
    item_query = """
        SELECT 
            ci.cart_item_id,
            p.product_id,
            p.name AS product_name,
            p.price,
            ci.quantity
        FROM CartItem ci
        JOIN Product p ON ci.product_id = p.product_id
        WHERE ci.cart_id = :cart_id
    """
    return await database.fetch_all(query=item_query, values={"cart_id": cart_id})