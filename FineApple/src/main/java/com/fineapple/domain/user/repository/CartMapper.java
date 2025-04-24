package com.fineapple.domain.user.repository;

import com.fineapple.domain.user.dto.CartDto;
import com.fineapple.domain.user.dto.CartItemDto;
import com.fineapple.domain.user.entity.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CartMapper {

    void deleteCartByUserId(Long userId);

    //cartItem
    void insertCartItem(CartItemDto cartItemDto);
    CartItemDto findCartItemByProductId(long cartId, long productId);
    void updateCartItemQuantity(Long productId, Long quantity);
    void updateCartItem(Long cartId, Long productId, Long quantity);
    void deleteCartByProductId(Long productId);
    void deleteCart(Long cartId);   //cart 전체 삭제


    //cart
    //게스트인지 확인용
    boolean isGuest(Long userId);

    CartDto findByUserId(Long userId);
    CartDto findByGuestId(Long guestId);

    void insertCart(Long userId);
    void insertCartGuest(Long userId);


    List<CartItemDto> findCartItems(Long cartId);

}
