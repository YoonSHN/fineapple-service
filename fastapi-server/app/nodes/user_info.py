from app.database.db import database
import logging

from app.models.chat import ChatState

logger = logging.getLogger(__name__)

USER_PROFILE_QUERY = """
SELECT 
    u.email, u.create_date,
    ui.name, ui.birth, ui.tel, ui.user_role, ui.user_status
FROM User u
JOIN UserInfo ui ON u.user_id = ui.user_id
WHERE u.user_id = :user_id
"""

async def user_info_node(state: ChatState) -> ChatState:
    """
    사용자 프로필 정보 조회 노드 (User + UserInfo 조인)
    """
    user_id = state.user_id if state.user_id else None
    if not user_id:
        return state.copy(update={"answer": "로그인이 필요합니다. 먼저 로그인 해주세요."})

    try:
        row = await database.fetch_one(query=USER_PROFILE_QUERY, values={"user_id": user_id})
        if not row :
            return state.copy(update={"answer": "사용자 정보를 찾을 수 없습니다."})

        # 안전하게 필드 추출
        def safe(value, default="N/A"):
            return value if value else default

        response = (
            f"📄 사용자 프로필\n"
            f"- 이름: {safe(row.get('name'))}\n"
            f"- 이메일: {safe(row.get('email'))}\n"
            f"- 생년월일: {safe(str(row.get('birth')))}\n"
            f"- 전화번호: {safe(row.get('tel'))}\n"
            f"- 회원 등급: {safe(row.get('user_role'))}\n"
            f"- 계정 상태: {safe(row.get('user_status'))}"
        )
        return state.copy(update={"answer": response})

    except Exception as e:
        logger.warning(f"[user_info_node] 오류 발생: {e}")
        return state.copy(update={"answer": "사용자 정보를 조회하는 중 오류가 발생했습니다."})