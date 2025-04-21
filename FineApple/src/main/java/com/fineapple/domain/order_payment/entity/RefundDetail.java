package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RefundDetail {
    private Long refundTransaction;
    private Long refundId;
    private String issueStatus;
    private LocalDateTime approvedTime;
    private String approvedNumber;
    private BigDecimal requestPrice;
    private BigDecimal remainingPrice;
    private String refundBankName;
    private String refundBankCode;

    @Builder
    public RefundDetail(Long refundTransaction, Long refundId, String issueStatus, LocalDateTime approvedTime,
                        String approvedNumber, BigDecimal requestPrice, BigDecimal remainingPrice,
                        String refundBankName, String refundBankCode) {
        this.refundTransaction = refundTransaction;
        this.refundId = refundId;
        this.issueStatus = issueStatus;
        this.approvedTime = approvedTime;
        this.approvedNumber = approvedNumber;
        this.requestPrice = requestPrice;
        this.remainingPrice = remainingPrice;
        this.refundBankName = refundBankName;
        this.refundBankCode = refundBankCode;
    }
}

