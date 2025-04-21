package com.fineapple.domain.shipment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentDetailDto {
    private Long shipmentId;
    private LocalDateTime deliveryAt;
    private BigDecimal shippingCost;
    private String deliveryAgentName;
    private String deliveryAgentContact;
    private String address; //상세 주소

    //order
    private Long orderId;
    private String paymentMethod;
    private String paymentName;
    private String orderStatus;
    private String orderStatusName;
    private Long userId;
    private Long guestId;
    private Long totalPrice;
    private Long discountPrice;

    //store
    private Long storeId;
    private String storeNumber;
    private String storeName;
    private String location;

    private String deliveryStatus;

    //Address
    private Long deliveryId;
    private String name;
}