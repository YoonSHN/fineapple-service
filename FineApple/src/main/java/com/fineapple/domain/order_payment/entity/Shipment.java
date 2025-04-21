package com.fineapple.domain.order_payment.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class Shipment {
    private Long shipmentId;
    private String trackingNumber;
    private String trackingUrl;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime dispatchedAt;
    private LocalDateTime deliveredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long orderId;
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

    @Builder
    public Shipment(Long shipmentId, String trackingNumber, String trackingUrl, LocalDateTime estimatedDeliveryDate,
                    LocalDateTime dispatchedAt, LocalDateTime deliveredAt, LocalDateTime createdAt, LocalDateTime updatedAt, Long orderId,
                    String deliveryStatus, Long storeId, Long deliveryId, BigDecimal shippingCost, String courierCompany,
                    String delayReason, String deliveryAgentName, String deliveryAgentContact, String postNum, String city,
                    String region, String roadNum, String address, String storeName) {
        this.shipmentId = shipmentId;
        this.trackingNumber = trackingNumber;
        this.trackingUrl = trackingUrl;
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        this.dispatchedAt = dispatchedAt;
        this.deliveredAt = deliveredAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderId = orderId;
        this.deliveryStatus = deliveryStatus;
        this.storeId = storeId;
        this.deliveryId = deliveryId;
        this.shippingCost = shippingCost;
        this.courierCompany = courierCompany;
        this.delayReason = delayReason;
        this.deliveryAgentName = deliveryAgentName;
        this.deliveryAgentContact = deliveryAgentContact;
        this.postNum = postNum;
        this.city = city;
        this.region = region;
        this.roadNum = roadNum;
        this.address = address;
        this.storeName = storeName;
    }
}

