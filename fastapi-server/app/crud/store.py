from app.database.db import database

GET_STORE_LIST_QUERY = """
SELECT
    store_id,
    name AS store_name,
    location,
    postal_code,
    store_number,
    opening_time,
    closing_time
FROM Store
WHERE store_status = 'ST0201'
  AND store_type = 'ST0101'
  AND (:region IS NULL OR location LIKE CONCAT('%', :region, '%'))
ORDER BY created_at DESC;
"""

async def get_store_list(region: str = None) -> list[dict]:
    rows = await database.fetch_all(
        query=GET_STORE_LIST_QUERY,
        values={"region": region}
    )
    return [dict(row) for row in rows]


def format_stores_for_llm(stores: list[dict]) -> str:
    if not stores:
        return "현재 운영 중인 오프라인 매장이 없습니다."

    lines = [f"총 {len(stores)}개의 오프라인 매장이 운영 중입니다:\n"]
    for store in stores:
        name = store["store_name"]
        location = store["location"]
        postal = store["postal_code"]
        tel = store["store_number"]
        open_time = str(store["opening_time"])[:5]
        close_time = str(store["closing_time"])[:5]

        lines.append(
            f"- {name} ({location}, {postal})\n"
            f"  운영 시간: {open_time} ~ {close_time}, 전화번호: {tel}"
        )

    return "\n".join(lines)