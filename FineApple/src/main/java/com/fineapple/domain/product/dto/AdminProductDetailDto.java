package com.fineapple.domain.product.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class AdminProductDetailDto {
    private Long productId;
    private String productImage;
    private String name;
    private String description;
    private BigDecimal price;
    private String createdAt;
    private String updatedAt;
    private String categoryName;
    private Boolean isActive;
    private String targetReleaseDate;
    private String actualReleaseDate;
    private String saleStartDate;
    private String saleStopDate;
    private String saleRestartDate;
    private String saleEndDate;
    private String saleStatus;

    public String getDescription() {
        return description == null ? "" : description;  // description이 null일 경우 빈 문자열로 처리
    }
}
