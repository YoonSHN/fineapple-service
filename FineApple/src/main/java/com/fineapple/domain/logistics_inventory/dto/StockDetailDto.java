package com.fineapple.domain.logistics_inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockDetailDto {

    private Long stockId;
    private Long productId;
    private Long storeId;
    private Integer quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastRestockDate;
    private String stockStatus;
    private LocalDateTime firstStockInDate;
    private LocalDateTime lastStockOutDate;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private Integer safetyStockLevel;
    private Integer stockInQuantity;
    private Integer stockOutQuantity;
    private Boolean isRestockRequired;
}
