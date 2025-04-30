package com.fineapple.domain.user.service;

import com.fineapple.application.exception.CartNotFoundException;
import com.fineapple.application.exception.ProductNotFoundException;
import com.fineapple.domain.product.dto.ProductDetailDto;
import com.fineapple.domain.product.repository.ProductMapper;
import com.fineapple.domain.user.dto.CartDto;
import com.fineapple.domain.user.dto.CartItemDto;
import com.fineapple.domain.user.repository.CartMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartMapper cartMapper;
    private final ProductMapper productMapper;

    //cart 조회
    @Transactional
    @Override
    public CartDto getCart(Long userId) {
        CartDto cartDto = null;

        //게스트 체크
        boolean isGuest = cartMapper.isGuest(userId);

        //유저와 게스트의 구분 유저 -> 게스트아이디 컬럼 null
        //게스트 -> 유저아이디 컬럼 null
        if (!isGuest) {
            cartDto = cartMapper.findByUserId(userId);
            if (cartDto == null) { //유저의 장바구니가 생성된게 없다.
                cartMapper.insertCart(userId);
                cartDto = cartMapper.findByUserId(userId);
            }
        }else{ // 유저 아이디 = null -> 게스트용으로 생성
            cartDto = cartMapper.findByGuestId(userId);
            if(cartDto == null) { // 게스트의 장바구니가 생성된게 없다.
                cartMapper.insertCartGuest(userId);
                cartDto = cartMapper.findByGuestId(userId);
            }
        }
        return new CartDto(
                cartDto.getCartId(),
                cartDto.getUserId(),
                cartMapper.findCartItems(cartDto.getCartId())
        );
    }



    //cartItem 조회
    @Override
    public List<CartItemDto> getCartItems(Long userId) {
        //장바구니 보유 여부 확인
        boolean isGuest = (userId == null) || cartMapper.isGuest(userId);
        CartDto cart = isGuest ? cartMapper.findByGuestId(userId) : cartMapper.findByUserId(userId);

        if (cart == null) {
            throw new CartNotFoundException ("장바구니를 찾을 수 없습니다." + userId);
        }

        return cartMapper.findCartItems(cart.getCartId());
    }

    //addCartItem
    @Transactional
    @Override
    public void addToItem(Long userId, Long productId, Long quantity) {
        //장바구니 보유 여부 확인
        boolean isGuest = (userId == null) || cartMapper.isGuest(userId);
        CartDto cart = isGuest ? cartMapper.findByGuestId(userId) : cartMapper.findByUserId(userId);

        if (cart == null) { // 장바구니 없으면 생성
            if (isGuest) {
                cartMapper.insertCartGuest(userId);
                cart = cartMapper.findByGuestId(userId);
            } else {
                cartMapper.insertCart(userId);
                cart = cartMapper.findByUserId(userId);
            }
        }

        //제품 정보 가져오기
        ProductDetailDto product = productMapper.findProductById(productId);
        if(product == null) {
            throw new ProductNotFoundException("제품을 찾을 수 없습니다." + productId);
        }
        //cartItemDto 생성을 위한 가격정보 받아와 새로운 dto 생성
        BigDecimal productPrice = product.getPrice();

        CartItemDto newCartItem = new CartItemDto(
                cart.getCartId(),
                quantity,
                product.getImageUrl(),
                product.getName(),
                productPrice,
                product.getProductId(),
                null
        );

        CartItemDto cartItemDto = cartMapper.findCartItemByProductId(cart.getCartId(), productId);
        if (cartItemDto == null) { //같은 상품이 없으면
            //새로 추가
            cartMapper.insertCartItem(newCartItem);

        } else{ //있으면 기존 상품 수량+
            cartMapper.updateCartItemQuantity(cartItemDto.getProductId(), quantity);
        }
    }

    @Transactional
    @Override
    public void updateItem(Long userId, Long productId, Long quantity) {
        // 장바구니 보유 여부 확인
        boolean isGuest = (userId == null) || cartMapper.isGuest(userId);
        CartDto cart = isGuest ? cartMapper.findByGuestId(userId) : cartMapper.findByUserId(userId);

        if (cart == null) {
            throw new CartNotFoundException("장바구니를 찾을 수 없습니다. userId: " + userId);
        }

        CartItemDto cartItem = cartMapper.findCartItemByProductId(cart.getCartId(), productId);
        if (cartItem == null) {
            throw new ProductNotFoundException("장바구니에 해당 상품이 존재하지 않습니다. productId: " + productId);
        }

        // 수량 업데이트
        cartMapper.updateCartItem(cart.getCartId(), productId, quantity);
    }

    //장바구니 상품 단건 삭제
    @Transactional
    @Override
    public void removeCartItem(Long userId, Long productId) {
        //장바구니 보유 여부 확인
        boolean isGuest = (userId == null) || cartMapper.isGuest(userId);
        CartDto cart = isGuest ? cartMapper.findByGuestId(userId) : cartMapper.findByUserId(userId);

        if (cart == null) {
            throw new CartNotFoundException ("장바구니를 찾을 수 없습니다." + userId);
        }
        CartItemDto cartItemDto = cartMapper.findCartItemByProductId(cart.getCartId(), productId);
        if (cartItemDto == null) {
            throw new ProductNotFoundException("제품을 찾을 수 없습니다." + productId);
        }

        cartMapper.deleteCartByProductId(productId);
    }

    //장바구니 초기화(장바구니 전체 삭제)
    @Transactional
    @Override
    public void initializeCart(Long userId) {
        //장바구니 보유 여부 확인
        boolean isGuest = (userId == null) || cartMapper.isGuest(userId);
        CartDto cart = isGuest ? cartMapper.findByGuestId(userId) : cartMapper.findByUserId(userId);

        if (cart == null) {
            throw new CartNotFoundException ("장바구니를 찾을 수 없습니다." + userId);
        }
        cartMapper.deleteCart(cart.getCartId());    //cart삭제
        //cart 재생성
        if (isGuest) {
            cartMapper.insertCartGuest(userId);
        } else {
            cartMapper.insertCart(userId);
        }
    }

}
