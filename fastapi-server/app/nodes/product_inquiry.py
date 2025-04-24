import re
from app.crud.product import get_product_id_by_name, get_products
from app.tools.normalize_product import normalize_eng_product_name_to_kor
from app.nodes.rag_generate import rag_generate_node
from app.nodes.rag_retrieve_node import rag_retrieve_node
from app.models.chat import ChatState


async def product_inquiry_node(state: ChatState) -> ChatState:
    try:
        question = state.question.strip() if state.question else ""
        if not question:
            return state.copy(update={
                "answer": "상품에 대한 질문이 감지되지 않았습니다. 다시 입력해 주세요."
            })

        words = re.findall(r"[\w가-힣]+", question)
        product_keywords = [w for w in words if any(k in w.lower() for k in ["fine", "파인", "폰", "패드", "북"])]

        if not product_keywords:
            chatstate = await rag_retrieve_node(state)
            return await rag_generate_node(chatstate)

        normalized_names = [normalize_eng_product_name_to_kor(w) for w in product_keywords]
        found_products = []

        for name in normalized_names:
            pid = await get_product_id_by_name(name)
            if pid:
                prods = await get_products(keyword=name)
                if prods:
                    found_products.append(prods[0])

        if len(found_products) >= 2:
            response = "상품 비교 결과입니다:\n\n"
            for i, p in enumerate(found_products, start=1):
                name = p.get("name", "상품명 없음")
                price = f"{int(p['price']):,}원" if p.get("price") else "가격 정보 없음"
                desc = p.get("description", "설명 없음")
                response += f"{i}. {name} - {price}\n   {desc}\n\n"
        elif len(found_products) == 1:
            p = found_products[0]
            name = p.get("name", "상품명 없음")
            price = f"{int(p['price']):,}원" if p.get("price") else "가격 정보 없음"
            desc = p.get("description", "설명 없음")
            response = f"상품 정보입니다:\n{name} - {price}\n{desc}"
        else:
            response = f"'{', '.join(normalized_names)}' 관련 상품을 찾을 수 없습니다."

        return state.copy(update={"answer": response})

    except Exception as e:
        print(f"[product_inquiry_node] 오류: {e}")
        return state.copy(update={
            "answer": "상품 정보를 조회하는 중 오류가 발생했습니다."
        })
