#  -- 25. 결제 (Payment)
#  INSERT INTO Payment (payment_id, order_id, payment_status, requested_at, total_amount, pg_approval_code, currency, paid_at, cancelled_at, receipt_url, card_name, card_quota, payment_method, failure_code, pg_unique_id, pg_type)
#  VALUES
#      (1, 1, 'OR0202', '2025-03-12 10:00:00', 50000, 'A1234567890', 'KRW', '2025-03-12 10:00:00', NULL, 'http://receipt.url/1001', 'dummyCard', 1, 'OR0501', NULL, 'PGUID1001', 'inicis'),
#      (2, 2, 'OR0201', '2025-03-12 11:00:00', 75000, NULL, 'KRW', '2025-03-12 11:00:00', '2025-03-12 11:05:00', NULL, 'dummyCard', 2, 'OR0501', NULL, 'PGUID1002', 'inicis'),
#      (3, 3, 'OR0204', '2025-03-12 12:00:00', 100000, NULL, 'KRW', NULL, NULL, NULL, 'dummyCard', 3, 'OR0502', 'ERR003', 'PGUID1003', 'inicis');
#
#  -- 26. 결제 상세 (PaymentDetail)
#  INSERT INTO PaymentDetail (detail_id, payment_id, orderitemdetail_id, product_name, paid_amount, quantity, cancelled_quantity, cancelled_amount, fail_reason, cancelled_at, created_at, updated_at)
#  VALUES
#      (1, 1, 1, '상품 A', 25000, 2, 0, NULL, '카드 사용 불가 상태', NULL, '2025-03-12 10:00:00', '2025-03-12 12:00:00'),
#      (2, 2, 2, '상품 B', 75000, 1, 0, 100000, NULL, '2025-03-12 12:30:00', '2025-03-12 11:00:00', '2025-03-12 11:00:00'),
#      (3, 3, 3, '상품 C', 100000, 1, 1, NULL, '카드 한도 초과', NULL, '2025-03-12 12:00:00', '2025-03-12 15:00:00');

-- 22. 할인 (Discount)
INSERT INTO Discount (discount_id, order_id, discount_type, discount_name, discount_code, discount_price, discount_start_day, discount_end_day, minimum_price, is_active, product_id)
VALUES
    (1, 1, 'DC0101', '카드사 할인', 'KAKAO0313', 10, '2025-03-02 23:31:09', '2025-03-02 23:31:09', 5000, FALSE, 1),
    (2, 2, 'DC0102', '구매 금액별 할인', 'OVERMILLION1', 0, '2025-03-02 23:31:09', '2026-03-02 23:31:08', 5000, TRUE, 1),
    (3, 3, 'DC0101', '시즌 할인', 'SPRING2025', 0, '2025-03-02 23:31:09', '2025-03-21 23:31:09', 5000, TRUE, 1),
    (4, 4, 'DC0102', '신학기 맞이 대표 상품 할인', 'WELCOME2025', 10000, '2025-03-02 23:31:59', '2025-03-22 23:39:01', 5000, TRUE, 1),
    (5, 5, 'DC0102', '직원용 할인 코드', 'EMPLOYEE2025-1', 300000, '2025-03-03 11:38:11', '2025-03-29 11:59:41', 5000, TRUE, 1);

-- 23. 장바구니 아이템 (CartItem)
INSERT INTO CartItem (cart_item_id, cart_id, product_id, created_at, updated_at, quantity, options)
VALUES
    (1, 1, 1, '2025-01-01 12:34:56', NULL, 1,'{"13인치":50000}'),
    (2, 2, 2, '2025-01-02 12:34:56', NULL, 1, NULL),
    (3, 3, 3, '2025-01-03 12:11:11', '2025-01-03 12:11:11', 1, NULL);

-- 24. 배송 (Shipment)
INSERT INTO Shipment (shipment_id, tracking_number, tracking_url, estimated_delivery_date, dispatched_at, delivered_at, created_at, updated_at, order_id, delivery_status, store_id, delivery_id, shipping_cost, courier_company, delay_reason, delivery_agent_name, delivery_agent_contact, post_num, city, region, road_num, address, store_name)
VALUES
    (1001, 'TRK123456', 'http://tracking.url/1001', '2025-03-15 18:00:00', '2025-03-15 09:00:00', '2025-03-15 17:50:00', '2025-03-15 08:55:00', '2025-03-15 17:55:00', 1, 'SH0103', 1, 1, 5000, 'apple', '교통사고', '한국진', '010-0000-1111', '12345', '서울특별시', '강남구', '테헤란로 123', '101호', 'applestore1'),
    (1002, 'TRK654321', 'http://tracking.url/1002', '2025-03-16 20:00:00', '2025-03-16 10:00:00', '2025-03-16 19:45:00', '2025-03-16 09:50:00', '2025-03-16 19:50:00', 2, 'SH0102', 2, 2, 0, 'apple', NULL, '한국', '010-0000-1112', '23456', '서울특별시', '종로구', '세종대로 456', '202호', 'applestore2'),
    (1003, 'TRK112233', 'http://tracking.url/1003', '2025-03-17 15:00:00', '2025-03-17 08:00:00', '2025-03-17 14:45:00', '2025-03-17 07:55:00', '2025-03-17 14:50:00', 3, 'SH0102', 1, 3, 0, 'apple', NULL, '한국사', '010-0000-1113', '34567', '경기도', '수원시', '장안로 789', '303호', 'applestore3');


