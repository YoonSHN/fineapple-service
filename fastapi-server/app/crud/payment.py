from app.database.db import database

GET_RECENT_PAYMENTS_BY_USER_QUERY = """
SELECT
    p.payment_id,
    p.total_amount,
    p.payment_status,
    p.requested_at,
    pd.product_name,
    pd.quantity
FROM Payment p
JOIN Orders o ON p.order_id = o.order_id
JOIN PaymentDetail pd ON p.payment_id = pd.payment_id
WHERE o.user_id = :user_id
ORDER BY p.requested_at DESC
LIMIT 5;
"""

async def get_recent_payments_by_user_id(user_id: int) -> list[dict]:
    rows = await database.fetch_all(
        query=GET_RECENT_PAYMENTS_BY_USER_QUERY,
        values={"user_id": user_id}
    )
    return [dict(row) for row in rows]

def format_payments_for_llm(payments: list[dict]) -> str:
    if not payments:
        return "최근 결제 내역이 없습니다."

    lines = ["📄 최근 결제 내역입니다:"]

    for i, p in enumerate(payments, 1):
        requested_time = p['requested_at'].strftime("%Y-%m-%d %H:%M")
        lines.append(
            f"{i}. 결제 ID: {p['payment_id']}\n"
            f"   - 상품: {p['product_name']} ({p['quantity']}개)\n"
            f"   - 금액: {p['total_amount']}원\n"
            f"   - 상태: {p['payment_status']}\n"
            f"   - 결제일시: {requested_time}"
        )
    return "\n".join(lines)