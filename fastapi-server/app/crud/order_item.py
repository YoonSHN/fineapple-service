from app.database.db import database

ORDER_DETAIL_QUERY = """
SELECT
    o.orderitemdetail_id,
    o.name AS product_name,
    o.quantity,
    o.price,
    o.discount_price,
    o.additional_price,
    p.product_id,
    pi.image_url
FROM OrderItemDetail o
JOIN Product p ON o.product_id = p.product_id
LEFT JOIN Product_Image pi ON p.product_id = pi.product_id AND pi.product_main = TRUE
WHERE o.order_id = :order_id;
"""

async def get_order_items(order_id: int) -> list[dict]:
    rows = await database.fetch_all(query=ORDER_DETAIL_QUERY, values={"order_id": order_id})
    return [dict(row) for row in rows]


def format_order_items_for_llm(items: list[dict]) -> str:
    if not items:
        return "해당 주문에 포함된 상품이 없습니다."

    lines = [f"해당 주문에 포함된 상품은 총 {len(items)}개입니다:\n"]

    for i, item in enumerate(items, start=1):
        name = item.get("product_name")
        qty = item.get("quantity", 1)
        price = f"{int(item.get('price', 0)):,}원"
        discount = item.get("discount_price")
        additional = item.get("additional_price")
        discount_text = f", 할인 {int(discount):,}원" if discount else ""
        additional_text = f", 추가금액 {int(additional):,}원" if additional else ""

        lines.append(f"{i}. {name} - {qty}개 ({price}{discount_text}{additional_text})")

    return "\n".join(lines)
