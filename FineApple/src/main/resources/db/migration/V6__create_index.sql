CREATE INDEX idx_orders_covering_subquery
    ON Orders (
               order_status,
               is_cancelled,
               created_at,
               order_code,
               order_id
        );

CREATE INDEX idx_orders_created_id
    ON Orders (created_at DESC, order_id);

CREATE INDEX idx_userinfo_tel ON UserInfo(tel);
CREATE INDEX idx_guest_tel ON Guest(tel);