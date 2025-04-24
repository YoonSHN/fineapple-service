package com.fineapple.domain.logistics_inventory.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class LogisticsDetail {
    private Long detailId;
    private Integer quantity;
    private LocalDateTime createdAt;
    private Long productId;
    private LocalDateTime updatedAt;
    private LocalDateTime departureTime;
    private LocalDateTime estimatedArrivalTime;
    private LocalDateTime actualArrivalTime;
    private BigDecimal amount;
    private String transportationMethod;
    private Long departureStoreManagerId;
    private Long arrivalStoreManagerId;
    private Long logisticsId;

    @Builder
    public LogisticsDetail(Long detailId, Integer quantity, LocalDateTime createdAt, Long productId, LocalDateTime updatedAt,
                           LocalDateTime departureTime, LocalDateTime estimatedArrivalTime, LocalDateTime actualArrivalTime,
                           BigDecimal amount, String transportationMethod, Long departureStoreManagerId,
                           Long arrivalStoreManagerId, Long logisticsId) {
        this.detailId = detailId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.productId = productId;
        this.updatedAt = updatedAt;
        this.departureTime = departureTime;
        this.estimatedArrivalTime = estimatedArrivalTime;
        this.actualArrivalTime = actualArrivalTime;
        this.amount = amount;
        this.transportationMethod = transportationMethod;
        this.departureStoreManagerId = departureStoreManagerId;
        this.arrivalStoreManagerId = arrivalStoreManagerId;
        this.logisticsId = logisticsId;
    }
}

