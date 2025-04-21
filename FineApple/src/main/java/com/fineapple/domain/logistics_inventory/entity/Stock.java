package com.fineapple.domain.logistics_inventory.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Stock {
    private Long stockId;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastRestockDate;
    private Long productId;
    private Long storeId;
    private String stockStatus;
    private LocalDateTime firstStockInDate;
    private LocalDateTime lastStockOutDate;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private Integer safetyStockLevel;
    private Integer stockInQuantity;
    private Integer stockOutQuantity;
    private Boolean isRestockRequired;

    @Builder(toBuilder = true)
    public Stock(Long stockId, Integer quantity, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime lastRestockDate,
                 Long productId, Long storeId, String stockStatus, LocalDateTime firstStockInDate, LocalDateTime lastStockOutDate,
                 Integer minStockLevel, Integer maxStockLevel, Integer safetyStockLevel, Integer stockInQuantity,
                 Integer stockOutQuantity, Boolean isRestockRequired) {
        this.stockId = stockId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastRestockDate = lastRestockDate;
        this.productId = productId;
        this.storeId = storeId;
        this.stockStatus = stockStatus;
        this.firstStockInDate = firstStockInDate;
        this.lastStockOutDate = lastStockOutDate;
        this.minStockLevel = minStockLevel;
        this.maxStockLevel = maxStockLevel;
        this.safetyStockLevel = safetyStockLevel;
        this.stockInQuantity = stockInQuantity;
        this.stockOutQuantity = stockOutQuantity;
        this.isRestockRequired = isRestockRequired;
    }
}

