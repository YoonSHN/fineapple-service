package com.fineapple.domain.logistics_inventory.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class Logistics {
    private Long logisticsId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long fromStoreId;
    private Long toStoreId;
    private String deliveryStatus;
    private String approvedStatus;
    private Integer sumQuantity;
    private BigDecimal totalAmount;
    private String fromStoreName;
    private String fromStoreLocation;
    private String toStoreName;
    private String toStoreLocation;

    @Builder
    public Logistics(Long logisticsId, LocalDateTime createdAt, LocalDateTime updatedAt, Long fromStoreId, Long toStoreId,
                     String deliveryStatus, String approvedStatus, Integer sumQuantity, BigDecimal totalAmount,
                     String fromStoreName, String fromStoreLocation, String toStoreName, String toStoreLocation) {
        this.logisticsId = logisticsId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.fromStoreId = fromStoreId;
        this.toStoreId = toStoreId;
        this.deliveryStatus = deliveryStatus;
        this.approvedStatus = approvedStatus;
        this.sumQuantity = sumQuantity;
        this.totalAmount = totalAmount;
        this.fromStoreName = fromStoreName;
        this.fromStoreLocation = fromStoreLocation;
        this.toStoreName = toStoreName;
        this.toStoreLocation = toStoreLocation;
    }
}

