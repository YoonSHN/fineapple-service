package com.fineapple.domain.logistics_inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreDto {
    private Long storeId;
    private String name;
    private String location;
    private String storeNumber;
}