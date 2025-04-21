package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Payment {
    private Long paymentId;
    private Long orderId;
    private String paymentStatus;
    private LocalDateTime requestedAt;
    private BigDecimal totalAmount;
    private String pgApprovalCode;
    private String currency;
    private LocalDateTime paidAt;
    private LocalDateTime cancelledAt;
    private String receiptUrl;
    private String cardName;
    private Integer cardQuota;
    private String paymentMethod;
    private String failureCode;
    private String pgUniqueId;
    private String pgType;

    @Builder
    public Payment(Long paymentId, Long orderId, String paymentStatus, LocalDateTime requestedAt, BigDecimal totalAmount,
                   String pgApprovalCode, String currency, LocalDateTime paidAt, LocalDateTime cancelledAt, String receiptUrl,
                   String cardName, Integer cardQuota, String paymentMethod, String failureCode, String pgUniqueId, String pgType) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.requestedAt = requestedAt;
        this.totalAmount = totalAmount;
        this.pgApprovalCode = pgApprovalCode;
        this.currency = currency;
        this.paidAt = paidAt;
        this.cancelledAt = cancelledAt;
        this.receiptUrl = receiptUrl;
        this.cardName = cardName;
        this.cardQuota = cardQuota;
        this.paymentMethod = paymentMethod;
        this.failureCode = failureCode;
        this.pgUniqueId = pgUniqueId;
        this.pgType = pgType;
    }
}

