import re
from typing import List

def load_products_from_md(file_path: str) -> List[dict]:
    with open(file_path, encoding="utf-8") as f:
        text = f.read()

    blocks = re.split(r"\n\s*---+\s*\n", text.strip())
    products = []

    for i, block in enumerate(blocks):
        name_match = re.search(r"##\s*\d+\.\s*(.+)", block)
        price_match = re.search(r"\*\*가격:\*\*\s*([\d,]+원)", block)
        image_match = re.search(r"\*\*이미지 URL:\*\*\s*(.+)", block)
        description_match = re.search(r"\*\*특징:\*\*\s*(.+)", block)


        feature_matches = re.findall(r"\s*-\s*\*\*(.+?:)\*\*\s*(.+)", block)


        if not name_match:
            continue

        name = name_match.group(1).strip()
        price = price_match.group(1).strip() if price_match else ""
        image_url = image_match.group(1).strip() if image_match else ""
        description = description_match.group(1).strip() if description_match else ""

        # 특징/이미지 제외한 항목만 features
        features = [
            f"{key.strip(':').strip()}: {value.strip()}"
            for key, value in feature_matches
            if key.strip(':').strip() not in {"특징", "이미지 URL"}
        ]

        products.append({
            "name": name,
            "price": price,
            "features": features,
            "description": description,
            "imageUrl": image_url,
        })


    return products
