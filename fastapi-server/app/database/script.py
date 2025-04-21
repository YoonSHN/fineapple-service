import csv
import random
import datetime
import json
import time
from collections import defaultdict

start_time = time.time()

# 무작위 datetime 생성
def random_datetime(start, end):
    delta = end - start
    return start + datetime.timedelta(seconds=random.randint(0, int(delta.total_seconds())))

# 날짜 범위 현재시간 기준
end_date = datetime.datetime.now()
start_date = end_date - datetime.timedelta(days=365)

total_orders = 100_000
order_ids = list(range(1, total_orders + 1))
random.shuffle(order_ids)

# 주문 상태 세팅
order_status_cancel = 'OR0403'
possible_statuses = ['OR0102', 'OR0202', 'OR0403', 'OR0204','SH0102','SH0103']

# 파일명 및 필드 정의 추가예정
orders_file = 'orders_bulk.csv'
order_item_detail_file = 'OrderItemDetail_bulk.csv'
payment_file = 'Payment_bulk.csv'
payment_detail_file = 'PaymentDetail_bulk.csv'
history_file = 'OrderItemDetail_History_bulk.csv'
order_status_file = 'OrderStatus_bulk.csv'

orders_fieldnames = [
    'order_id', 'created_at', 'updated_at', 'order_code',
    'total_price', 'discount_price', 'is_cancelled',
    'delivery_id', 'guest_id', 'user_id',
    'order_status', 'payment_method'
]

order_item_detail_fields = [
    'orderitemdetail_id', 'name', 'quantity', 'price', 'created_at', 'updated_at',
    'discount_price', 'coupon_applied', 'additional_price', 'additional',
    'order_id', 'product_id', 'option_id', 'item_status'
]

payment_fields = [
    'payment_id', 'order_id', 'payment_status', 'requested_at', 'total_amount',
    'pg_approval_code', 'currency', 'paid_at', 'cancelled_at', 'receipt_url',
    'card_name', 'card_quota', 'payment_method', 'failure_code', 'pg_unique_id', 'pg_type'
]

payment_detail_fields = [
    'detail_id', 'payment_id', 'orderitemdetail_id', 'product_name', 'paid_amount',
    'quantity', 'cancelled_quantity', 'cancelled_amount', 'fail_reason',
    'cancelled_at', 'created_at', 'updated_at'
]

history_fields = [
    'history_id', 'orderitemdetail_id', 'old_price', 'new_price',
    'old_quantity', 'new_quantity', 'change_reason', 'changed_at', 'changed_by', 'item_history_status'
]

order_status_fields = [
    'orderstatus_id', 'order_id', 'payment_status', 'updated_at', 'orderstatus_status'
]

# 샘플 데이터 정의 나중에 더 추가해야함
item_names = ['iPad Pro M4', 'Mac Mini M4', 'iPhone 16 Pro Max', 'Apple Watch Series 10']

item_statuses = ['OR0401', 'OR0402']

history_statuses = ['OR0401', 'OR0402', 'OR0403']

payment_status_options = ['OR0201', 'OR0202', 'OR0204']

orderstatus_status_options = ['OR0403', 'OR0401']
change_reasons = ['상품정률 5% 할인', '쿠폰사용', '전체 환불']
memory_options = ["16GB", "512GB", "32GB", "1TB"]
product_names = ['상품 A', '상품 B', '상품 C', '상품 D']

# 매핑
order_items_map = defaultdict(list)
payment_id_counter = 1
detail_id_counter = 1

