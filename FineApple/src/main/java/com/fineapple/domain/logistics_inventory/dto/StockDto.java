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
public class StockDto { //스토어에 있는 모든 상품을 조회할 떄 표시 할 내용
    private Long stockId;
    private Long productId;
    private int stockInQuantity;
    private int stockOutQuantity;
    private String stockInReason;
    private String stockOutReason;
    private String storeName;
    private Long storeId;
    private String codeName;
    // 드랍박스에서 선택한 상태 코드 값 추가
    private String stockStatus;
    private Integer quantity;
    private int previousStock;
    private int newStock;
    private LocalDateTime updatedAt;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private Boolean isRestockRequired;

    @Override
    public String toString() {
        return "StockDto{" +
                "stockId=" + stockId +
                ", productId=" + productId +
                ", stockInQuantity=" + stockInQuantity +
                ", stockOutQuantity=" + stockOutQuantity +
                ", stockInReason='" + stockInReason + '\'' +
                ", stockOutReason='" + stockOutReason + '\'' +
                ", storeName='" + storeName + '\'' +
                ", storeId=" + storeId +
                ", codeName='" + codeName + '\'' +
                ", stockStatus='" + stockStatus + '\'' +
                ", quantity=" + quantity +
                ", previousStock=" + previousStock +
                ", newStock=" + newStock +
                ", updatedAt=" + updatedAt +
                ", minStockLevel=" + minStockLevel +
                ", maxStockLevel=" + maxStockLevel +
                ", isRestockRequired=" + isRestockRequired +
                '}';
    }
}


