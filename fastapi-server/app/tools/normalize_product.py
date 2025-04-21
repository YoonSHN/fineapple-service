import logging
import re
from typing import List

def normalize_eng_product_name_to_kor(name: str) -> str:
    PREFIX_MAP = {
        "FinePhone": "파인폰",
        "FineBook": "파인북",
        "FinePad": "파인패드",
        "FineWatch": "파인워치",
        "FinePods": "파인팟",
        "FineBuds": "파인팟",  # LLM이 잘못 부를 수 있음
        "FineDisplay": "파인디스플레이",
        "FineCharge": "파인차지",  # 충전기류 대응
        "iFine": "아이파인",
    }

    SUFFIX_MAP = {
        "Pro": "Pro",
        "Mini": "미니",
        "Ultra": "울트라",
        "SE": "SE",
    }

    name.replace(" ", "")
    for eng_prefix, kor_prefix in PREFIX_MAP.items():
        if name.startswith(eng_prefix):
            remain = name[len(eng_prefix):].strip()
            tokens = remain.split()

            if not tokens:
                return kor_prefix

            first = tokens[0]
            suffix = " ".join(tokens[1:]) if len(tokens) > 1 else ""
            kor_suffix = SUFFIX_MAP.get(first, first)

            # Series는 특수 처리
            if first == "Series" and len(tokens) > 1:
                return f"{kor_prefix} 시리즈 {tokens[1]}"

            return f"{kor_prefix} {kor_suffix}".strip()

    return name