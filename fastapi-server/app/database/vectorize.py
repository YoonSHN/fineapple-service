import pandas as pd
from scipy.sparse import hstack
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import StandardScaler

def vectorize_product_data(product_df, category_df):
    # 상품 이름 벡터화 TF-IDF
    vectorize = TfidfVectorizer(stop_words='english')
    product_name_vectors = vectorize.fit_transform(product_df['name'])

    # 카테고리 벡터화 원- 핫 인코딩
    # 카테고리가 계층 구조라 부모 카테고리까지 합쳐서 벡터화
    df = product_df.merge(category_df[['category_id', 'parent_id']], on='category_id', how='left')
    df['parent_id'] = df['parent_id'].fillna(-1)
    category_vectors = pd.get_dummies(df['parent_id'])


    # 가격 정규화
    scaler = StandardScaler()
    price_vectors = scaler.fit_transform(product_df[['price']])


    # 벡터화 합치기
    combined_vectors = hstack([product_name_vectors, category_vectors, price_vectors])

    return combined_vectors

def calculate_cosine_similarity(combined_vectors):
    # 코사인 유사도 계산.
    cosine_sim = cosine_similarity(combined_vectors)
    return cosine_sim