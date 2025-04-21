package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class UserCoupon {
    private Long userCouponId;
    private Long couponId;
    private Long userId;
    private Boolean isUsed;
    private LocalDateTime createdAt;
    private LocalDateTime usedAt;
    private LocalDateTime endAt;

    @Builder
    public UserCoupon(Long userCouponId, Long couponId, Long userId, Boolean isUsed, LocalDateTime createdAt, LocalDateTime usedAt, LocalDateTime endAt) {
        this.userCouponId = userCouponId;
        this.couponId = couponId;
        this.userId = userId;
        this.isUsed = isUsed;
        this.createdAt = createdAt;
        this.usedAt = usedAt;
        this.endAt = endAt;
    }
}

