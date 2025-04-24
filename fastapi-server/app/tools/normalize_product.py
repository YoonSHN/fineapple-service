import re

def normalize_eng_product_name_to_kor(name: str) -> str:
    PREFIX_MAP = {
        "FinePhone": "파인폰",
        "FineBook": "파인북",
        "FinePad": "파인패드",
        "FineWatch": "파인워치",
        "FinePods": "파인팟",
        "FineBuds": "파인팟",
        "FineDisplay": "파인디스플레이",
        "FineCharge": "파인차지",
        "iFine": "아이파인",
    }

    SUFFIX_MAP = {
        "Pro": "프로",
        "Mini": "미니",
        "Ultra": "울트라",
        "SE": "SE",
        "Air": "에어",
    }

    name = re.sub(r"[\s\-]", "", name)  # 공백/하이픈 제거

    for eng_prefix, kor_prefix in PREFIX_MAP.items():
        if name.startswith(eng_prefix):
            remain = name[len(eng_prefix):]

            if remain.startswith("Series"):
                series_num = re.findall(r'\d+', remain)
                return f"{kor_prefix} 시리즈 {series_num[0]}" if series_num else f"{kor_prefix} 시리즈"

            tokens = re.findall(r'(?:SE|Pro|Mini|Ultra|Air)|\d+', remain)

            model_number = ""
            suffix = ""

            for token in tokens:
                if token.isdigit():
                    model_number = token
                elif token in SUFFIX_MAP:
                    suffix = SUFFIX_MAP[token]

            result = kor_prefix
            if model_number:
                result += f" {model_number}"
            if suffix:
                result += f" {suffix}"

            return result.strip()

    return name
