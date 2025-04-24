from fastapi import APIRouter
from watchfiles import awatch

from app.services.persnal_user_recommender import personal_recommend
from app.services.recommender import recommend_similar_items

router = APIRouter(
    prefix="/api/v1/recommend",
    tags = ["recommend"]
)

@router.get("/{product_id}")
async def get_recommendations(product_id: int):
    return await recommend_similar_items(product_id)

@router.get("/user/{user_id}")
async def personal_recommendations(user_id: int):
    return await personal_recommend(user_id)



