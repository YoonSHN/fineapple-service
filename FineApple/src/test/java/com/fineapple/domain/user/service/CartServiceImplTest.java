package com.fineapple.domain.user.service;

import com.fineapple.domain.product.dto.ProductDetailDto;
import com.fineapple.domain.product.repository.ProductMapper;
import com.fineapple.domain.user.dto.CartDto;
import com.fineapple.domain.user.dto.CartItemDto;
import com.fineapple.domain.user.repository.CartMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceImplTest {

    @Mock
    private CartMapper cartMapper;

    @Mock
    private ProductMapper productMapper;

    private CartServiceImpl cartServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartServiceImpl = new CartServiceImpl(cartMapper, productMapper);
    }

    @Test
    void testAddToItem() {
        // Arrange
        Long userId = 1L;
        Long productId = 101L;
        Long quantity = 2L;

        // Mocking CartMapper to return a cart for the given userId
        CartDto cartDto = new CartDto(1L, userId, null);
        when(cartMapper.findByUserId(userId)).thenReturn(cartDto);

        // Mocking ProductMapper to return a ProductDetailDto for the given productId
        ProductDetailDto productDetail = new ProductDetailDto(
                productId, "imageUrl", "productName", "description",
                new BigDecimal("10.99"), "2025-05-01", "2025-06-01", "category", true
        );
        when(productMapper.findProductById(productId)).thenReturn(productDetail);

        // Mocking CartMapper to find no existing cart item for this productId
        when(cartMapper.findCartItemByProductId(cartDto.getCartId(), productId)).thenReturn(null);

        // Act
        cartServiceImpl.addToItem(userId, productId, quantity);

        // Assert
        // Verify if insertCartItem was called
        verify(cartMapper).insertCartItem(any(CartItemDto.class));
    }
}