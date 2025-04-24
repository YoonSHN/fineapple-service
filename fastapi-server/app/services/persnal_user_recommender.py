from app.database.connection import get_recent_view_product_from_els
from app.services.recommender import recommend_similar_items


#해당 유저가 본 마지막 상품 기반 추천
async def personal_recommend(user_id):
    df = await get_recent_view_product_from_els(user_id)
    if df.empty:
        return {"message": "최근 본 상품 정보가 없습니다."}

    product_id = int(df.iloc[0]['product_id'])

    recommendation = recommend_similar_items(product_id)

    return {
        "product_id": recommendation["product_id"],
        "recommended_items": recommendation["recommended_items"]
    }
