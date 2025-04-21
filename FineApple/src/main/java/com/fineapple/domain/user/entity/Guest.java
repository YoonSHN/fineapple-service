package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class Guest {
    private Long guestId;
    private Long orderId;
    private String name;
    private String address;
    private String tel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public Guest(Long guestId, Long orderId, String name, String address, String tel, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.guestId = guestId;
        this.orderId = orderId;
        this.name = name;
        this.address = address;
        this.tel = tel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}

