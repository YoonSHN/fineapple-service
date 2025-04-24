from app.database.db import database

# order_code로 payment_id 가져오기
GET_PAYMENT_ID_BY_ORDER_CODE_QUERY = """
SELECT p.payment_id
FROM Payment p
JOIN Orders o ON p.order_id = o.order_id
WHERE o.order_code = :order_code AND o.user_id = :user_id
LIMIT 1;
"""

async def get_payment_id_by_order_code(order_code: str, user_id: int) -> int | None:
    row = await database.fetch_one(
        query=GET_PAYMENT_ID_BY_ORDER_CODE_QUERY,
        values={"order_code": order_code, "user_id": user_id}
    )
    return row["payment_id"] if row else None

# 결제 금액 조회
GET_PAYMENT_AMOUNT_QUERY = """
SELECT total_amount
FROM Payment
WHERE payment_id = :payment_id
"""

async def get_payment_total_amount(payment_id: int) -> float | None:
    row = await database.fetch_one(query=GET_PAYMENT_AMOUNT_QUERY, values={"payment_id": payment_id})
    return float(row["total_amount"]) if row else None


# payment_id가 사용자(user_id)의 주문에서 발생한 건지 확인
async def is_user_payment(payment_id: int, user_id: int) -> bool:
    query = """
    SELECT p.payment_id
    FROM Payment p
    JOIN Orders o ON p.order_id = o.order_id
    WHERE p.payment_id = :payment_id AND o.user_id = :user_id
    """
    row = await database.fetch_one(query=query, values={
        "payment_id": payment_id,
        "user_id": user_id
    })
    return row is not None


# 환불 요청 (전체 환불 먼저 처리(부분 환불은 추후에))
REFUND_INSERT_QUERY = """
INSERT INTO Refund (
    refund_status,
    payment_id,
    refund_total_amount,
    refund_reason
) VALUES (
    :refund_status,
    :payment_id,
    :refund_total_amount,
    :refund_reason
);
"""
UPDATE_ORDER_STATUS_QUERY = """
UPDATE Orders
SET order_status = :order_status, updated_at = NOW()
WHERE order_id = (
    SELECT order_id FROM Payment WHERE payment_id = :payment_id
);
"""
async def request_refund(payment_id: int, refund_total_amount: float, refund_reason: str):
    await database.execute(query=REFUND_INSERT_QUERY, values={
        "refund_status": "OR0301",  # '환불 대기 상태' 상태
        "payment_id": payment_id,
        "refund_total_amount": refund_total_amount,
        "refund_reason": refund_reason
    })
    await database.execute(query=UPDATE_ORDER_STATUS_QUERY, values={
                "order_status": "OR0103",  # '주문 취소 요청' 상태
                "payment_id": payment_id
            })

# 환불 내역 조회
REFUND_HISTORY_QUERY = """
SELECT
    r.refund_id,
    r.refund_total_amount,
    r.refund_reason,
    r.requested_at,
    r.approved_at,
    r.refund_status,
    code.description AS refund_status_name,
    o.order_code
FROM Refund r
JOIN Payment p ON r.payment_id = p.payment_id
JOIN Orders o ON p.order_id = o.order_id
LEFT JOIN CommonCode code ON r.refund_status = code.code
WHERE o.user_id = :user_id
ORDER BY r.requested_at DESC;
"""

async def get_refund_history(user_id: int) -> list[dict]:
    rows = await database.fetch_all(query=REFUND_HISTORY_QUERY, values={"user_id": user_id})
    return [dict(row) for row in rows]

def format_refunds_for_llm(refunds: list[dict]) -> str:
    if not refunds:
        return "현재 환불 요청 내역이 없습니다."

    lines = [f"총 {len(refunds)}건의 환불 요청 내역이 있습니다:\n"]

    for r in refunds:
        code = r["order_code"]
        amount = f"{int(r['refund_total_amount']):,}원"
        reason = r.get("refund_reason", "사유 없음")
        status = r.get("refund_status_name", "상태 없음")
        requested = r["requested_at"].strftime("%Y-%m-%d")
        approved = r["approved_at"].strftime("%Y-%m-%d") if r["approved_at"] else "승인 대기 중"

        lines.append(
            f"- 주문번호 {code} | 환불 금액: {amount}\n"
            f"  사유: {reason} | 요청일: {requested} | 상태: {status} | 승인일: {approved}"
        )

    return "\n".join(lines)
