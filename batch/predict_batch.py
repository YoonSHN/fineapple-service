import requests
import pandas as pd
from sqlalchemy import create_engine, text
import logging
from datetime import datetime, date
import os

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("batch")

FASTAPI_URL = "http://fastapi:8000"
DB_HOST = os.getenv("DB_HOST")
DB_USER = os.getenv("DB_USER")
DB_PASSWORD = os.getenv("DB_PASSWORD")
DB_NAME = os.getenv("DB_NAME")
DB_URL = f"mysql+pymysql://{DB_USER}:{DB_PASSWORD}@{DB_HOST}:3306/{DB_NAME}"


engine = create_engine(DB_URL)

def get_monthly_data():

    query = text("""SELECT DATE_FORMAT(o.created_at, '%Y-%m-01') AS date, 'ALL' AS groupKey, SUM(oid.quantity * oid.price) AS value
        FROM Orders o JOIN OrderItemDetail oid ON o.order_id = oid.order_id
        WHERE o.is_cancelled = FALSE
        GROUP BY DATE_FORMAT(o.created_at, '%Y-%m-01')""")


    df = pd.read_sql(query, engine)

    return df.to_dict(orient="records")

def get_daily_data():

    query = text("""SELECT DATE(o.created_at) AS date, 'ALL' AS groupKey, SUM(oid.quantity * oid.price) AS value
        FROM Orders o JOIN OrderItemDetail oid ON o.order_id = oid.order_id
        WHERE o.is_cancelled = FALSE
        GROUP BY DATE(o.created_at)""")

    df = pd.read_sql(query, engine)

    return df.to_dict(orient="records")

def post_api(point: str, load: list):
    try:
        for item in load:
                    if isinstance(item.get("date"), (datetime, date)):
                        item["date"] = item["date"].isoformat()

        url = f"{FASTAPI_URL}/api{point}"
        res = requests.post(url, json=load)
        res.raise_for_status()
        logger.info(f" {point} 예측 성공: {res.json()}")
    except Exception as e:
        logger.error(f" {point} 예측 실패: {str(e)}")

if __name__ == "__main__":
    logger.info("배치 시작함_________ ")

    monthly_data = get_monthly_data()
    post_api("/predict", monthly_data)

    daily_data = get_daily_data()
    post_api("/predict/daily", daily_data)

    logger.info("배치 끝~________")



