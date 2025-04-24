package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PaymentDetail {
    private Long detailId;
    private Long paymentId;
    private Long orderItemDetailId;
    private String productName;
    private BigDecimal paidAmount;
    private Integer quantity;
    private Integer cancelledQuantity;
    private BigDecimal cancelledAmount;
    private String failReason;
    private LocalDateTime cancelledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public PaymentDetail(Long detailId, Long paymentId, Long orderItemDetailId, String productName, BigDecimal paidAmount,
                         Integer quantity, Integer cancelledQuantity, BigDecimal cancelledAmount, String failReason,
                         LocalDateTime cancelledAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.detailId = detailId;
        this.paymentId = paymentId;
        this.orderItemDetailId = orderItemDetailId;
        this.productName = productName;
        this.paidAmount = paidAmount;
        this.quantity = quantity;
        this.cancelledQuantity = cancelledQuantity;
        this.cancelledAmount = cancelledAmount;
        this.failReason = failReason;
        this.cancelledAt = cancelledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

