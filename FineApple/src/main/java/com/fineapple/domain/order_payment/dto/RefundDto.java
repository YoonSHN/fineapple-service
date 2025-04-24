package com.fineapple.domain.order_payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class RefundDto {
    private Long refundId;
    private String refundStatus;
    private Long paymentId;
    private LocalDateTime requestedAt;
    private LocalDateTime approvedAt;
    private BigDecimal refundTotalAmount;
    private String refundReason;
    private String pgResponseCode;
    private String refundFailReason;

    private List<RefundDetailDto> details;

}

