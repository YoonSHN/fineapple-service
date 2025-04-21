package com.fineapple.domain.order_payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class RefundDetailDto {
    private Long refundTransaction;
    private Long refundId;
    private String issueStatus;
    private LocalDateTime approvedTime;
    private String approvedNumber;
    private BigDecimal requestPrice;
    private BigDecimal remainingPrice;
    private String refundBankName;
    private String refundBankCode;
}
