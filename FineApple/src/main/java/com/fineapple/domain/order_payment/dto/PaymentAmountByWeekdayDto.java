package com.fineapple.domain.order_payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentAmountByWeekdayDto {
    private String weekday;
    private BigDecimal totalAmount;
}
