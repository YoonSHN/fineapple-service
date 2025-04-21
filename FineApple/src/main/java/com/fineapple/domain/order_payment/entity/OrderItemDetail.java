package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class OrderItemDetail {
    private Long orderItemDetailId;
    private Long orderId;
    private Long productId;
    private Long optionId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
    private Boolean couponApplied;
    private BigDecimal discountPrice;
    private BigDecimal additionalPrice;
    private String additional;
    private String itemStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public OrderItemDetail(Long orderItemDetailId, Long orderId, Long productId, Long optionId,
                           String name, Integer quantity, BigDecimal price, Boolean couponApplied,
                           BigDecimal discountPrice, BigDecimal additionalPrice, String additional,
                           String itemStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.orderItemDetailId = orderItemDetailId;
        this.orderId = orderId;
        this.productId = productId;
        this.optionId = optionId;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.couponApplied = couponApplied;
        this.discountPrice = discountPrice;
        this.additionalPrice = additionalPrice;
        this.additional = additional;
        this.itemStatus = itemStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
