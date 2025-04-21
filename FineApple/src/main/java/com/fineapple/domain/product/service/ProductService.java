package com.fineapple.domain.product.service;

import com.fineapple.domain.product.dto.*;
import com.github.pagehelper.PageInfo;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {


    @Transactional(readOnly = true)
    PageInfo<ProductAdminDto> getAllProduct(ProductSearchParam productSearchParam, int pageNum, int pageSize);

    ProductDetailDto getProductDetail(Long productId);

    List<ProductListDto> getAllProduct();

    List<ProductAdminDto> getAllProductOptionByproductId(Long productId);

    AdminProductDetailDto getAdminProductDetail(Long productId);

    PageInfo<ProductListDto> getProductsByCategoryId(Long categoryId, int pageNum, int pageSize);

    default String defaultIfNull(String value, String defaultValue) {
        return value != null ? value : defaultValue;
    }

    default <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    @Transactional
    void toggleIsActive(Long productId);

    @Transactional
    void deleteOption(Long productId, Long optionId);

    PageInfo<ProductListDto> getProductsByCategoryPath(String path, int pageNum, int pageSize);
}
