package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class OrderStatus {
    private Long orderStatusId;
    private Long orderId;
    private String orderStatus;
    private String paymentStatus;
    private LocalDateTime updatedAt;

    @Builder
    public OrderStatus(Long orderStatusId, Long orderId, String orderStatus, String paymentStatus, LocalDateTime updatedAt) {
        this.orderStatusId = orderStatusId;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.updatedAt = updatedAt;
    }
}
