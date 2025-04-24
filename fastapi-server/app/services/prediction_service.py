from prophet import Prophet
import pandas as pd

def run_prediction(data, freq, periods, group_key):
    df = pd.DataFrame([{"ds": d.date, "y": float(d.value)} for d in data])
    df.dropna(inplace=True)
    if len(df) < 2:
        raise ValueError("입력 데이터가 부족합니다. 최소 2개 이상의 유효한 데이터가 필요합니다.")

    df = df.sort_values("ds")
    model = Prophet()
    model.fit(df)

    future = model.make_future_dataframe(periods=periods, freq=freq)
    forecast = model.predict(future)

    result = forecast[["ds", "yhat"]].tail(periods)
    result = result.rename(columns={'ds': 'date', 'yhat': 'value'})
    result['date'] = result['date'].dt.strftime('%Y-%m-%d')
    result['value'] = result['value'].fillna(0)

    if freq == 'D':
        result['value'] = result['value'].astype(int)

    return result.to_dict(orient="records")