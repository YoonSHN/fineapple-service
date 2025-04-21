import re
from typing import List, Dict
from app.tools.normalize_product import normalize_eng_product_name_to_kor

def extract_products_from_context(self, context: str) -> List[Dict]:
    if not context or "관련 문서를 찾을 수 없습니다" in context:
        return []

    product_pattern = re.compile(
        r"제품명:\s*(?P<name>.+?)\s*[\n\r]+"
        r"가격:\s*(?P<price>.+?)\s*[\n\r]+"
        r"설명:\s*(?P<description>.+?)\s*[\n\r]+"
        r"주요 기능:\s*(?P<features>.+?)\s*[\n\r]+"
        r"이미지:\s*(?P<imageUrl>.+?)\s*(?:\n|\r|$)",
        re.DOTALL
    )

    products = []
    for match in product_pattern.finditer(context):
        groups = match.groupdict()
        product = {
            "id": str(len(products) + 1),
            "name": groups["name"].strip(),
            "price": groups["price"].strip(),
            "description": groups["description"].strip(),
            "features": [f.strip() for f in groups["features"].split(",")],
            "imageUrl": groups["imageUrl"].strip()
        }
        products.append(product)

    return products

def extract_product_names_from_llm_answer(text: str, known_names: List[str]) -> List[str]:
    """ RDB에서 가져오는 방식말고 MD파일에서 가져오는 방식을 선택
    1. md 에서 파싱해서 가져오기
    2. 응답에서 이름을 추출
    3. 영어일경우 한글로 번역후 캐쉬된 md파일에서 리스트로 뽑아오기
    4. product를 전달
    """
    candidates = re.findall(r'\b(Fine\w+)\s+(?:Series\s\d+|\d+\s?\w*|\w+)', text)

    raw_candidates = [' '.join(c).strip() for c in candidates]

    # 영어 → 한글 변환
    normalized = [normalize_eng_product_name_to_kor(name) for name in raw_candidates]

    # 텍스트에 직접 포함된 한글 제품명도 찾아봄
    direct_matches = [name for name in known_names if name in text]

    # 변환된 이름이 known 목록에 있으면 채택
    matched = [name for name in known_names if name in normalized or name in direct_matches]

    # 긴 이름 우선 + 중복 제거
    matched = sorted(set(matched), key=lambda x: -len(x))


    return matched

def find_products_by_name(names: List[str], product_cache: List[dict]) -> List[dict]:
    name_set = set(names)
    seen = set()
    results = []

    for product in product_cache:
        name = product["name"]
        if name in name_set and name not in seen:
            results.append(product)
            seen.add(name)

    return results