from app.services.order_service import get_user_orders
from app.utills.extract import extract_order_code
from app.database.session import get_db

def state_with_orders(state):
    """
    주문 정보 분류 : 현재 미사용
    :param state:
    :return:
    """
    if state.intent not in ["주문조회", "배송조회", "주문상태"]:
        return state

    if not state.user_id:
        return state

    db = next(get_db())
    all_orders = get_user_orders(db, state.user_id)

    order_code = extract_order_code(state.question)
    if order_code:
        filtered = [o for o in all_orders if str(o["order_code"]) == order_code]
    else:
        filtered = all_orders

    state.order_info = filtered
    state.order_code = order_code
    return state
