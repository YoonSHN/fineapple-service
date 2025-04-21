package com.fineapple.domain.order_payment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Orders {
    private Long orderId;
    private Long orderCode;
    private Long guestId;
    private Long userId;
    private Long deliveryId;
    private BigDecimal totalPrice;
    private BigDecimal discountPrice;
    private Boolean isCancelled;
    private String orderStatus;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Orders(Long orderId, Long orderCode, Long guestId, Long userId, Long deliveryId, BigDecimal totalPrice,
                  BigDecimal discountPrice, Boolean isCancelled, String orderStatus,
                  String paymentMethod, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.guestId = guestId;
        this.userId = userId;
        this.deliveryId = deliveryId;
        this.totalPrice = totalPrice;
        this.discountPrice = discountPrice;
        this.isCancelled = isCancelled;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}