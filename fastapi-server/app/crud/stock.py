from app.database.db import database

# 상품 재고 조회 -> 매장 정보
GET_PRODUCT_STOCK_QUERY = """
SELECT
    s.stock_id,
    st.name AS store_name,
    st.location AS store_location,
    p.name AS product_name,
    s.quantity
FROM Stock s
         JOIN Store st ON s.store_id = st.store_id
         JOIN Product p ON s.product_id = p.product_id
WHERE s.product_id = :product_id
AND st.store_status = 'ST0201'
ORDER BY s.updated_at DESC;
"""

async def get_product_stock(product_id: int):
    rows = await database.fetch_all(
        query=GET_PRODUCT_STOCK_QUERY,
        values={"product_id": product_id}
    )
    return [dict(row) for row in rows]

def format_stock_for_llm(stock_rows: list[dict]) -> str:
    if not stock_rows:
        return "현재 운영 중인 매장에서 해당 상품의 재고가 없습니다."

    product_name = stock_rows[0]["product_name"]
    lines = [f"'{product_name}'의 매장별 재고 정보입니다:\n"]

    for row in stock_rows:
        store = row["store_name"]
        qty = row["quantity"]
        lines.append(f"- {store}: {qty}개 보유 중")

    return "\n".join(lines)
