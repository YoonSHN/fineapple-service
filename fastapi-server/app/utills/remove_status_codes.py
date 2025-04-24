import re

def remove_status_codes(text: str) -> str:
    return re.sub(r'\s*\([A-Z]+\d+\)', '', text)
