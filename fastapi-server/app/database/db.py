from databases import Database
import os
from dotenv import load_dotenv

load_dotenv()

DATABASE_URL = os.getenv(
    "DATABASE_URL",
    "mysql+aiomysql://root:1234@127.0.0.1:3306/fineapple"
)

database = Database(DATABASE_URL)
