from app.database.db import database

# 주문 목록 조회
ORDER_LIST_QUERY = """
SELECT
    o.order_id,
    o.order_code,
    o.created_at,
    o.total_price,
    o.discount_price,
    o.final_price,
    o.order_status,
    status_code.description AS order_status_name,
    o.payment_method,
    payment_code.description AS payment_method_name
FROM Orders o
LEFT JOIN CommonCode status_code ON o.order_status = status_code.code
LEFT JOIN CommonCode payment_code ON o.payment_method = payment_code.code
WHERE o.user_id = :user_id
ORDER BY o.created_at DESC
LIMIT 10;
"""

async def get_orders(user_id: int) -> list[dict]:
    rows = await database.fetch_all(query=ORDER_LIST_QUERY, values={"user_id": user_id})
    return [dict(row) for row in rows]

def format_orders_for_llm(order_rows: list[dict]) -> str:
    if not order_rows:
        return "최근 주문 내역이 없습니다."

    lines = [f"총 {len(order_rows)}건의 주문이 있습니다:\n"]

    for i, order in enumerate(order_rows, start=1):
        order_code = order.get("order_code", "주문번호 없음")
        date = order.get("created_at", "").strftime("%Y-%m-%d")
        total = f"{int(order.get('total_price', 0)):,}원"
        discount = order.get("discount_price")
        final = f"{int(order.get('final_price', 0)):,}원"
        payment = order.get("payment_method_name", "결제 수단 없음")
        status = order.get("order_status_name", "주문 상태 없음")

        discount_info = f", 할인 {int(discount):,}원" if discount else ""

        lines.append(
            f"{i}. 주문번호 : {order_code} \n"
            f"   총액 {total}{discount_info} → 결제 금액 {final}\n"
            f"   결제 방식: {payment} / 상태: {status}\n"
        )

    return "\n".join(lines)


# 주문 상품 상세 조회
# order_code로 order_id 찾기
GET_ORDER_ID_BY_CODE_QUERY = """
SELECT order_id FROM Orders
WHERE order_code = :order_code AND user_id = :user_id
LIMIT 1;
"""
async def get_order_id_by_code(order_code: str, user_id: int) -> int | None:
    row = await database.fetch_one(
        query=GET_ORDER_ID_BY_CODE_QUERY,
        values={"order_code": order_code, "user_id": user_id}
    )
    return row["order_id"] if row else None

# order_id가 user_id의 소유인지 확인
CHECK_ORDER_OWNERSHIP_QUERY = """
SELECT order_id FROM Orders
WHERE order_id = :order_id AND user_id = :user_id
LIMIT 1;
"""
async def is_user_order(order_id: int, user_id: int) -> bool:
    row = await database.fetch_one(query=CHECK_ORDER_OWNERSHIP_QUERY, values={
        "order_id": order_id,
        "user_id": user_id
    })
    return row is not None
