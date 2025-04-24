from langchain_upstage import UpstageEmbeddings

from app.models.chat import ChatState
import logging
from app.retriever.vector import load_vector_db
from app.utills.crc_chain import crc_chain
from dotenv import load_dotenv
load_dotenv()
logger = logging.getLogger(__name__)
embedding_model = UpstageEmbeddings(model="solar-embedding-1-large")

vector_store = load_vector_db(embedding_model)

async def rag_retrieve_node(state: ChatState) -> ChatState:
    try:
        question = state.question.strip()

        chat_history = []
        for i in range(len(state.history) - 1):
            msg = state.history[i]
            next_msg = state.history[i + 1]
            if msg["role"] == "user" and next_msg["role"] == "assistant":
                chat_history.append((msg["content"], next_msg["content"]))

        result = await crc_chain.ainvoke({
            "question": question,
            "chat_history": chat_history
        })

        context = result.get("answer", "관련 문서를 찾을 수 없습니다.")

        return state.copy(update={
            "context": context,
            "history": state.history + [{"role": "user", "content": question}]
        })
    except Exception as e:
        logger.warning(f"[rag_retrieve_node] 오류: {e}")
        return state.copy(update={
            "context": "문서 검색 중 오류가 발생했습니다.",
            "history": state.history + [{"role": "user", "content": state.question}]
        })
