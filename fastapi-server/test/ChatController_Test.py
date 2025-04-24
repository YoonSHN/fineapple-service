import json
from app.controllers.chat_controller import ChatController
from app.models.chat import ChatRequest
import asyncio

async def run_test():
    controller = ChatController()
    req = ChatRequest(query="환불은 어떻게 하나요?", user_id=1)
    res = await controller.process_chat(req)
    print(json.dumps(res.model_dump(), indent=2))

asyncio.run(run_test())