-- 27. 환불 (Refund)
INSERT INTO Refund (refund_id, payment_id, refund_status, requested_at, approved_at, refund_total_amount, refund_reason, pg_response_code, refund_fail_reason)
VALUES
    (1, 1, 'OR0301', '2025-03-12 10:10:00', NULL, 50000, '고객 요청', 'RFG1001', NULL),
    (2, 2, 'OR0303', '2025-03-12 11:10:00', NULL, 75000, '상품 불량', 'RFG1002', NULL),
    (3, 3, 'OR0304', '2025-03-12 12:35:00', '2025-03-12 13:20:00', 100000, '단순 변심', 'RFG1003', NULL);

-- 28. 환불 상세 (RefundDetail)
INSERT INTO RefundDetail (RefundTransaction, refund_id, issue_status, approved_time, approved_number, request_price, remaining_price, refund_bank_name, refund_bank_code)
VALUES
    (1, 1, 'OK', '2025-03-12 10:10:00', 'APRV000001', 100, 9000, '신한은행', '81'),
    (2, 2, 'OK', '2025-03-12 10:10:00', 'APRV000002', 5000, 50000, '국민은행', '03'),
    (3, 3, 'OK', '2025-03-12 10:10:00', 'APRV000003', 10000, 1500, '우리은행', '81'),
    (4, 3, 'OK', '2025-03-12 10:10:00', 'APRV000004', 3000, 0, '하나은행', '03'),
    (5, 3, 'OK', '2025-03-12 10:10:00', 'APRV000005', 10000, 0, '농협은행', '88');

-- 29. 고객지원문의 (SupportInquiry)
INSERT INTO SupportInquiry (inquiry_id, subject, content, created_at, updated_at, user_info, inquiry_status, inquiry_type, email, name, company_name, country_region, assigned_to, resolved_by, acceptprivacypolicy, response_due_date, prioritycode, order_id, orderitemdetail_id, parent_inquiry_id, language_code)
VALUES
    (1, '배송 지연 문의', '', '2025-03-10 10:00:00', '2025-03-10 10:05:00', 1, 'IN0101', 'IN0202', 'customer1@naver.com', '임한규', NULL, '서울', '김매니저', '김매니저', TRUE, '2025-03-15 09:00:00', 'IN0303', NULL, NULL, NULL, 'IN0401'),
    (2, '상품 불량 문의', '', '2025-03-11 14:30:00', '2025-03-11 14:35:00', 2, 'IN0102', 'IN0201', 'customer2@example.com', '곽우진', '패캠', '서울', '박담당자', '박매니저', TRUE, '2025-03-15 10:00:00', 'IN0302', 2, NULL, NULL, 'IN0401'),
    (3, '교환 요청', '', '2025-03-13 09:00:00', '2025-03-13 09:05:00', 3, 'IN0103', 'IN0203', 'customer3@daum.net', '한국진', NULL, NULL, '박담당자', '박매니저', TRUE, '2025-03-16 11:00:00', 'IN0301', NULL, NULL, NULL, 'IN0401');

-- 30. 고객지원답변 (SupportResponse)
INSERT INTO SupportResponse (answer_id, content, created_at, updated_at, inquiry_id, user_info, language_code)
VALUES
    (1, '배송 지연 문의에 대해 확인 후 곧 안내드리겠습니다.', '2025-03-10 10:10:00', '2025-03-10 10:10:00', 1, 2001, 'IN0401'),
    (2, '상품 불량에 대한 교환 절차를 안내드립니다.', '2025-03-15 10:05:00', '2025-03-15 10:05:00', 2, 2002, 'IN0401'),
    (3, '교환 요청 관련하여 주문 내역을 확인 후 답변 드리겠습니다.', '2025-03-17 09:00:00', '2025-03-17 09:00:00', 3, 2003, 'IN0401');

-- 31. 고객지원첨부파일 (SupportAttachment)
INSERT INTO SupportAttachment (attachment_id, file_url, file_name, file_size, created_at, inquiry_id)
VALUES
    (1001, 'https://test.com/files/inquiry1_file1.jpg', '', 5000, '2025-03-10 10:12:00', 1),
    (1002, 'https://test.com/files/inquiry2_attachment.pdf', '', 5000, '2025-03-11 14:25:00', 2),
    (1003, 'https://test.com/files/inquiry3_img.png', '', 50000, '2025-03-13 09:15:00', 3);

