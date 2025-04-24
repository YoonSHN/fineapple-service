from fastapi.testclient import TestClient
from app.main import app

client = TestClient(app)

def test_login_and_session():
    response = client.post("/login", json={
        "email":"1234@co.kr",
        "password":"123"
    })

    assert response.status_code == 200
    data = response.json()
    assert "user_id" in data

    me_response = client.get("/me")
    assert me_response.status_code == 200

    me_data = me_response.json()
    assert me_data["user_id"] == data["user_id"]

    print("세션 유지 테스트 성공")

def test_logout():

    client.post("/login", json={
        "email": "test@example.com",
        "password": "123"
    })


    client.post("/logout")

    me_response = client.get("/me")
    assert me_response.status_code == 401

    print("로그아웃 후 인증 실패 테스트 성공")