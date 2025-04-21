package com.fineapple.domain.product.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class ProductImage {
    private Long imageId;
    private String imageUrl;
    private LocalDateTime createdId;
    private LocalDateTime updatedId;
    private Long productId;
    private String altText;

    @Builder
    public ProductImage(Long imageId, String imageUrl, LocalDateTime createdId, LocalDateTime updatedId, Long productId, String altText) {
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.createdId = createdId;
        this.updatedId = updatedId;
        this.productId = productId;
        this.altText = altText;
    }
}

