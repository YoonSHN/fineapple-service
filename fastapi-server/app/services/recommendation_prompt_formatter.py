import pandas as pd

from app.database.connection import get_product_data, get_category_data


def format_recommendation_prompt(product_id: int, recommended_items: list) -> str:
    """
    상품 ID 및 추천된 상품 ID 리스트를 받아, 자연어 스타일로 추천 메시지를 생성.
    -> llm 프롬프트용

    Parameters:
        product_id (int): 기준 상품 ID
        recommended_items (list): 추천된 상품 ID 목록 3가지 유사 상품 추천
        df (pd.DataFrame): 전체 상품

    Returns:
        str: 자연어 추천 메시지
    """
    product_df = get_product_data()
    category_df = get_category_data()

    # 카테고리 이름이 상품 name 하고 겹치니 category_name으로 변경
    category_df.rename(columns={'name': 'category_name'}, inplace=True)
    # 상품 데이터와 카테고리 데이터 병합
    merged_df = product_df.merge(category_df, how="left", on="category_id")


    # 기준 상품 정보
    base_product = merged_df[merged_df['product_id'] == product_id].iloc[0]
    base_name = base_product['name']

    # 추천 상품들
    recommended_items_df = merged_df[merged_df['product_id'].isin(recommended_items)]


    lines = [f"최근에 '{base_name}' 상품을 보셨네요. 이와 유사한 제품들을 추천드립니다:"]
    for idx, row in recommended_items_df.iterrows():
        name = row['name']
        price = row['price']
        category = row['category_name']
        lines.append(f"- '{name}' (가격: {price}원, 카테고리: {category})")

    return "\n".join(lines)