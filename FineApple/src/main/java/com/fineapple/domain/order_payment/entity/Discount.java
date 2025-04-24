package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Discount {
    private Long discountId;
    private String discountName;
    private String discountCode;
    private BigDecimal discountPrice;
    private LocalDateTime discountStartDay;
    private LocalDateTime discountEndDay;
    private Integer minimumPrice;
    private Boolean isActive;
    private Long orderId;
    private Long productId;
    private String discountType;

    @Builder
    public Discount(Long discountId, String discountName, String discountCode, BigDecimal discountPrice,
                    LocalDateTime discountStartDay, LocalDateTime discountEndDay, Integer minimumPrice, Boolean isActive,
                    Long orderId, Long productId, String discountType) {
        this.discountId = discountId;
        this.discountName = discountName;
        this.discountCode = discountCode;
        this.discountPrice = discountPrice;
        this.discountStartDay = discountStartDay;
        this.discountEndDay = discountEndDay;
        this.minimumPrice = minimumPrice;
        this.isActive = isActive;
        this.orderId = orderId;
        this.productId = productId;
        this.discountType = discountType;
    }
}

