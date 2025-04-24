class OrderPresenter:
    def format_orders(self, orders: list) -> list:
        return [order.to_dict() for order in orders]

    def format_order_code(self, order_code: str) -> str:
        return order_code 