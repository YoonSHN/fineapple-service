from app.database.db import database

# 배송 현황 조회 쿼리
GET_SHIPMENT_STATUS_QUERY = """
SELECT
    o.order_code,
    s.tracking_url,
    cc.description AS delivery_status_name,
    s.post_num,
    s.address,
    s.city,
    s.region,
    s.road_num,
    s.store_name
FROM Shipment s
JOIN Orders o ON s.order_id = o.order_id
LEFT JOIN CommonCode cc ON s.delivery_status = cc.code
WHERE o.user_id = :user_id
ORDER BY s.updated_at DESC;
"""

async def get_shipment_status(user_id: int) -> list[dict]:
    rows = await database.fetch_all(
        query=GET_SHIPMENT_STATUS_QUERY,
        values={"user_id": user_id}
    )
    return [dict(row) for row in rows]


def format_shipments_for_llm(shipments: list[dict]) -> str:
    if not shipments:
        return "현재 배송 중인 주문이 없습니다."

    lines = [f"총 {len(shipments)}건의 배송 내역이 있습니다:\n"]
    for i, s in enumerate(shipments, 1):
        lines.append(
            f"{i}. 주문번호: {s['order_code']}\n"
            f"   배송 상태: {s.get('delivery_status_name', '확인 불가')}\n"
            f"   수령지: {s['city']} {s['region']} {s['road_num']} {s['address']} ({s['post_num']})\n"
            f"   배송조회 링크: {s['tracking_url']}\n"
        )
    return "\n".join(lines)
