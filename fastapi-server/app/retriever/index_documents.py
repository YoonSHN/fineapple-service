# scripts/index_documents.py
from langchain_upstage import UpstageEmbeddings
from langchain_community.document_loaders import UnstructuredMarkdownLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_community.vectorstores import Chroma
from chromadb.config import Settings
import os
from dotenv import load_dotenv
load_dotenv()

def index_documents():
    embedding = UpstageEmbeddings(model="solar-embedding-1-large")
    base_dir = os.path.dirname(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
    folder_path = os.path.join(base_dir, "data/docs")
    docs = []

    for filename in os.listdir(folder_path):
        if filename.endswith(".md"):
            filepath = os.path.join(folder_path, filename)
            loader = UnstructuredMarkdownLoader(filepath)
            docs.extend(loader.load())

    splitter = RecursiveCharacterTextSplitter(chunk_size=1000, chunk_overlap=100)

    chunks = splitter.split_documents(docs)

    vectorstore_path = os.path.join(base_dir, "data/vectorstore")


    client_settings = Settings(
        anonymized_telemetry=False,
        settings={
            "hnsw:configuration": {
                "num_threads": 4
            }
        }
    )

    vectordb = Chroma.from_documents(
        documents=chunks,
        embedding=embedding,
        persist_directory=vectorstore_path
    )
    vectordb.persist()

if __name__ == "__main__":
    index_documents()
