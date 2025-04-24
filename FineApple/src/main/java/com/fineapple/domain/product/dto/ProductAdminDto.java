package com.fineapple.domain.product.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductAdminDto {
    private Long productId;
    private String productName;
    private String description;
    private String productImageUrl;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
    private String categoryName;

    private List<ProductOptionDto> options;
}
