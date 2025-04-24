# app/retriever/vector.py
import os
from langchain_community.vectorstores import Chroma
from langchain_upstage import UpstageEmbeddings

DEFAULT_DB_DIR = "./data/vectorstore"

def create_vector_db(documents, embedding_model, persist_dir=DEFAULT_DB_DIR):
    vectordb = Chroma.from_documents(
        documents=documents,
        embedding=embedding_model,
        persist_directory=persist_dir
    )
    return vectordb

def load_vector_db(embedding_model, persist_dir=DEFAULT_DB_DIR):
    return Chroma(
        persist_directory=persist_dir,
        embedding_function=embedding_model
    )

def get_or_create_vector_db(documents=None, embedding_model=None, persist_dir=DEFAULT_DB_DIR):
    if not embedding_model:
        embedding_model = UpstageEmbeddings(model="solar-embedding-1-large")

    if os.path.exists(persist_dir) and os.listdir(persist_dir):
        return load_vector_db(embedding_model, persist_dir)
    elif documents:
        return create_vector_db(documents, embedding_model, persist_dir)
    else:
        raise ValueError("벡터 DB를 생성하려면 documents가 필요합니다.")

def search_docs(query: str, vectordb, top_k: int = 3) -> str:
    docs = vectordb.similarity_search(query, k=top_k)
    if not docs:
        return "관련 문서를 찾을 수 없습니다."
    return "\n".join([doc.page_content for doc in docs])
