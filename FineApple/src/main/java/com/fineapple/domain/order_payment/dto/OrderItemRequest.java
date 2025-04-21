package com.fineapple.domain.order_payment.dto;

import java.math.BigDecimal;

public record OrderItemRequest(Long productId, Integer quantity, Integer price) {
}
