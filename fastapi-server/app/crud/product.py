from app.database.db import database

# 카테고리나 키워드로 필터
PRODUCT_LIST_QUERY = """
SELECT
    p.product_id,
    p.name,
    p.price,
    p.description,
    pi.image_url
FROM Product p
LEFT JOIN Product_Image pi ON p.product_id = pi.product_id AND pi.product_main = TRUE
WHERE p.is_active = TRUE
  AND p.sale_status = 'PR0104'
  AND (:category_id IS NULL OR p.category_id = :category_id)
  AND (:keyword IS NULL OR p.name LIKE CONCAT('%', :keyword, '%'))
ORDER BY p.created_at DESC
LIMIT 20;
"""
async def get_products(category_id: int = None, keyword: str = None):
    values = {"category_id": category_id, "keyword": keyword}
    rows = await database.fetch_all(query=PRODUCT_LIST_QUERY, values=values)
    return [dict(row) for row in rows]


# 모든 상품 조회
PRODUCT_LIST_BY_IDS_QUERY = """
SELECT
    p.product_id,
    p.name,
    p.price,
    p.description,
    pi.image_url
FROM Product p
LEFT JOIN Product_Image pi ON p.product_id = pi.product_id AND pi.product_main = TRUE
WHERE p.is_active = TRUE
  AND p.sale_status = 'PR0104'
  AND p.category_id IN :category_ids
ORDER BY p.created_at DESC
LIMIT 20;
"""

async def get_products_by_category_ids(category_ids: list[int]):
    rows = await database.fetch_all(
        query=PRODUCT_LIST_BY_IDS_QUERY,
        values={"category_ids": tuple(category_ids)}
    )
    return [dict(row) for row in rows]

def format_products_for_llm(products: list[dict]) -> str:
    if not products:
        return "해당 조건에 맞는 상품이 없습니다."

    lines = [f"총 {len(products)}개의 상품을 찾았어요:\n"]

    for i, p in enumerate(products, start=1):
        name = p.get("name", "상품명 없음")
        price = f"{int(p['price']):,}원" if p.get("price") is not None else "가격 정보 없음"
        desc = p.get("description") or "설명 없음"
        lines.append(f"{i}. {name} - {price}\n   {desc}")

    return "\n".join(lines)


# 상품명으로 product_id 조회
GET_PRODUCT_ID_BY_NAME = """
SELECT product_id FROM Product
WHERE name LIKE CONCAT('%', :name, '%')
ORDER BY created_at DESC
LIMIT 1;
"""

async def get_product_id_by_name(name: str) -> int | None:
    row = await database.fetch_one(GET_PRODUCT_ID_BY_NAME, {"name": name})
    return row["product_id"] if row else None

