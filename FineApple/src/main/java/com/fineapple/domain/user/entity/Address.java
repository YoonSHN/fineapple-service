package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class Address {
    private Long deliveryId;
    private String name;
    private String tel;
    private String country;
    private String city;
    private String region;
    private String postNum;
    private String roadNum;
    private String address;
    private Boolean isDefault;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userInfo;
    private Long guestId;
    private String deliveryStatus;

    @Builder
    public Address(Long deliveryId, String name, String tel, String country, String city, String region, String postNum,
                   String roadNum, String address, Boolean isDefault, LocalDateTime createdAt, LocalDateTime updatedAt,
                   Long userInfo, Long guestId, String deliveryStatus) {
        this.deliveryId = deliveryId;
        this.name = name;
        this.tel = tel;
        this.country = country;
        this.city = city;
        this.region = region;
        this.postNum = postNum;
        this.roadNum = roadNum;
        this.address = address;
        this.isDefault = isDefault;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userInfo = userInfo;
        this.guestId = guestId;
        this.deliveryStatus = deliveryStatus;
    }
}