-- 32. 재고 관리 (Stock)
INSERT INTO Stock (stock_id, quantity, created_at, updated_at, last_restock_date, product_id, store_id, stock_status, first_stock_in_date, last_stock_out_date, min_stock_level, max_stock_level, safety_stock_level, stock_in_quantity, stock_out_quantity, is_restock_required)
VALUES
    (1, 0, '2024-01-14 12:00:00', '2024-01-14 12:00:00', '2024-01-10 12:00:00', 1, 1, 'ST0302', '2024-01-10 12:00:00', '2024-01-14 12:00:00', 20, 50, 10, 0, 10, FALSE),
    (2, 25, '2025-03-13 12:00:00', '2025-03-13 12:00:00', NULL, 5, 1, 'ST0301', '2024-04-10 12:00:00', '2025-03-13 12:00:00', 20, 50, 10, 10, 0, FALSE),
    (3, 0, '2024-02-14 12:00:00', '2024-02-14 12:00:00', NULL, 3, 3, 'ST0302', '2024-02-08 12:00:00', '2024-02-14 12:00:00', 0, 0, 0, 0, 10, FALSE);


INSERT INTO Logistics (logistics_id, created_at, updated_at, from_store_id, to_store_id, delivery_status, approved_status, sum_quantity, total_amount, from_store_name, from_store_location, to_store_name, to_store_location)
VALUES
    (1, '2025-01-13 12:00:00', '2025-01-13 12:00:00', 1, 2, 'SH0103', 'ST0702', 10, 8750000, '명동 지점', '서울특별시 중구 남대문로2가 9-1', '여의도 지점', '서울특별시 영등포구 여의도동 23 IFC몰 L1'),
    (2, '2025-03-13 12:00:00', '2025-03-13 12:00:00', 1, 4, 'SH0101', 'ST0702', 10, 3500000, '명동 지점', '서울특별시 중구 남대문로2가 9-1', '잠실 지점', '서울특별시 송파구 신천동 29 롯데월드몰 1층'),
    (3, '2025-03-13 12:00:00', '2025-03-13 12:00:00', 3, 2, 'SH0101', 'ST0702', 10, 5000000, '청계전 지점', '서울특별시 중구 황학동 2545 지하2층', '여의도 지점', '서울특별시 영등포구 여의도동 23 IFC몰 L1');
-- 34. 물류 이동 상세 (LogisticsDetail)
INSERT INTO LogisticsDetail (detail_id, logistics_id, created_at, product_id, updated_at, transportation_method, departure_store_manager_id, arrival_store_manager_id, departure_time, estimated_arrival_time, actual_arrival_time, amount)
VALUES
    (1, 1, '2025-01-13 12:00:00', 5, '2025-01-15 15:30:00', 'ST0803', 1, 2, '2025-01-13 14:00:00', '2025-01-13 15:00:00', '2025-01-13 15:30:00', 1750000),
    (2, 1, '2025-01-13 12:00:00', 6, '2025-01-16 15:30:00', 'ST0803', 1, 2, '2025-01-13 14:00:00', '2025-01-14 15:00:00', '2025-01-14 15:30:00', 6000000),
    (3, 1, '2025-01-13 12:00:00', 7, '2025-01-17 15:30:00', 'ST0803', 1, 2, '2025-01-13 14:00:00', '2025-01-15 15:00:00', '2025-01-15 15:30:00', 1000000),
    (4, 2, '2025-03-13 12:00:00', 5, '2025-03-15 12:00:00', 'ST0801', 2, 3, '2025-03-15 12:00:00', '2025-03-15 17:00:00', NULL, 3500000),
    (5, 3, '2025-03-13 12:00:00', 7, '2025-03-15 12:00:00', 'ST0801', 3, 1, '2025-03-15 12:00:00', '2025-03-16 17:00:00', NULL, 5000000);

-- 35. 재고 변경 이력 (StockChangeHistory)
INSERT INTO StockChangeHistory (stock_change_id, stock_id, product_id, store_id, store_name, stock_in_quantity, stock_in_reason, stock_out_quantity, stock_out_reason, previous_stock, new_stock, changed_at)
VALUES
    (1, 1, 1, 1, '명동 지점', 0, NULL, 10, 'ST0501', 10, 0, '2024-01-14 12:00:00'),
    (2, 2, 5, 1, '명동 지점', 10, 'ST0402', 0, 'ST0501', 15, 25, '2025-03-13 12:00:00'),
    (3, 3, 3, 3, '가로수길 지점', 0, NULL, 10, 'ST0502', 10, 0, '2024-02-14 12:00:00');