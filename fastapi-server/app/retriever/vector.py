# app/retriever/vector.py
from langchain_upstage import UpstageEmbeddings
from langchain_community.vectorstores import Chroma

def load_vector_db(path: str = "./data/vectorstore"):
    embedding = UpstageEmbeddings(model="solar-embedding-1-large")
    vectordb = Chroma(
        persist_directory=path,
        embedding_function=embedding
    )
    return vectordb

def search_docs(query: str, vectordb, top_k: int = 3) -> str:
    docs = vectordb.similarity_search(query, k=top_k)
    if not docs:
        return "관련 문서를 찾을 수 없습니다."
    return "\n".join([doc.page_content for doc in docs])
