package com.fineapple.domain.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ShipmentRequestDto {
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime dispatchedAt;
    private String deliveryStatus;
    private Long storeId;
    private Long deliveryId;
    private BigDecimal shippingCost;
    private String courierCompany;
    private String delayReason;
    private String deliveryAgentName;
    private String deliveryAgentContact;
    private String postNum;
    private String city;
    private String region;
    private String roadNum;
    private String address;
    private String storeName;
    private Long orderId;
    private LocalDateTime deliveredAt;

}