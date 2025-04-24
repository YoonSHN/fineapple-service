package com.fineapple.domain.order_payment.dto;

import java.math.BigDecimal;

public record OrderInfoDto(Long orderId, Long orderCode, BigDecimal totalPrice,
                           BigDecimal discountPrice, String orderStatus, String paymentStatus) {
}
