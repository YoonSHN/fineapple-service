package com.fineapple.domain.logistics_inventory.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class StockChangeHistory {
    private Long stockChangeId;
    private Long stockId;
    private Long productId;
    private Long storeId;
    private String storeName;
    private Integer stockInQuantity;
    private String stockInReason;
    private Integer stockOutQuantity;
    private String stockOutReason;
    private Integer previousStock;
    private Integer newStock;
    private LocalDateTime changedAt;

    @Builder
    public StockChangeHistory(Long stockChangeId, Long stockId, Long productId, Long storeId, String storeName,
                              Integer stockInQuantity, String stockInReason, Integer stockOutQuantity, String stockOutReason,
                              Integer previousStock, Integer newStock, LocalDateTime changedAt) {
        this.stockChangeId = stockChangeId;
        this.stockId = stockId;
        this.productId = productId;
        this.storeId = storeId;
        this.storeName = storeName;
        this.stockInQuantity = stockInQuantity;
        this.stockInReason = stockInReason;
        this.stockOutQuantity = stockOutQuantity;
        this.stockOutReason = stockOutReason;
        this.previousStock = previousStock;
        this.newStock = newStock;
        this.changedAt = changedAt;
    }
}

