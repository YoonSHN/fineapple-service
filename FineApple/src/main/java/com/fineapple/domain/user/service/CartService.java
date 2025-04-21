package com.fineapple.domain.user.service;

import com.fineapple.domain.user.dto.CartDto;
import com.fineapple.domain.user.dto.CartItemDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface CartService {

    //cart 조회
    @Transactional
    CartDto getCart(Long userId);

    //cartItem 조회
    List<CartItemDto> getCartItems(Long userId);

    //addCartItem
    @Transactional
    void addToItem(Long userId, Long productId, Long quantity);

    @Transactional
    void updateItem(Long userId, Long productId, Long quantity);

    //장바구니 상품 단건 삭제
    @Transactional
    void removeCartItem(Long userId, Long productId);

    //장바구니 초기화(장바구니 전체 삭제)
    @Transactional
    void initializeCart(Long userId);
}
