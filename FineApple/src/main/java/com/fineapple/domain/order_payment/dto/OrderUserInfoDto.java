package com.fineapple.domain.order_payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderUserInfoDto {
    private String name;
    private String address;
    private String addressDetail;
    private String contact;
    private String email;
}