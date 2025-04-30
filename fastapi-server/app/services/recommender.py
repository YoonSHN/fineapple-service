import pandas as pd
from unicodedata import category


from app.database.vectorize import vectorize_product_data, calculate_cosine_similarity
from app.database.connection import get_product_data, get_category_data


def recommend_similar_items(product_id: int):

    product_df = get_product_data()
    category_df = get_category_data()
    if product_id not in product_df['product_id'].values:
        return {"message": f"{product_id}에 대한 추천 결과가 없습니다."}

    # 벡터화
    vectors = vectorize_product_data(product_df,category_df)

    cosine_sim = calculate_cosine_similarity(vectors)

    # 상품 ID 기준으로 추천 정리
    sim_df = pd.DataFrame(cosine_sim, index=product_df['product_id'], columns=product_df['product_id'])

    # 유사 상품 3개 추출
    similar_items = sim_df[product_id].sort_values(ascending=False).drop(product_id).head(3)

    return {
        "product_id": product_id,
        "recommended_items": similar_items.index.tolist()
    }