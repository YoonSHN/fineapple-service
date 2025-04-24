# from fastapi import APIRouter, Request
# from pydantic import BaseModel
# from typing import Optional
# import asyncio
# from dotenv import load_dotenv
#
# load_dotenv()
#
# from langchain.agents import Tool, initialize_agent
# from langchain_community.chat_models import ChatOpenAI
# from app.tools.chat_tools import (
#     class_intent_tool,
#     retrieve_relevant_docs,
#     generate_response_tool,
# )
#
# router = APIRouter()
#
# class AgentChatRequest(BaseModel):
#     query: str
#     user_id: Optional[int] = None
#
# # 비동기 MCP 툴을 동기 wrapper로 감싸기
# def syncify(async_func):
#     def wrapper(*args, **kwargs):
#         return asyncio.run(async_func(*args, **kwargs))
#     return wrapper
#
#
# tools = [
#     Tool(name="IntentClassifier", func=syncify(class_intent_tool), description="의도를 분류합니다."),
#     Tool(name="DocRetriever", func=syncify(retrieve_relevant_docs), description="관련 문서를 검색합니다."),
#     Tool(name="AnswerGenerator", func=syncify(generate_response_tool), description="응답을 생성합니다."),
# ]
#
#
# llm = ChatOpenAI(model_name="gpt-4", temperature=0)
#
#
# agent = initialize_agent(tools, llm, agent="zero-shot-react-description", verbose=True)
#
# @router.post("/agent-chat")
# async def agent_chat(request: AgentChatRequest):
#     try:
#         response = await asyncio.to_thread(agent.run, request.query)
#         return {"content": response}
#     except Exception as e:
#         return {"error": str(e)}
