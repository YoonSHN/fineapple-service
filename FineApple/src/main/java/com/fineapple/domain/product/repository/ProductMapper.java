package com.fineapple.domain.product.repository;

import com.fineapple.domain.product.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    List<ProductAdminDto> selectAllProduct(ProductSearchParam param);

    List<ProductAdminDto> selectAllProductWithOption(Long productId);

    ProductDetailDto findProductById(Long id);

    List<ProductListDto> findAllProducts();

    AdminProductDetailDto findAdminProductById(Long id);

    boolean existsMainImage(Long productId);

    int insertProduct(ProductInsertDto productDto);

    int insertProductImage(ProductImageInsertDto imageDto);

    boolean existsProductById(Long productId);

    boolean existsCategoryById(Long categoryId);

    int insertProductOption(ProductOptionDto productOptionDto);

    int updateIsActive(Long productId, boolean isActive);

    ProductOptionDto findOptionById(Long optionId);

    int deleteProductOptionById(Long optionId);

    void deleteProductImage(Long imageId);

    ProductImageInsertDto selectMainImage(Long productId);

    int updateProduct(ProductUpdateDto productDto);

    List<ProductListDto> selectProductByCategoryId(Long categoryId);

    List<ProductListDto> selectProductByCategoryPath(String path);

    ProductDetailDto findProductByIdForUpdate(Long productId);


}