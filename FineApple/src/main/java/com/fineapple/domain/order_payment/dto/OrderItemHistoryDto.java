package com.fineapple.domain.order_payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderItemHistoryDto {
    private Long orderItemHistoryId;
    private Long orderItemDetailId;
    private BigDecimal oldPrice;
    private BigDecimal newPrice;
    private Integer oldQuantity;
    private Integer newQuantity;
    private String changeReason;
    private LocalDateTime changedAt;
    private String changeBy;
    private String itemHistoryStatus;
}
