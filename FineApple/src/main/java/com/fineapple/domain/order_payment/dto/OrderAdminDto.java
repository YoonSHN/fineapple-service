package com.fineapple.domain.order_payment.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
public class OrderAdminDto {

    private Long orderId;
    private String orderCode;
    private BigDecimal totalPrice;
    private BigDecimal discountPrice;
    private Boolean isCancelled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String orderStatus;
    private String tel;
    private Long orderItemDetailId;
    private String name;
    private int quantity;
    private BigDecimal price;
    private String imageUrl;


}