# 파일 열기
with open(orders_file, 'w', newline='', encoding='utf-8') as f1, \
        open(order_item_detail_file, 'w', newline='', encoding='utf-8') as f2, \
        open(payment_file, 'w', newline='', encoding='utf-8') as f3, \
        open(payment_detail_file, 'w', newline='', encoding='utf-8') as f4, \
        open(history_file, 'w', newline='', encoding='utf-8') as f5, \
        open(order_status_file, 'w', newline='', encoding='utf-8') as f6:

    w1 = csv.DictWriter(f1, fieldnames=orders_fieldnames)
    w2 = csv.DictWriter(f2, fieldnames=order_item_detail_fields)
    w3 = csv.DictWriter(f3, fieldnames=payment_fields)
    w4 = csv.DictWriter(f4, fieldnames=payment_detail_fields)
    w5 = csv.DictWriter(f5, fieldnames=history_fields)
    w6 = csv.DictWriter(f6, fieldnames=order_status_fields)

    w1.writeheader()
    w2.writeheader()
    w3.writeheader()
    w4.writeheader()
    w5.writeheader()
    w6.writeheader()

    payment_id = 1
    detail_id = 1
    history_id = 1

    for order_id in order_ids:
        created_dt = random_datetime(start_date, end_date)
        updated_dt = created_dt + datetime.timedelta(seconds=random.randint(0, 86400))
        order_code = f"{created_dt.strftime('%Y%m%d')}{str(order_id).zfill(5)}"
        order_status = random.choice(possible_statuses)
        is_cancelled = 1 if order_status == order_status_cancel else 0

        total_price = 0
        total_discount = 0

        # 주문상세 1~3개
        for idx in range(random.randint(1, 3)):
            quantity = random.randint(1, 10)
            price = random.randint(100000, 3000000)
            discount_price = random.randint(0, 100000)
            total_price += price * quantity
            total_discount += discount_price

            additional = json.dumps({random.choice(memory_options): random.randint(50000, 200000)}, ensure_ascii=False) if random.choice([True, False]) else r'\N'
            cdt = random_datetime(start_date, end_date)
            udt = cdt + datetime.timedelta(seconds=random.randint(0, 86400))
            orderitemdetail_id = order_id * 10 + idx

            w2.writerow({
                'orderitemdetail_id': orderitemdetail_id,
                'name': random.choice(item_names),
                'quantity': quantity,
                'price': price,
                'created_at': cdt.strftime('%Y-%m-%d %H:%M:%S'),
                'updated_at': udt.strftime('%Y-%m-%d %H:%M:%S'),
                'discount_price': discount_price,
                'coupon_applied': random.choice([0, 1]),
                'additional_price': 0,
                'additional': additional,
                'order_id': order_id,
                'product_id': random.randint(1, 10),
                'option_id': random.randint(1, 10),
                'item_status': random.choice(item_statuses)
            })

            for _ in range(random.randint(1, 2)):
                old_price = random.randint(100000, 3000000)
                new_price = old_price - random.randint(0, 10000)
                old_qty = random.randint(1, 10)
                new_qty = random.choice([old_qty, 0])
                h_dt = random_datetime(start_date, end_date)

                w5.writerow({
                    'history_id': history_id,
                    'orderitemdetail_id': orderitemdetail_id,
                    'old_price': old_price,
                    'new_price': new_price,
                    'old_quantity': old_qty,
                    'new_quantity': new_qty,
                    'change_reason': random.choice(change_reasons) if random.random() < 0.7 else r'\N',
                    'changed_at': h_dt.strftime('%Y-%m-%d %H:%M:%S'),
                    'changed_by': random.randint(1, 3),
                    'item_history_status': random.choice(history_statuses)
                })
                history_id += 1

            order_items_map[order_id].append({
                'orderitemdetail_id': orderitemdetail_id,
                'quantity': quantity,
                'price': price
            })

        w1.writerow({
            'order_id': order_id,
            'created_at': created_dt.strftime('%Y-%m-%d %H:%M:%S'),
            'updated_at': updated_dt.strftime('%Y-%m-%d %H:%M:%S'),
            'order_code': order_code,
            'total_price': total_price,
            'discount_price': total_discount,
            'is_cancelled': is_cancelled,
            'delivery_id': r'\N',
            'guest_id': r'\N',
            'user_id': random.randint(1, 5),
            'order_status': order_status,
            'payment_method': random.choice(['OR0501', 'OR0502'])
        })

        status_dt = random_datetime(start_date, end_date)
        w6.writerow({
            'orderstatus_id': order_id,
            'order_id': order_id,
            'payment_status': 'OR0202' if order_status == 'OR0202' else random.choice(payment_status_options),
            'updated_at': status_dt.strftime('%Y-%m-%d %H:%M:%S'),
            'orderstatus_status': order_status
        })
        # 해당조건을 만족해야 결제정보 생성 추가예야함 더
        if order_status in ['OR0202', 'SH0102', 'SH0103']:
            rdt = random_datetime(start_date, end_date)
            pdt = rdt + datetime.timedelta(seconds=random.randint(0, 3600))

            w3.writerow({
                'payment_id': payment_id,
                'order_id': order_id,
                'payment_status': 'OR0202',
                'requested_at': rdt.strftime('%Y-%m-%d %H:%M:%S'),
                'total_amount': total_price,
                'pg_approval_code': 'A' + str(random.randint(1000000000, 9999999999)),
                'currency': 'KRW',
                'paid_at': pdt.strftime('%Y-%m-%d %H:%M:%S'),
                'cancelled_at': r'\N',
                'receipt_url': f"http://receipt.url/{payment_id}",
                'card_name': 'dummyCard',
                'card_quota': random.choice([1, 2, 3]),
                'payment_method': 'OR0501',
                'failure_code': r'\N',
                'pg_unique_id': f"PGUID{payment_id}",
                'pg_type': 'inicis'
            })

            for item in order_items_map[order_id]:
                cd_dt = random_datetime(start_date, end_date)
                ud_dt = cd_dt + datetime.timedelta(seconds=random.randint(0, 3600))
                cancel_qty = random.randint(0, item['quantity'])

                if cancel_qty > 0:
                    cancel_amt = round(item['price'] * cancel_qty, 2)
                    fail_reason = random.choice(['카드 사용 불가 상태', '카드 한도 초과'])
                    cancel_dt = pdt + datetime.timedelta(seconds=random.randint(0, 3600))
                    cancel_at = cancel_dt.strftime('%Y-%m-%d %H:%M:%S')
                else:
                    cancel_amt = r'\N'
                    fail_reason = r'\N'
                    cancel_at = r'\N'

                w4.writerow({
                    'detail_id': detail_id,
                    'payment_id': payment_id,
                    'orderitemdetail_id': item['orderitemdetail_id'],
                    'product_name': random.choice(product_names),
                    'paid_amount': item['price'] * item['quantity'],
                    'quantity': item['quantity'],
                    'cancelled_quantity': cancel_qty,
                    'cancelled_amount': cancel_amt,
                    'fail_reason': fail_reason,
                    'cancelled_at': cancel_at,
                    'created_at': cd_dt.strftime('%Y-%m-%d %H:%M:%S'),
                    'updated_at': ud_dt.strftime('%Y-%m-%d %H:%M:%S')
                })
                detail_id += 1

            payment_id += 1

print(f"전체 실행 시간: {time.time() - start_time:.2f}초")
