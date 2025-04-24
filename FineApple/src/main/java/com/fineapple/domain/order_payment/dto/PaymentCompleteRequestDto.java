package com.fineapple.domain.order_payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCompleteRequestDto {
    private Long orderId;
    private String impUid;
}

