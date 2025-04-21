from app.tools.chat_tools import process_chat_tool
import asyncio

async def test():

    result = await process_chat_tool("2024040500001 주문 배송됐나요?", user_id=8)

    print(result)

asyncio.run(test())
