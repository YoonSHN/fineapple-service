import httpx
from mysql.connector import pooling
import pandas as pd
from app.database import config
from elasticsearch import Elasticsearch

es = Elasticsearch(
    [config.ES_HOST]
)


dbconfig = {
    "host": config.DB_HOST,
    "user": config.DB_USER,
    "password":config.DB_PASSWORD,
    "database":config.DB_NAME
}
# 커넥션 풀 생성 size만큼 접속 가능
connection_pool = pooling.MySQLConnectionPool(
    pool_name="click_pool",
    pool_size=5,
    **dbconfig
)

# 클릭로그 커넥션 풀에서 받아오기
# def get_click_log():
#     conn = connection_pool.get_connection()
#
#     try:
#         query = "SELECT user_id, session_id, product_id FROM ClickLog"
#         click_log_df = pd.read_sql(query, conn)
#     finally:
#         conn.close()
#     return click_log_df

# 카테고리 데이터 받아오기
def get_category_data():
    conn = connection_pool.get_connection()
    try:
        query = "SELECT category_id, parent_id , name FROM Category"
        category_data_df = pd.read_sql(query, conn)
    finally:
        conn.close()
    return category_data_df

# 벡터화용 상품테이블 정보 가져오기
def get_product_data():
    conn = connection_pool.get_connection()
    try:
        query = "SELECT product_id, name, category_id, price FROM Product"
        product_data_df = pd.read_sql(query, conn)
    finally:
        conn.close()
    return product_data_df

#유저의 최근 본 상품 정보
# def get_recent_view_product(user_id):
#     conn = connection_pool.get_connection()
#     try:
#         query = """
#         select product_id, clicked_at from ClickLog
#         where user_id = %s
#         order by clicked_at desc
#         limit 1;
#         """
#         recent_view_df = pd.read_sql(query, conn, params=(user_id,))
#     finally:
#         conn.close()
#     return recent_view_df
async def get_recent_view_product_from_els(user_id, index="product-access-*"):
    try:
        query = {
            "query": {
                "bool": {
                    "must": [
                        {"term": {"userId.keyword": str(user_id)}},
                        {"term": {"eventType.keyword": "product_access"}}
                    ]
                }
            },
            "sort": [{"@timestamp": {"order": "desc"}}],
            "size": 1
        }

        async with httpx.AsyncClient() as client:
            response = await client.post(
                f"{config.ES_HOST}/{index}/_search",
                json=query
            )
            response.raise_for_status()
            result = response.json()
            hits = result["hits"]["hits"]
            if not hits:
                return pd.DataFrame()

            doc = hits[0]["_source"]
            return pd.DataFrame([{
                "product_id": doc.get("productId"),
                "clicked_at": doc.get("@timestamp")
            }])
    except Exception as e:
        print(f"[ERROR] Elasticsearch 조회 실패: {e}")
        return pd.DataFrame()