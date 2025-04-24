package com.fineapple.domain.shipment.dto;

import lombok.Getter;

@Getter
public class UpdateShipmentStatusRequest {
    private String deliveryStatus;
}