import re
from typing import Optional

def extract_order_code(text: str) -> Optional[str]:
    """
    텍스트에서 13자리 숫자로 된 주문번호 추출
    """
    match = re.search(r"\b(\d{13})\b", text)
    return match.group(1) if match else None
