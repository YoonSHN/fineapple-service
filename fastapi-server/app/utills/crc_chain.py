from langchain.chains import ConversationalRetrievalChain
from langchain.memory import ConversationBufferMemory
from langchain_upstage import ChatUpstage, UpstageEmbeddings
from app.retriever.vector import load_vector_db
from app.tools.wrappers import AnswerOnlyWrapper
from dotenv import load_dotenv

load_dotenv()

embedding_model = UpstageEmbeddings(model="solar-embedding-1-large")
vectordb = load_vector_db(embedding_model)
memory = ConversationBufferMemory(memory_key="chat_history", return_messages=True)
llm = ChatUpstage(model="solar-pro")
"""
1. 유사도 기반 검색
2. crc 체인을 통해 메모리에 리트리버를 저장하여 대화형 체인을 구성
3. 문서와 대답 output을 외부 메모리인 AnswerOnlyWrapper으로 통일 
"""
base_crc = ConversationalRetrievalChain.from_llm(
    llm=llm,
    retriever=vectordb.as_retriever(search_type="mmr"),
    memory=None,
    return_source_documents=True
)

crc_chain = AnswerOnlyWrapper(chain=base_crc,  memory=memory)
