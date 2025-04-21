package com.fineapple.domain.logistics_inventory.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockChangeHistoryDto {
    private Long stockChangeId;
    private Long stockId;
    private Long storeId;
    private String storeName;
    private Long productId;
    private Integer stockInQuantity;
    private Integer stockOutQuantity;
    private Integer previousStock;
    private Integer newStock;
    private String stockInReason;
    private String stockOutReason;
    private LocalDateTime changedAt;

    private String productName;
    private String type; // IN, OUT
    private String memo; // 재고 사유를 묶어서 보여주고 싶을 경우
}
