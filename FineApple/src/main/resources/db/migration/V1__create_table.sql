-- 테이블 생성
CREATE TABLE ProductOption
(
    option_id        BIGINT         NOT NULL AUTO_INCREMENT,
    product_id       BIGINT         NOT NULL,
    option_name      VARCHAR(50)    NOT NULL,
    option_value     VARCHAR(100)   NOT NULL,
    additional_price DECIMAL(13, 2) NOT NULL DEFAULT 0,
    PRIMARY KEY (option_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE CommonCode
(
    code_id     BIGINT      NOT NULL,
    code        VARCHAR(50) NOT NULL,
    code_name   VARCHAR(50) NOT NULL,
    description TEXT        NULL,
    PRIMARY KEY (code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE SupportAttachment
(
    attachment_id BIGINT       NOT NULL AUTO_INCREMENT,
    file_url      VARCHAR(255) NOT NULL,
    file_name     VARCHAR(255) NOT NULL,
    file_size     INT          NOT NULL CHECK (file_size >= 0),
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    inquiry_id    BIGINT       NOT NULL,
    PRIMARY KEY (attachment_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Refund
(
    refund_id           BIGINT         NOT NULL AUTO_INCREMENT,
    refund_status       VARCHAR(50)    NOT NULL,
    payment_id          BIGINT         NOT NULL,
    requested_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    approved_at         TIMESTAMP      NULL     DEFAULT NULL,
    refund_total_amount DECIMAL(10, 2) NOT NULL,
    refund_reason       VARCHAR(255)   NULL     DEFAULT NULL,
    pg_response_code    VARCHAR(255)   NULL,
    refund_fail_reason  TEXT           NULL,
    CHECK (approved_at IS NULL OR requested_at <= approved_at),
    PRIMARY KEY (refund_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Cart
(
    cart_id    BIGINT    NOT NULL AUTO_INCREMENT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_id    BIGINT    NULL,
    guest_id   BIGINT    NULL,
    PRIMARY KEY (cart_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Guest
(
    guest_id   BIGINT       NOT NULL AUTO_INCREMENT,
    order_id   BIGINT       NOT NULL,
    name       VARCHAR(50)  NULL,
    address    VARCHAR(255) NULL,
    tel        VARCHAR(20)  NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NULL     DEFAULT NULL,
    PRIMARY KEY (guest_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE LoginHistory
(
    loginhistory_id BIGINT       NOT NULL AUTO_INCREMENT,
    user_id         BIGINT       NOT NULL,
    login_time      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address      VARCHAR(50)  NOT NULL,
    device_info     VARCHAR(255) NOT NULL,
    login_status    VARCHAR(50)  NOT NULL,
    PRIMARY KEY (loginhistory_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE OrderStatus
(
    orderstatus_id     BIGINT      NOT NULL AUTO_INCREMENT,
    updated_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    order_id           BIGINT      NOT NULL,
    orderstatus_status VARCHAR(50) NOT NULL,
    payment_status     VARCHAR(50) NOT NULL,
    PRIMARY KEY (orderstatus_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Department
(
    department_id VARCHAR(20) NOT NULL,
    name          VARCHAR(20) NOT NULL,
    PRIMARY KEY (department_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE User
(
    user_id     BIGINT       NOT NULL AUTO_INCREMENT,
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    create_date TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    is_active   BOOLEAN      NOT NULL,
    PRIMARY KEY (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Store
(
    store_id             BIGINT         NOT NULL AUTO_INCREMENT,
    name                 VARCHAR(255)   NOT NULL,
    location             VARCHAR(255)   NOT NULL,
    updated_at           TIMESTAMP               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_at           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    store_number         VARCHAR(20)    NULL,
    store_type           VARCHAR(50)    NOT NULL,
    operation_start_date TIMESTAMP      NOT NULL,
    operation_end_date   TIMESTAMP      NULL     DEFAULT NULL,
    opening_time         TIME           NOT NULL DEFAULT '09:00:00',
    closing_time         TIME           NOT NULL DEFAULT '18:00:00',
    postal_code          VARCHAR(20)    NOT NULL,
    monthly_revenue      DECIMAL(13, 2) NOT NULL DEFAULT 0,
    store_status         VARCHAR(50)    NOT NULL,
    manager_id           BIGINT         NOT NULL,
    PRIMARY KEY (store_id),
    UNIQUE (manager_id),
    CHECK (operation_end_date IS NULL OR operation_start_date < operation_end_date),
    CHECK (opening_time < closing_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE SupportInquiry
(
    inquiry_id          BIGINT       NOT NULL AUTO_INCREMENT,
    subject             VARCHAR(255) NOT NULL,
    content             TEXT         NOT NULL,
    created_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_info           BIGINT       NOT NULL,
    inquiry_status      VARCHAR(50)  NOT NULL,
    inquiry_type        VARCHAR(50)  NOT NULL,
    email               VARCHAR(255) NOT NULL,
    name                VARCHAR(50)  NOT NULL,
    company_name        VARCHAR(255)          DEFAULT NULL,
    country_region      VARCHAR(50)           DEFAULT NULL,
    assigned_to         VARCHAR(50)  NULL,
    resolved_by         VARCHAR(50)  NULL,
    acceptprivacypolicy BOOLEAN      NOT NULL,
    response_due_date   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    prioritycode        VARCHAR(50)  NOT NULL,
    order_id            BIGINT                DEFAULT NULL,
    orderitemdetail_id  BIGINT                DEFAULT NULL,
    parent_inquiry_id   BIGINT                DEFAULT NULL,
    language_code       VARCHAR(50)  NOT NULL,
    PRIMARY KEY (inquiry_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE SupportResponse
(
    answer_id     BIGINT      NOT NULL AUTO_INCREMENT,
    content       TEXT        NOT NULL,
    created_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    inquiry_id    BIGINT      NOT NULL,
    user_info     BIGINT      NOT NULL,
    language_code VARCHAR(50) NOT NULL,
    PRIMARY KEY (answer_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE UserCoupon
(
    user_coupon_id BIGINT    NOT NULL AUTO_INCREMENT,
    coupon_id      BIGINT    NOT NULL,
    user_id        BIGINT    NOT NULL,
    is_used        BOOLEAN   NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    used_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_at         TIMESTAMP NOT NULL,
    CHECK (used_at IS NULL OR used_at > created_at),
    CHECK (end_at > created_at),
    CHECK (used_at IS NULL OR used_at < end_at),
    PRIMARY KEY (user_coupon_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE UserProfileHistory
(
    profilehistory_id BIGINT       NOT NULL AUTO_INCREMENT,
    user_id           BIGINT       NOT NULL,
    field_changed     VARCHAR(100) NOT NULL,
    previous_value    TEXT         NOT NULL,
    new_value         TEXT         NOT NULL,
    changed_by        BIGINT       NOT NULL,
    changed_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (profilehistory_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Payment
(
    payment_id       BIGINT         NOT NULL AUTO_INCREMENT,
    order_id         BIGINT         NOT NULL,
    payment_status   VARCHAR(50)    NOT NULL,
    requested_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount     DECIMAL(10, 2) NOT NULL CHECK (total_amount > 0),
    pg_approval_code VARCHAR(255)   NULL,
    currency         VARCHAR(10)    NULL,
    paid_at          TIMESTAMP      NULL     DEFAULT NULL,
    cancelled_at     TIMESTAMP      NULL     DEFAULT NULL,
    receipt_url      VARCHAR(255)   NULL,
    card_name        VARCHAR(50)    NULL,
    card_quota       INT            NULL,
    payment_method   VARCHAR(50)    NULL,
    failure_code     VARCHAR(255)   NULL,
    pg_unique_id     VARCHAR(255)   NULL,
    pg_type          VARCHAR(255)   NULL,
    CHECK (requested_at <= paid_at OR paid_at IS NULL),
    CHECK (requested_at <= cancelled_at OR cancelled_at IS NULL),
    CHECK (paid_at <= cancelled_at OR cancelled_at IS NULL),
    PRIMARY KEY (payment_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE PaymentDetail
(
    detail_id          BIGINT         NOT NULL AUTO_INCREMENT,
    payment_id         BIGINT         NOT NULL,
    orderitemdetail_id BIGINT         NOT NULL,
    product_name       VARCHAR(255)   NULL,
    paid_amount        DECIMAL(10, 2) NOT NULL,
    quantity           INT            NOT NULL,
    cancelled_quantity INT            NULL,
    cancelled_amount   DECIMAL(10, 2) NULL,
    fail_reason        TEXT           NULL,
    cancelled_at       TIMESTAMP      NULL     DEFAULT NULL,
    created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (detail_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE UserInfo
(
    user_info   BIGINT      NOT NULL AUTO_INCREMENT,
    name        VARCHAR(50) NOT NULL,
    birth       DATE        NOT NULL,
    tel         VARCHAR(20) NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    user_id     BIGINT      NOT NULL,
    user_role   VARCHAR(50) NOT NULL,
    user_status VARCHAR(50) NOT NULL,
    CHECK (updated_at IS NULL OR created_at <= updated_at),
    PRIMARY KEY (user_info)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE EmployeeInfo
(
    emp_info          BIGINT         NOT NULL AUTO_INCREMENT,
    employee_id       BIGINT         NOT NULL,
    department_id     VARCHAR(20)    NOT NULL,
    title             VARCHAR(50)    NOT NULL,
    salary            DECIMAL(10, 2) NOT NULL,
    vacation_count    INT            NOT NULL,
    required_training BOOLEAN        NOT NULL,
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP      NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    store_id          BIGINT         NOT NULL,
    employee_status   VARCHAR(50)    NOT NULL,
    PRIMARY KEY (emp_info)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Address
(
    delivery_id     BIGINT       NOT NULL AUTO_INCREMENT,
    name            VARCHAR(20)  NOT NULL,
    tel             VARCHAR(20)  NOT NULL,
    country         VARCHAR(50)  NOT NULL,
    city            VARCHAR(100) NOT NULL,
    region          VARCHAR(100) NOT NULL,
    post_num        VARCHAR(20)  NOT NULL,
    road_num        VARCHAR(255) NOT NULL,
    address         VARCHAR(255) NOT NULL,
    is_default      BOOLEAN      NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    user_info       BIGINT       NULL,
    guest_id        BIGINT       NULL,
    delivery_status VARCHAR(50)  NOT NULL,
    CHECK (updated_at IS NULL OR created_at <= updated_at),
    PRIMARY KEY (delivery_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE RefundDetail
(
    RefundTransaction BIGINT         NOT NULL AUTO_INCREMENT,
    refund_id         BIGINT         NOT NULL,
    issue_status      VARCHAR(20)    NOT NULL,
    approved_time     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    approved_number   VARCHAR(12)    NOT NULL,
    request_price     DECIMAL(10, 2) NOT NULL,
    remaining_price   DECIMAL(10, 2) NOT NULL,
    refund_bank_name  VARCHAR(50)    NULL,
    refund_bank_code  VARCHAR(5)     NULL,
    PRIMARY KEY (RefundTransaction)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Employee
(
    employee_id BIGINT      NOT NULL AUTO_INCREMENT,
    name        VARCHAR(25) NOT NULL,
    birth       DATE        NOT NULL,
    tel         VARCHAR(20) NOT NULL,
    email       VARCHAR(50) NOT NULL,
    address     VARCHAR(50) NOT NULL,
    start_date  DATE        NOT NULL,
    created_at  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (employee_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Category
(
    category_id       BIGINT       NOT NULL AUTO_INCREMENT,
    name              VARCHAR(255) NOT NULL,
    path              VARCHAR(50) NOT NULL,
    description       TEXT NULL,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    parent_id         BIGINT       NULL     DEFAULT NULL,
    visibility_status VARCHAR(50)  NOT NULL,
    PRIMARY KEY (category_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



CREATE TABLE Coupon
(
    coupon_id        BIGINT         NOT NULL AUTO_INCREMENT,
    coupon_code      VARCHAR(20)    NOT NULL UNIQUE,
    coupon_name      VARCHAR(50)    NOT NULL,
    discount_amount  DECIMAL(10, 2) NULL,
    min_order_amount DECIMAL(10, 2) NULL,
    start_date       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    end_date         TIMESTAMP      NOT NULL,
    is_active        BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    use_valid        INT            NOT NULL DEFAULT 0,
    PRIMARY KEY (coupon_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Product
(
    product_id          BIGINT         NOT NULL AUTO_INCREMENT,
    name                VARCHAR(255)   NOT NULL,
    description         TEXT           NULL,
    price               DECIMAL(13, 2) NOT NULL,
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    category_id         BIGINT         NOT NULL,
    is_active           BOOLEAN        NOT NULL,
    target_release_date TIMESTAMP      NOT NULL,
    actual_release_date TIMESTAMP      NULL,
    sale_start_date     TIMESTAMP      NULL,
    sale_stop_date      TIMESTAMP      NULL,
    sale_restart_date   TIMESTAMP      NULL,
    sale_end_date       TIMESTAMP      NULL,
    sale_status         VARCHAR(50)    NOT NULL,
    PRIMARY KEY (product_id),
    CHECK (actual_release_date IS NULL OR actual_release_date >= target_release_date),
    CHECK (sale_stop_date IS NULL OR sale_start_date IS NULL OR sale_stop_date >= sale_start_date),
    CHECK (sale_restart_date IS NULL OR sale_stop_date IS NULL OR sale_restart_date >= sale_stop_date),
    CHECK (sale_end_date IS NULL OR sale_restart_date IS NULL OR sale_end_date >= sale_restart_date)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Orders
(
    order_id       BIGINT         NOT NULL AUTO_INCREMENT,
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    order_code     BIGINT         NOT NULL,
    total_price    DECIMAL(13, 2) NOT NULL CHECK (total_price >= 0),
    discount_price DECIMAL(13, 2) NULL CHECK (discount_price >= 0),
    is_cancelled   BOOLEAN        NOT NULL,
    delivery_id    BIGINT         NULL,
    guest_id       BIGINT         NULL,
    user_id        BIGINT         NULL,
    order_status   VARCHAR(50)    NOT NULL,
    payment_method VARCHAR(50)    NULL,
    PRIMARY KEY (order_id),
    CHECK (created_at <= updated_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Stock
(
    stock_id            BIGINT      NOT NULL AUTO_INCREMENT,
    quantity            INT         NOT NULL DEFAULT 0,
    created_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_restock_date   TIMESTAMP   NULL     DEFAULT NULL,
    product_id          BIGINT      NOT NULL,
    store_id            BIGINT      NOT NULL,
    stock_status        VARCHAR(50) NOT NULL,
    first_stock_in_date TIMESTAMP   NOT NULL,
    last_stock_out_date TIMESTAMP   NULL     DEFAULT NULL,
    min_stock_level     INT         NOT NULL,
    max_stock_level     INT         NOT NULL,
    safety_stock_level  INT         NOT NULL,
    stock_in_quantity   INT         NOT NULL,
    stock_out_quantity  INT         NOT NULL,
    is_restock_required BOOLEAN     NOT NULL,
    PRIMARY KEY (stock_id),
    CHECK (quantity >= 0),
    CHECK (min_stock_level >= 0),
    CHECK (max_stock_level >= min_stock_level),
    CHECK (safety_stock_level >= 0 AND safety_stock_level <= min_stock_level),
    CHECK (stock_in_quantity >= 0),
    CHECK (stock_out_quantity >= 0),
    CHECK (last_stock_out_date IS NULL OR last_stock_out_date >= first_stock_in_date),
    CHECK (is_restock_required IN (0, 1))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE CartItem
(
    cart_item_id BIGINT    NOT NULL AUTO_INCREMENT,
    quantity     BIGINT    NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NULL     DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    cart_id      BIGINT    NOT NULL,
    product_id   BIGINT    NOT NULL,
    options      JSON      NULL,
    PRIMARY KEY (cart_item_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE OrderItemDetail
(
    orderitemdetail_id BIGINT         NOT NULL AUTO_INCREMENT,
    name               VARCHAR(255)   NOT NULL,
    quantity           INT            NOT NULL CHECK (quantity > 0),
    price              DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
    created_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    discount_price     DECIMAL(10, 2) NULL CHECK (discount_price >= 0),
    coupon_applied     BOOLEAN        NOT NULL DEFAULT FALSE,
    additional_price   DECIMAL(10, 2) NULL CHECK (additional_price >= 0),
    additional         JSON           NULL,
    order_id           BIGINT         NOT NULL,
    product_id         BIGINT         NOT NULL,
    option_id          BIGINT         NOT NULL,
    item_status        VARCHAR(50)    NOT NULL,
    PRIMARY KEY (orderitemdetail_id),
    CHECK (created_at <= updated_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE OrderItemDetail_History
(
    history_id          BIGINT         NOT NULL AUTO_INCREMENT,
    old_price           DECIMAL(10, 2) NULL CHECK (old_price > 0),
    new_price           DECIMAL(10, 2) NULL CHECK (new_price > 0),
    old_quantity        INT            NULL CHECK (old_quantity >= 0),
    new_quantity        INT            NULL CHECK (new_quantity >= 0),
    change_reason       VARCHAR(255)   NULL,
    changed_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    changed_by          BIGINT         NULL,
    orderitemdetail_id  BIGINT         NOT NULL,
    item_history_status VARCHAR(50)    NOT NULL,
    PRIMARY KEY (history_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Product_Image
(
    image_id     BIGINT       NOT NULL AUTO_INCREMENT,
    image_url    VARCHAR(500) NOT NULL,
    created_id   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_id   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    product_id   BIGINT       NOT NULL,
    alt_text     VARCHAR(500) NULL,
    product_main BOOLEAN      NOT NULL,
    PRIMARY KEY (image_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Shipment
(
    shipment_id             BIGINT         NOT NULL AUTO_INCREMENT,
    tracking_number         VARCHAR(100)   NOT NULL,
    tracking_url            VARCHAR(255)   NOT NULL,
    estimated_delivery_date TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    dispatched_at           TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    delivered_at            TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at              TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    order_id                BIGINT         NOT NULL,
    delivery_status         VARCHAR(50)    NOT NULL,
    store_id                BIGINT         NOT NULL,
    delivery_id             BIGINT         NOT NULL,
    shipping_cost           DECIMAL(10, 2) NOT NULL CHECK (shipping_cost >= 0),
    courier_company         VARCHAR(100)   NOT NULL,
    delay_reason            TEXT                    DEFAULT NULL,
    delivery_agent_name     VARCHAR(50)    NOT NULL,
    delivery_agent_contact  VARCHAR(50)    NOT NULL,
    post_num                VARCHAR(20)    NOT NULL,
    city                    VARCHAR(100)   NOT NULL,
    region                  VARCHAR(100)   NOT NULL,
    road_num                VARCHAR(255)   NOT NULL,
    address                 VARCHAR(255)   NOT NULL,
    store_name              VARCHAR(100)   NOT NULL,
    PRIMARY KEY (shipment_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;



CREATE TABLE Discount
(
    discount_id        BIGINT         NOT NULL AUTO_INCREMENT,
    discount_name      VARCHAR(50)    NOT NULL,
    discount_code      VARCHAR(50)    NOT NULL,
    discount_price     DECIMAL(13, 2) NOT NULL CHECK (discount_price >= 0),
    discount_start_day TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    discount_end_day   TIMESTAMP      NOT NULL,
    minimum_price      INT            NULL CHECK (minimum_price IS NULL OR minimum_price > 0),
    is_active          BOOLEAN        NOT NULL,
    order_id           BIGINT         NOT NULL,
    product_id         BIGINT         NOT NULL,
    discount_type      VARCHAR(50)    NOT NULL,
    PRIMARY KEY (discount_id),
    CHECK (discount_start_day <= discount_end_day)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE StockChangeHistory
(
    stock_change_id    BIGINT      NOT NULL AUTO_INCREMENT,
    stock_id           BIGINT      NOT NULL,
    product_id         BIGINT      NOT NULL,
    store_id           BIGINT      NOT NULL,
    store_name         VARCHAR(50) NOT NULL,
    stock_in_quantity  INT         NOT NULL,
    stock_in_reason    VARCHAR(50) NULL,
    stock_out_quantity INT         NOT NULL,
    stock_out_reason   VARCHAR(50) NULL,
    previous_stock     INT         NOT NULL,
    new_stock          INT         NOT NULL,
    changed_at         TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (stock_change_id),
    CHECK (stock_in_quantity >= 0),
    CHECK (stock_out_quantity >= 0),
    CHECK (previous_stock >= 0),
    CHECK (new_stock >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;


CREATE TABLE Logistics
(
    logistics_id        BIGINT         NOT NULL AUTO_INCREMENT,
    created_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    from_store_id       BIGINT         NOT NULL,
    to_store_id         BIGINT         NOT NULL,
    delivery_status     VARCHAR(50)    NOT NULL,
    approved_status     VARCHAR(50)    NOT NULL,
    sum_quantity        INT            NOT NULL,
    total_amount        DECIMAL(13, 2) NOT NULL,
    from_store_name     VARCHAR(50)    NOT NULL,
    from_store_location VARCHAR(255)   NOT NULL,
    to_store_name       VARCHAR(50)    NOT NULL,
    to_store_location   VARCHAR(255)   NOT NULL,
    PRIMARY KEY (logistics_id),
    CHECK (sum_quantity >= 0),
    CHECK (total_amount >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE LogisticsDetail
(
    detail_id                  BIGINT         NOT NULL AUTO_INCREMENT,
    quantity                   INT            NOT NULL DEFAULT 0,
    created_at                 TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    product_id                 BIGINT         NOT NULL,
    updated_at                 TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    departure_time             TIMESTAMP      NOT NULL,
    estimated_arrival_time     TIMESTAMP      NOT NULL,
    actual_arrival_time        TIMESTAMP      NULL     DEFAULT NULL,
    amount                     DECIMAL(13, 2) NOT NULL,
    transportation_method      VARCHAR(50)    NOT NULL,
    departure_store_manager_id BIGINT         NOT NULL,
    arrival_store_manager_id   BIGINT         NOT NULL,
    logistics_id               BIGINT         NOT NULL,
    PRIMARY KEY (detail_id),
    CHECK (quantity >= 0),
    CHECK (amount >= 0),
    CHECK (estimated_arrival_time >= departure_time),
    CHECK (actual_arrival_time IS NULL OR actual_arrival_time >= departure_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE predicted_values
(
    id         SERIAL PRIMARY KEY,
    date       DATE        NOT NULL,
    group_key  VARCHAR(20) NOT NULL,
    value      BIGINT      NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE Message (
                         message_id   BIGINT       NOT NULL AUTO_INCREMENT,
                         user_id      BIGINT       NULL,
                         role         VARCHAR(20)  NOT NULL,  -- 유저랑 ai
                         content      TEXT         NOT NULL,
                         created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         intent VARCHAR(50) DEFAULT NULL,
                         PRIMARY KEY (message_id),
                         FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

-- ProductOption 테이블
ALTER TABLE ProductOption
    ADD CONSTRAINT FK_ProductOption_Product
        FOREIGN KEY (product_id) REFERENCES Product (product_id);

-- SupportAttachment 테이블
ALTER TABLE SupportAttachment
    ADD CONSTRAINT FK_SupportAttachment_SupportInquiry
        FOREIGN KEY (inquiry_id) REFERENCES SupportInquiry (inquiry_id);

-- Refund 테이블
ALTER TABLE Refund
    ADD CONSTRAINT FK_Refund_Payment
        FOREIGN KEY (payment_id) REFERENCES Payment (payment_id);

-- CartItem 테이블
ALTER TABLE CartItem
    ADD CONSTRAINT FK_CartItem_Cart
        FOREIGN KEY (cart_id) REFERENCES Cart (cart_id) ON DELETE CASCADE;
ALTER TABLE CartItem
    ADD CONSTRAINT FK_CartItem_Product
        FOREIGN KEY (product_id) REFERENCES Product (product_id) ON DELETE CASCADE;

ALTER TABLE SupportInquiry
    ADD CONSTRAINT FK_SupportInquiry_Order_id
        FOREIGN KEY (order_id) REFERENCES Orders (order_id);
ALTER TABLE SupportInquiry
    ADD CONSTRAINT FK_SupportInquiry_Orderitemdetail_id
        FOREIGN KEY (orderitemdetail_id) REFERENCES OrderItemDetail (orderitemdetail_id);


ALTER TABLE UserInfo
    ADD CONSTRAINT FK_UserInfo_User FOREIGN KEY (user_id) REFERENCES User (user_id);

-- UserCoupon 테이블
ALTER TABLE UserCoupon
    ADD CONSTRAINT FK_UserCoupon_Coupon
        FOREIGN KEY (coupon_id) REFERENCES Coupon (coupon_id);

ALTER TABLE UserCoupon
    ADD CONSTRAINT FK_UserCoupon_User
        FOREIGN KEY (user_id) REFERENCES User (user_id);

-- OrderStatus 테이블
ALTER TABLE OrderStatus
    ADD CONSTRAINT FK_OrderStatus_Order
        FOREIGN KEY (order_id) REFERENCES Orders (order_id);

-- SupportResponse 테이블
ALTER TABLE SupportResponse
    ADD CONSTRAINT FK_SupportResponse_Inquiry
        FOREIGN KEY (inquiry_id) REFERENCES SupportInquiry (inquiry_id) ON DELETE CASCADE;

-- Payment 테이블
ALTER TABLE Payment
    ADD CONSTRAINT FK_Payment_Order
        FOREIGN KEY (order_id) REFERENCES Orders (order_id) ON DELETE CASCADE;

-- PaymentDetail 테이블
ALTER TABLE PaymentDetail
    ADD CONSTRAINT FK_PaymentDetail_Payment
        FOREIGN KEY (payment_id) REFERENCES Payment (payment_id) ON DELETE CASCADE;

-- Address 테이블
ALTER TABLE Address
    ADD CONSTRAINT FK_Address_UserInfo
        FOREIGN KEY (user_info) REFERENCES UserInfo (user_info);

ALTER TABLE Address
    ADD CONSTRAINT FK_Address_Guest
        FOREIGN KEY (guest_id) REFERENCES Guest (guest_id);

-- Product 테이블
ALTER TABLE Product
    ADD CONSTRAINT FK_Product_Category
        FOREIGN KEY (category_id) REFERENCES Category (category_id);

ALTER TABLE Category
    ADD CONSTRAINT FK_Category_Category
        FOREIGN KEY (parent_id) REFERENCES Category (category_id) ON DELETE CASCADE;

ALTER TABLE SupportInquiry
    ADD CONSTRAINT FK_SupportInquiry_SupportInquiry
        FOREIGN KEY (parent_inquiry_id) REFERENCES SupportInquiry (inquiry_id) ON DELETE CASCADE;


-- Product 테이블
ALTER TABLE Product
    ADD CONSTRAINT FK_Product_CommonCode FOREIGN KEY (sale_status) REFERENCES CommonCode (code);
--  Order 테이블
ALTER TABLE Orders
    ADD CONSTRAINT FK_Order_Delivery
        FOREIGN KEY (delivery_id) REFERENCES Address (delivery_id);

ALTER TABLE Orders
    ADD CONSTRAINT FK_Order_User
        FOREIGN KEY (user_id) REFERENCES User (user_id) ON DELETE CASCADE;

ALTER TABLE Orders
    ADD CONSTRAINT FK_Order_Guest
        FOREIGN KEY (guest_id) REFERENCES Guest (guest_id) ON DELETE CASCADE;

-- EmployeeInfo 테이블
ALTER TABLE EmployeeInfo
    ADD CONSTRAINT FK_EmployeeInfo_Employee
        FOREIGN KEY (employee_id) REFERENCES Employee (employee_id);

ALTER TABLE EmployeeInfo
    ADD CONSTRAINT FK_EmployeeInfo_Department
        FOREIGN KEY (department_id) REFERENCES Department (department_id);

ALTER TABLE EmployeeInfo
    ADD CONSTRAINT FK_EmployeeInfo_Store
        FOREIGN KEY (store_id) REFERENCES Store (store_id);

-- RefundDetail 테이블
ALTER TABLE RefundDetail
    ADD CONSTRAINT FK_RefundDetail_Refund FOREIGN KEY (refund_id) REFERENCES Refund (refund_id);

-- UserProfileHistory 테이블
ALTER TABLE UserProfileHistory
    ADD CONSTRAINT FK_UserProfileHistory_User
        FOREIGN KEY (user_id) REFERENCES User (user_id);
ALTER TABLE UserProfileHistory
    ADD CONSTRAINT FK_UserProfileHistory_User2
        FOREIGN KEY (changed_by) REFERENCES User (user_id);


    -- Cart 테이블
    ALTER TABLE Cart ADD CONSTRAINT FK_Cart_User FOREIGN KEY (user_id) REFERENCES User(user_id);
--    ALTER TABLE Cart ADD CONSTRAINT FK_Cart_Guest FOREIGN KEY (guest_id) REFERENCES Guest(guest_id);

ALTER TABLE Logistics
    ADD CONSTRAINT FK_Logistics_From_Store FOREIGN KEY (from_store_id) REFERENCES Store (store_id);
ALTER TABLE Logistics
    ADD CONSTRAINT FK_Logistics_To_Store FOREIGN KEY (to_store_id) REFERENCES Store (store_id);

ALTER TABLE SupportInquiry
    ADD CONSTRAINT FK_SupportInquiry_language_code_CommonCode FOREIGN KEY (language_code) REFERENCES CommonCode (code);
ALTER TABLE SupportResponse
    ADD CONSTRAINT FK_SupportResponse_language_code_CommonCode FOREIGN KEY (language_code) REFERENCES CommonCode (code);


-- OrderItemDetail 테이블
ALTER TABLE OrderItemDetail
    ADD CONSTRAINT FK_OrderItemDetail_Order FOREIGN KEY (order_id) REFERENCES Orders (order_id);
ALTER TABLE OrderItemDetail
    ADD CONSTRAINT FK_OrderItemDetail_Product FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE OrderItemDetail
    ADD CONSTRAINT FK_OrderItemDetail_ProductOption FOREIGN KEY (option_id) REFERENCES ProductOption (option_id);
ALTER TABLE OrderItemDetail
    ADD CONSTRAINT FK_OrderItemDetail_Common_Code
        FOREIGN KEY (item_status) REFERENCES CommonCode (code);

-- OrderItemDetail_History 테이블
ALTER TABLE OrderItemDetail_History
    ADD CONSTRAINT FK_OrderItemDetail_History_OrderItemDetail FOREIGN KEY (orderitemdetail_id) REFERENCES OrderItemDetail (orderitemdetail_id);
ALTER TABLE OrderItemDetail_History
    ADD CONSTRAINT FK_OrderItemDetail_HIstory_Common_Code
        FOREIGN KEY (item_history_status) REFERENCES CommonCode (code);

-- Product_Image 테이블
ALTER TABLE Product_Image
    ADD CONSTRAINT FK_Product_Image_Product FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE Store
    ADD CONSTRAINT FK_Store_Employee FOREIGN KEY (manager_id) REFERENCES Employee (employee_id);
ALTER TABLE Store
    ADD CONSTRAINT FK_Store_CommonCode FOREIGN KEY (store_type) REFERENCES CommonCode (code);
ALTER TABLE Store
    ADD CONSTRAINT FK_Store_CommonCode2 FOREIGN KEY (store_status) REFERENCES CommonCode (code);


-- LogisticsDetail 테이블
ALTER TABLE LogisticsDetail
    ADD CONSTRAINT FK_LogisticsDetail_Product FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE LogisticsDetail
    ADD CONSTRAINT FK_LogisticsDetail_CommonCode FOREIGN KEY (transportation_method) REFERENCES CommonCode (code);

ALTER TABLE LogisticsDetail
    ADD CONSTRAINT FK_LogisticsDetail_Logistics FOREIGN KEY (departure_store_manager_id) REFERENCES Store (manager_id);
ALTER TABLE LogisticsDetail
    ADD CONSTRAINT FK_LogisticsDetail_Store FOREIGN KEY (arrival_store_manager_id) REFERENCES Store (manager_id);


-- Logistics 테이블
ALTER TABLE Logistics
    ADD CONSTRAINT FK_Logistics_Store FOREIGN KEY (from_store_id) REFERENCES Store (store_id);
ALTER TABLE Logistics
    ADD CONSTRAINT FK_Logistics_Store2 FOREIGN KEY (to_store_id) REFERENCES Store (store_id);


-- Shipment 테이블
ALTER TABLE Shipment
    ADD CONSTRAINT FK_Shipment_Order FOREIGN KEY (order_id) REFERENCES Orders (order_id);
ALTER TABLE Shipment
    ADD CONSTRAINT FK_Shipment_Store FOREIGN KEY (store_id) REFERENCES Store (store_id);
ALTER TABLE Shipment
    ADD CONSTRAINT FK_Shipment_Address FOREIGN KEY (delivery_id) REFERENCES Address (delivery_id);

-- Discount 테이블
ALTER TABLE Discount
    ADD CONSTRAINT FK_Discount_Order FOREIGN KEY (order_id) REFERENCES Orders (order_id);
ALTER TABLE Discount
    ADD CONSTRAINT FK_Discount_Product FOREIGN KEY (product_id) REFERENCES Product (product_id);

-- Stock 테이블
ALTER TABLE Stock
    ADD CONSTRAINT FK_Stock_Product FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE Stock
    ADD CONSTRAINT FK_Stock_Store FOREIGN KEY (store_id) REFERENCES Store (store_id);

-- LoginHistory 테이블
ALTER TABLE LoginHistory
    ADD CONSTRAINT FK_LoginHistory_User FOREIGN KEY (user_id) REFERENCES User (user_id);

ALTER TABLE PaymentDetail
    ADD CONSTRAINT FK_PaymentDetail_OrderItemDetail FOREIGN KEY (orderitemdetail_id) REFERENCES OrderItemDetail (orderitemdetail_id);

-- 공통코드부분

ALTER TABLE Logistics
    ADD CONSTRAINT FK_Logistics_CommonCode1 FOREIGN KEY (delivery_status) REFERENCES CommonCode (code);
ALTER TABLE Logistics
    ADD CONSTRAINT FK_Logistics_CommonCode2 FOREIGN KEY (approved_status) REFERENCES CommonCode (code);

ALTER TABLE Stock
    ADD CONSTRAINT FK_Stock_CommonCode FOREIGN KEY (stock_status) REFERENCES CommonCode (code);

ALTER TABLE LoginHistory
    ADD CONSTRAINT FK_LoginHistory_CommonCode FOREIGN KEY (login_status) REFERENCES CommonCode (code);

ALTER TABLE StockChangeHistory
    ADD CONSTRAINT FK_StockChangeHistory_Stock FOREIGN KEY (stock_id) REFERENCES Stock (stock_id);
ALTER TABLE StockChangeHistory
    ADD CONSTRAINT FK_StockChangeHistory_Product FOREIGN KEY (product_id) REFERENCES Product (product_id);
ALTER TABLE StockChangeHistory
    ADD CONSTRAINT FK_StockChangeHistory_Store FOREIGN KEY (store_id) REFERENCES Store (store_id);
ALTER TABLE StockChangeHistory
    ADD CONSTRAINT FK_StockChangeHistory_StockInReason FOREIGN KEY (stock_in_reason) REFERENCES CommonCode (code);
ALTER TABLE StockChangeHistory
    ADD CONSTRAINT FK_StockChangeHistory_StockOutReason FOREIGN KEY (stock_out_reason) REFERENCES CommonCode (code);

ALTER TABLE SupportInquiry
    ADD CONSTRAINT FK_SupportInquiry_CommonCode
        FOREIGN KEY (inquiry_status) REFERENCES CommonCode (code);

ALTER TABLE SupportInquiry
    ADD CONSTRAINT FK_SupportInquiry_Type
        FOREIGN KEY (inquiry_type) REFERENCES CommonCode (code);

ALTER TABLE SupportInquiry
    ADD CONSTRAINT FK_SupportInquiry_Priority
        FOREIGN KEY (prioritycode) REFERENCES CommonCode (code);

ALTER TABLE Logistics
    ADD CONSTRAINT FK_Logistics_DeliveryStatus
        FOREIGN KEY (delivery_status) REFERENCES CommonCode (code);

ALTER TABLE Orders
    ADD CONSTRAINT FK_Orders_CommonCode FOREIGN KEY (order_status) REFERENCES CommonCode (code);

ALTER TABLE Logistics
    ADD CONSTRAINT FK_Logistics_ApprovedStatus
        FOREIGN KEY (approved_status) REFERENCES CommonCode (code);

ALTER TABLE Shipment
    ADD CONSTRAINT FK_Shipment_Delivery_status
        FOREIGN KEY (delivery_status) REFERENCES CommonCode (code);

ALTER TABLE Refund
    ADD CONSTRAINT FK_Refund_Status
        FOREIGN KEY (refund_status) REFERENCES CommonCode (code);

ALTER TABLE Orders
    ADD CONSTRAINT FK_Orders_Status
        FOREIGN KEY (payment_method) REFERENCES CommonCode (code);

ALTER TABLE LoginHistory
    ADD CONSTRAINT FK_LoginHistory_Status
        FOREIGN KEY (login_status) REFERENCES CommonCode (code);

ALTER TABLE UserInfo
    ADD CONSTRAINT FK_UserInfo_Status
        FOREIGN KEY (user_status) REFERENCES CommonCode (code);

ALTER TABLE UserInfo
    ADD CONSTRAINT FK_UserInfo_Role
        FOREIGN KEY (user_role) REFERENCES CommonCode (code);

ALTER TABLE EmployeeInfo
    ADD CONSTRAINT FK_EmployeeInfo_Status
        FOREIGN KEY (employee_status) REFERENCES CommonCode (code);

ALTER TABLE OrderStatus
    ADD CONSTRAINT FK_OrderStatus_Status
        FOREIGN KEY (orderstatus_status) REFERENCES CommonCode (code);

ALTER TABLE OrderStatus
    ADD CONSTRAINT FK_OrderStatus_Payment
        FOREIGN KEY (payment_status) REFERENCES CommonCode (code);

ALTER TABLE Address
    ADD CONSTRAINT FK_Address_DeliveryStatus
        FOREIGN KEY (delivery_status) REFERENCES CommonCode (code);

ALTER TABLE Payment
    ADD CONSTRAINT FK_Payment_Status
        FOREIGN KEY (payment_status) REFERENCES CommonCode (code);

ALTER TABLE Discount
    ADD CONSTRAINT FK_Discount_Type
        FOREIGN KEY (discount_type) REFERENCES CommonCode (code);

