package com.fineapple.domain.product.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ProductOption {
    private Long optionId;
    private Long productId;
    private String optionName;
    private String optionValue;
    private BigDecimal additionalPrice;

    @Builder
    public ProductOption(Long optionId, Long productId, String optionName, String optionValue, BigDecimal additionalPrice) {
        this.optionId = optionId;
        this.productId = productId;
        this.optionName = optionName;
        this.optionValue = optionValue;
        this.additionalPrice = additionalPrice;
    }
}

