from app.database.db import database

GET_CATEGORY_ID_QUERY = """
SELECT category_id
FROM Category
WHERE name LIKE CONCAT('%', :name, '%')
ORDER BY parent_id IS NULL DESC, created_at ASC
LIMIT 1
"""

GET_CATEGORY_AND_CHILDREN_IDS_QUERY = """
SELECT category_id FROM Category
WHERE category_id = :id OR parent_id = :id
"""

async def get_category_id_by_name(name: str) -> int | None:
    row = await database.fetch_one(GET_CATEGORY_ID_QUERY, {"name": name})
    return row["category_id"] if row else None

async def get_category_and_children_ids(category_id: int) -> list[int]:
    rows = await database.fetch_all(GET_CATEGORY_AND_CHILDREN_IDS_QUERY, {"id": category_id})
    return [row["category_id"] for row in rows]
