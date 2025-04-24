package com.fineapple.domain.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductOptionDto {
    private Long optionId;
    private Long productId;
    private String optionName;
    private String optionValue;
    private BigDecimal additionalPrice;
}
