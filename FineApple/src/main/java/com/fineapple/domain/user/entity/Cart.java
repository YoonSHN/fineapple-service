package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Cart {
    private Long cartId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private Long guestId;

    @Builder
    public Cart(Long cartId, LocalDateTime createdAt, LocalDateTime updatedAt, Long userId, Long guestId) {
        if (userId == null && guestId == null) {
            throw new IllegalArgumentException("userId와 guestId 중 하나는 필수입니다.");
        }
        this.cartId = cartId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.guestId = guestId;
    }
}

