from app.models.user import UserResponse

class AuthPresenter:
    def format_login_response(self, user) -> dict:
        return {
            "message": "로그인 성공",
            "user": UserResponse(
                id=user.id,
                email=user.email,
                name=user.name
            )
        } 