package com.fineapple.domain.logistics_inventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockModifyDto {

    private Integer quantity;
    private String stockStatus;
    private LocalDateTime lastRestockDate;
    private LocalDateTime firstStockInDate;
    private LocalDateTime lastStockOutDate;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private Integer safetyStockLevel;
    private Integer stockInQuantity;
    private Integer stockOutQuantity;
    private Boolean isRestockRequired;
}