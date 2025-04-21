package com.fineapple.domain.shipment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ShipmentDto {
    private Long shipmentId;
    private String trackingNumber;
    private String trackingUrl;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime dispatchedAt;
    private String deliveryStatus;
    private Long storeId;
    private Long deliveryId;
    private String delayReason;
    private String roadNum;
    private String address;
    private String storeName; // 순서 주의
    private String deliveryAgentName;
    private String deliveryAgentContact;
    private Long orderId;
    private String location;
    private String storeNumber;
}
