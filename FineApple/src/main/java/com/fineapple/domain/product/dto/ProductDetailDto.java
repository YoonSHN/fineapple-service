package com.fineapple.domain.product.dto;

import com.fineapple.domain.logistics_inventory.entity.Stock;
import com.fineapple.domain.product.entity.Category;
import com.fineapple.domain.product.entity.Product;
import com.fineapple.domain.product.entity.ProductImage;
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
public class ProductDetailDto {
    private long productId;
    private String imageUrl;
    private String name;
    private String description;
    private BigDecimal price;
    private String targetReleaseDate;
    private String saleStartDate;
    private String categoryName;
    private boolean isActive;

}
