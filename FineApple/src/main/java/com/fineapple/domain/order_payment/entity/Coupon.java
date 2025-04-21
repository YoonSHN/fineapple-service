package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Coupon {
    private Long couponId;
    private String couponCode;
    private String couponName;
    private BigDecimal discountAmount;
    private BigDecimal minOrderAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer useValid;

    @Builder
    public Coupon(Long couponId, String couponCode, String couponName, BigDecimal discountAmount, BigDecimal minOrderAmount,
                  LocalDateTime startDate, LocalDateTime endDate, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, Integer useValid) {
        this.couponId = couponId;
        this.couponCode = couponCode;
        this.couponName = couponName;
        this.discountAmount = discountAmount;
        this.minOrderAmount = minOrderAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.useValid = useValid;
    }
}

