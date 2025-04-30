package com.fineapple.domain.product.service;

import com.fineapple.application.exception.ProductUploadException;
import com.fineapple.domain.product.dto.*;
import com.fineapple.domain.product.repository.ProductMapper;
import com.fineapple.config.ExceptionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductImgServiceImpTest {

    @InjectMocks
    private ProductImgServiceImp productImgService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private MessageSource messageSource;

    private final String imageDir = "uploads";
    private final String imageUrlPrefix = "http://localhost/images";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(productImgService, "imageDir", imageDir);
        ReflectionTestUtils.setField(productImgService, "imageUrlPrefix", imageUrlPrefix);

        lenient().when(messageSource.getMessage(anyString(), any(), any(Locale.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private ProductInsertDto defaultProduct() {
        ProductInsertDto dto = new ProductInsertDto();
        dto.setProductId(123L);
        dto.setCategoryId(1L);
        dto.setPrice(BigDecimal.valueOf(10000));
        return dto;
    }

    private ProductImageInsertDto defaultImage() {
        ProductImageInsertDto dto = new ProductImageInsertDto();
        dto.setProductMain(true);
        return dto;
    }

    private MockMultipartFile mockImage(String name) {
        return new MockMultipartFile("image", name, "image/jpeg", "image data".getBytes());
    }

    @Test
    void 상품업로드_테스트() {
        when(productMapper.insertProduct(any())).thenReturn(1);
        when(productMapper.existsMainImage(any())).thenReturn(false);
        when(productMapper.insertProductImage(any())).thenReturn(1);
        when(productMapper.existsCategoryById(1L)).thenReturn(true);

        String imageUrl = productImgService.upload(defaultProduct(), defaultImage(), mockImage("test.jpg"));

        assertNotNull(imageUrl);
        assertTrue(imageUrl.contains("test.jpg"));
    }

    @ExceptionTest(expected = ProductUploadException.class, message = "error.product.image.empty")
    void 상품비어있는지예외테스트() {
        MockMultipartFile file = new MockMultipartFile("file", "", "image/jpeg", new byte[0]);

        productImgService.upload(defaultProduct(), defaultImage(), file);
    }

    @Test
    void 이미지수정테스트() {
        Long productId = 123L;
        ProductUpdateDto updateDto = new ProductUpdateDto();
        updateDto.setCategoryId(1L);
        updateDto.setPrice(BigDecimal.valueOf(5000));
        updateDto.setProductMain(true);

        when(productMapper.existsProductById(productId)).thenReturn(true);
        when(productMapper.existsCategoryById(1L)).thenReturn(true);
        when(productMapper.updateProduct(any())).thenReturn(1);
        when(productMapper.selectMainImage(productId)).thenReturn(null);
        when(productMapper.insertProductImage(any())).thenReturn(1);

        assertDoesNotThrow(() ->
                productImgService.updateProduct(productId, updateDto, mockImage("new.jpg")));
    }

    @Test
    void 상품옵션추가테스트() {
        Long productId = 456L;
        ProductOptionDto optionDto = new ProductOptionDto();
        optionDto.setOptionName("Color");
        optionDto.setOptionValue("Red");

        when(productMapper.existsProductById(productId)).thenReturn(true);
        when(productMapper.insertProductOption(any())).thenReturn(1);

        assertDoesNotThrow(() -> productImgService.insertOption(productId, optionDto));
    }

    @ExceptionTest(expected = ProductUploadException.class, message = "error.product.option.exists")
    void 상품옵션예외테스트() {
        Long productId = 999L;
        ProductOptionDto optionDto = new ProductOptionDto();

        when(productMapper.existsProductById(productId)).thenReturn(false);

        productImgService.insertOption(productId, optionDto);
    }
}
