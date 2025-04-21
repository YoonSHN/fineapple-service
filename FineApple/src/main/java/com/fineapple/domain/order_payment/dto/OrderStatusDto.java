package com.fineapple.domain.order_payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderStatusDto {
    private Long orderstatusId;
    private String orderStatusStatus;
    private String paymentStatus;
    private LocalDate updatedAt;
    private String orderCode;

}
