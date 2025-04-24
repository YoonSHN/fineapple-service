from sqlalchemy import text
from app.database.session import get_db
from app.models.message import Message
from sqlalchemy.orm import Session

def save_message(message: Message):
    db: Session = next(get_db())

    sql = text("""
        INSERT INTO Message (user_id, role, content, intent)
        VALUES (:user_id, :role, :content, :intent)
    """)

    db.execute(sql, {
        "user_id": message.user_id,
        "role": message.role,
        "content": message.content,
        "intent": message.intent
    })
    db.commit()


def get_recent_messages(user_id: int, limit: int = 5):
    db: Session = next(get_db())

    sql = text("""
        SELECT role, content, intent
        FROM Message
        WHERE user_id = :user_id
        ORDER BY created_at DESC
        LIMIT :limit
    """)

    result = db.execute(sql, {
        "user_id": user_id,
        "limit": limit
    }).fetchall()

    return [{"role": row.role, "content": row.content, "intent": row.intent} for row in reversed(result)]




