package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Refund {
    private Long refundId;
    private String refundStatus;
    private Long paymentId;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private BigDecimal refundTotalAmount;
    private String refundReason;
    private String pgResponseCode;
    private String refundFailReason;

    @Builder
    public Refund(Long refundId, String refundStatus, Long paymentId, LocalDateTime requestedAt, LocalDateTime approvedAt,
                  BigDecimal refundTotalAmount, String refundReason, String pgResponseCode, String refundFailReason) {
        this.refundId = refundId;
        this.refundStatus = refundStatus;
        this.paymentId = paymentId;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
        this.refundTotalAmount = refundTotalAmount;
        this.refundReason = refundReason;
        this.pgResponseCode = pgResponseCode;
        this.refundFailReason = refundFailReason;
    }
}
