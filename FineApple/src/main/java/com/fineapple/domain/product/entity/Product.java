package com.fineapple.domain.product.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Product {
    private Long productId;
    private String name;
    private String description;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long categoryId;
    private Boolean isActive;
    private LocalDateTime targetReleaseDate;
    private LocalDateTime actualReleaseDate;
    private LocalDateTime saleStartDate;
    private LocalDateTime saleStopDate;
    private LocalDateTime saleRestartDate;
    private LocalDateTime saleEndDate;
    private String saleStatus;

    @Builder
    public Product(Long productId, String name, String description, BigDecimal price, LocalDateTime createdAt, LocalDateTime updatedAt,
                   Long categoryId, Boolean isActive, LocalDateTime targetReleaseDate, LocalDateTime actualReleaseDate,
                   LocalDateTime saleStartDate, LocalDateTime saleStopDate, LocalDateTime saleRestartDate, LocalDateTime saleEndDate, String saleStatus) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.categoryId = categoryId;
        this.isActive = isActive;
        this.targetReleaseDate = targetReleaseDate;
        this.actualReleaseDate = actualReleaseDate;
        this.saleStartDate = saleStartDate;
        this.saleStopDate = saleStopDate;
        this.saleRestartDate = saleRestartDate;
        this.saleEndDate = saleEndDate;
        this.saleStatus = saleStatus;
    }
}

