from pydantic import BaseModel, EmailStr

class UserLogin(BaseModel):
    email: EmailStr
    password: str

class UserResponse(BaseModel):
    id: int
    email: str
    name: str

class Token(BaseModel):
    access_token: str
    token_type: str

class User:
    def __init__(self, user_id: int, username: str):
        self.user_id = user_id
        self.username = username

    def to_dict(self):
        return {
            "user_id": self.user_id,
            "username": self.username
        }