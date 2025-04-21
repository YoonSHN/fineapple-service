package com.fineapple.domain.product.dto;

import com.fineapple.domain.product.entity.Category;
import com.fineapple.domain.product.entity.Product;
import com.fineapple.domain.product.entity.ProductImage;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProductListDto {
        private long productId;
        private String name;
        private BigDecimal price;
        private String categoryName;
        private String imageUrl;
        private String saleStatus;

}
