package com.fineapple.domain.product.service;

import com.fineapple.Infrastructure.exception.ProductNotFoundException;
import com.fineapple.Infrastructure.exception.ProductUploadException;
import com.fineapple.domain.product.dto.ProductAdminDto;
import com.fineapple.domain.product.dto.ProductDetailDto;
import com.fineapple.domain.product.dto.ProductOptionDto;
import com.fineapple.domain.product.dto.ProductSearchParam;
import com.fineapple.domain.product.repository.ProductMapper;
import com.fineapple.exceptionTest.ExceptionTest;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductMapper productMapper;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private ProductServiceImp productService;


    @Test
    void 상품_전체_조회_getAllProduct() {
        List<ProductAdminDto> mockList = new ArrayList<>();
        ProductAdminDto dto = new ProductAdminDto();
        dto.setProductId(1L);
        dto.setProductName("Test Product");
        mockList.add(dto);
        when(productMapper.selectAllProduct(any(ProductSearchParam.class))).thenReturn(mockList);
        PageInfo<ProductAdminDto> result = productService.getAllProduct(new ProductSearchParam(), 1, 10);
        Assertions.assertEquals(1, result.getList().size());
        Assertions.assertEquals("Test Product", result.getList().getFirst().getProductName());
    }

    @Test
    void getAllProductOptionByproductId() {
//        List<ProductAdminDto> mockList = new ArrayList<>();
//        ProductAdminDto dto = new ProductAdminDto();
//        dto.setProductId(2L);
//        dto.setProductName("Option Product");
//        mockList.add(dto);
//        when(productMapper.selectAllProductWithOption(2L)).thenReturn(mockList);
//        List<ProductAdminDto> result = productService.getAllProductOptionByproductId(2L);
//        Assertions.assertEquals(1, result.size());
//        Assertions.assertEquals("Option Product", result.getFirst().getProductName());
    }

    @BeforeEach
    void setup() {
        Mockito.lenient().when(messageSource.getMessage(any(), any(), any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void 상품_상태_토글_성공() {
        Long productId = 1L;

        ProductDetailDto mockDto = new ProductDetailDto();
        mockDto.setActive(true);

        when(productMapper.findProductByIdForUpdate(productId)).thenReturn(mockDto);
        when(productMapper.updateIsActive(productId, false)).thenReturn(1);

        assertDoesNotThrow(() -> productService.toggleIsActive(productId));
    }

    @Test
    void 상품_상태_토글_실패_상품없음() {
        Long productId = 1L;

        lenient().when(productMapper.existsProductById(productId)).thenReturn(false);

        assertThrows(ProductNotFoundException.class, () -> productService.toggleIsActive(productId));
    }

    @Test
    void 상품_옵션_삭제_성공() {
        Long productId = 1L;
        Long optionId = 10L;

        ProductOptionDto option = new ProductOptionDto();
        option.setOptionId(optionId);
        option.setProductId(productId);

        when(productMapper.findOptionById(optionId)).thenReturn(option);
        when(productMapper.deleteProductOptionById(optionId)).thenReturn(1);

        assertDoesNotThrow(() -> productService.deleteOption(productId, optionId));
    }

    @ExceptionTest(expected = ProductUploadException.class, message = "error.product.not_found")
    void 상품_옵션_삭제_실패_상품불일치() {
        Long productId = 1L;
        Long optionId = 10L;

        ProductOptionDto option = new ProductOptionDto();
        option.setOptionId(optionId);
        option.setProductId(999L);

        lenient().when(productMapper.findOptionById(optionId)).thenReturn(option);

        productService.deleteOption(productId, optionId);
    }

    @Test
    void 상품_상세조회_성공() {
        Long productId = 1L;

        when(productMapper.existsProductById(productId)).thenReturn(true);
        when(productMapper.findProductById(productId)).thenReturn(new ProductDetailDto());

        assertNotNull(productService.getProductDetail(productId));
    }

    @ExceptionTest(expected = ProductNotFoundException.class, message = "error.product.not_found")
    void 상품_상세조회_실패_null리턴() {
        Long productId = 1L;

        when(productMapper.existsProductById(productId)).thenReturn(true);
        when(productMapper.findProductById(productId)).thenReturn(null);

        productService.getProductDetail(productId);
    }

}

