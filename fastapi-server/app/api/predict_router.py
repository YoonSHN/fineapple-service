import pandas as pd
from fastapi import APIRouter
from pydantic import BaseModel
from typing import List, Union
from datetime import date

from sqlalchemy import text

from app.database.session import engine
from app.services.prediction_service import run_prediction


router = APIRouter()

class TimeSeriesDto(BaseModel):
    date: date
    groupKey: str
    value: float


def save_db(result_df: Union[pd.DataFrame, list[dict]], group_key: str):
    if isinstance(result_df, list):
        result_df = pd.DataFrame(result_df)
    with engine.begin() as conn:
        for _, row in result_df.iterrows():
            conn.execute(text("""
                INSERT INTO predicted_values (date, group_key, value)
                VALUES (:date, :group_key, :value)
            """), {
                "date": row["date"],
                "group_key": group_key,
                "value": int(row["value"])
            })

@router.post("/predict")
def predict(data: List[TimeSeriesDto]):
    result = run_prediction(data, freq="ME", periods=6, group_key="monthly")
    save_db(result, group_key="monthly")
    return result

@router.post("/predict/daily")
def predict_daily(data: List[TimeSeriesDto]):
    result = run_prediction(data, freq="D", periods=7, group_key="daily")
    save_db(result, group_key="daily")
    return result