package com.fineapple.domain.product.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageInsertDto {
    private Long imageId;
    private String imageUrl;
    private Long productId;
    private String altText;
    private Boolean productMain;
}
