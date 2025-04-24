package com.fineapple.domain.order_payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderAllDto {
    private Long orderId;
    private String orderCode;
    private BigDecimal totalPrice;
    private String orderStatus;
    private Boolean isCancelled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String tel;
}
