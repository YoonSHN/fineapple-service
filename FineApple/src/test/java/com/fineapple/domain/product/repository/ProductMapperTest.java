package com.fineapple.domain.product.repository;

import com.fineapple.domain.product.dto.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void testExistsCategoryById_true() {
        boolean exists = productMapper.existsCategoryById(1L);
        Assertions.assertTrue(exists);
    }

    @Test
    void testInsertProduct_success() {
        ProductInsertDto dto = new ProductInsertDto();
        dto.setName("테스트 상품");
        dto.setDescription("설명");
        dto.setPrice(new BigDecimal("2000"));
        dto.setCategoryId(1L);
        dto.setIsActive(true);
        dto.setTargetReleaseDate(LocalDateTime.now());
        dto.setSaleStatus("PR0101");

        int inserted = productMapper.insertProduct(dto);

        Assertions.assertEquals(1, inserted);
        Assertions.assertNotNull(dto.getProductId());
    }

    @Test
    void testInsertProductImage_success() {
        ProductInsertDto productDto = new ProductInsertDto();
        productDto.setName("이미지 테스트 상품");
        productDto.setDescription("이미지 설명");
        productDto.setPrice(new BigDecimal("1500"));
        productDto.setCategoryId(1L);
        productDto.setIsActive(true);
        productDto.setTargetReleaseDate(LocalDateTime.now());
        productDto.setSaleStatus("PR0101");

        productMapper.insertProduct(productDto);

        ProductImageInsertDto imageDto = new ProductImageInsertDto();
        imageDto.setProductId(productDto.getProductId());
        imageDto.setImageUrl("/test/image.jpg");
        imageDto.setProductMain(true);

        int inserted = productMapper.insertProductImage(imageDto);

        Assertions.assertEquals(1, inserted);
    }

    @Test
    void testFindProductById_success() {
        ProductDetailDto dto = productMapper.findProductById(1L);
        Assertions.assertNotNull(dto);
        Assertions.assertEquals(1L, dto.getProductId());
    }

    @Test
    void testSelectProductByCategoryId_success() {
        List<ProductListDto> list = productMapper.selectProductByCategoryId(1L);
        Assertions.assertNotNull(list);
    }

    @Test
    void testUpdateIsActive_success() {
        int updated = productMapper.updateIsActive(1L, false);
        Assertions.assertEquals(1, updated);
    }

    @Test
    void testFindProductByIdForUpdate_success() {
        ProductDetailDto dto = productMapper.findProductByIdForUpdate(1L);
        Assertions.assertNotNull(dto);
    }

    @Test
    void testInsertProductOption_success() {
        ProductOptionDto optionDto = new ProductOptionDto();
        optionDto.setProductId(1L);
        optionDto.setOptionName("색상");
        optionDto.setOptionValue("검정");
        optionDto.setAdditionalPrice(new BigDecimal("1500"));

        int inserted = productMapper.insertProductOption(optionDto);
        Assertions.assertEquals(1, inserted);
    }

    @Test
    void testDeleteProductOptionById_success() {
        ProductOptionDto optionDto = new ProductOptionDto();
        optionDto.setProductId(1L);
        optionDto.setOptionName("사이즈");
        optionDto.setOptionValue("L");
        optionDto.setAdditionalPrice(new BigDecimal("1500"));

        productMapper.insertProductOption(optionDto);
        Long optionId = optionDto.getOptionId();

        Assertions.assertNotNull(optionId);

        int deleted = productMapper.deleteProductOptionById(optionId);
        Assertions.assertEquals(1, deleted);
    }

    @Test
    void testSelectAllProduct_success() {
        ProductSearchParam param = new ProductSearchParam();
        param.setName("상품");

        List<ProductAdminDto> result = productMapper.selectAllProduct(param);
        Assertions.assertNotNull(result);
    }

    @Test
    void testSelectAllProductWithOption_success() {
        List<ProductAdminDto> result = productMapper.selectAllProductWithOption(1L);
        Assertions.assertNotNull(result);
    }

    @Test
    void testSelectMainImage_success() {
        ProductImageInsertDto image = productMapper.selectMainImage(1L);
        Assertions.assertNotNull(image);
    }

    @Test
    void testFindAllProducts_success() {
        List<ProductListDto> list = productMapper.findAllProducts();
        Assertions.assertNotNull(list);
    }

    @Test
    void testExistsProductById_true() {
        boolean exists = productMapper.existsProductById(1L);
        Assertions.assertTrue(exists);
    }

    @Test
    void testDeleteProductImage_success() {
        ProductImageInsertDto image = productMapper.selectMainImage(1L);
        if (image != null && image.getImageId() != null) {
            productMapper.deleteProductImage(image.getImageId());
        }
    }
}
