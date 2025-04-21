from langchain_core.messages import BaseMessage
from langchain_upstage import ChatUpstage
import os
from dotenv import load_dotenv
import logging

load_dotenv()
logger = logging.getLogger(__name__)
llm = ChatUpstage(model="solar-mini-250123")

def call_solar(prompt: str) -> BaseMessage:
    """LangChain LLM을 함수처럼 감싸는 래퍼"""
    try:
        logger.info(f"[LLM 요청] {prompt}")
        response = llm.invoke(prompt)
        logger.info(f"[LLM 응답] {response}")
        return response
    except Exception as e:
        logger.exception("LLM 호출 실패")
        return BaseMessage(content="LLM 호출 중 오류 발생")