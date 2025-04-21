package com.fineapple.domain.logistics_inventory.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Store {
    private Long storeId;
    private String name;
    private String location;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private String storeNumber;
    private String storeType;
    private LocalDateTime operationStartDate;
    private LocalDateTime operationEndDate;
    private Time openingTime;
    private Time closingTime;
    private String postalCode;
    private BigDecimal monthlyRevenue;
    private String storeStatus;
    private Long managerId;

    @Builder
    public Store(Long storeId, String name, String location, LocalDateTime updatedAt, LocalDateTime createdAt, String storeNumber,
                 String storeType, LocalDateTime operationStartDate, LocalDateTime operationEndDate, Time openingTime, Time closingTime,
                 String postalCode, BigDecimal monthlyRevenue, String storeStatus, Long managerId) {
        this.storeId = storeId;
        this.name = name;
        this.location = location;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.storeNumber = storeNumber;
        this.storeType = storeType;
        this.operationStartDate = operationStartDate;
        this.operationEndDate = operationEndDate;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
        this.postalCode = postalCode;
        this.monthlyRevenue = monthlyRevenue;
        this.storeStatus = storeStatus;
        this.managerId = managerId;
    }
}

