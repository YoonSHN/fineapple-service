package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CartItem {
    private Long cartItemId;
    private Long quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long cartId;
    private Long productId;

    @Builder
    public CartItem(Long cartItemId, Long quantity, LocalDateTime createdAt, LocalDateTime updatedAt, Long cartId, Long productId) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.cartId = cartId;
        this.productId = productId;
    }
}

