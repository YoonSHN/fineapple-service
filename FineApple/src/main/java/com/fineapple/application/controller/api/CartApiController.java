package com.fineapple.application.controller.api;

import com.fineapple.domain.user.dto.CartDto;
import com.fineapple.domain.user.dto.CartItemDto;
import com.fineapple.domain.user.dto.CartUpdateRequest;
import com.fineapple.domain.user.service.CartServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/carts")
public class CartApiController {
    private final CartServiceImpl cartServiceImpl;


    // 장바구니 조회 ("/{userId}")
    @GetMapping("/{userId}")
    public CartDto getCart(@PathVariable Long userId) {
        return cartServiceImpl.getCart(userId);
    }

    // 장바구니 상품 목록 조회 "/{userId}/items"
    @GetMapping("/{userId}/items")
    public List<CartItemDto> getCartItems(@PathVariable Long userId) {
        return cartServiceImpl.getCartItems(userId);
    }

    // 장바구니에 상품 추가
    @PostMapping("/{userId}/items")
    public void addCartItem(@PathVariable Long userId, @RequestParam Long productId, @RequestParam Long quantity) {
         cartServiceImpl.addToItem(userId, productId, quantity);
    }

    //장바구니 물건 수량 업데이트
    @PutMapping("/{userId}/{productId}")
    public void updateCartItem(@PathVariable Long userId, @PathVariable Long productId, @RequestBody CartUpdateRequest cartUpdateRequest) {
        Long quantity = cartUpdateRequest.getQuantity();
        cartServiceImpl.updateItem(userId, productId, quantity);
    }

    // 장바구니에서 특정 상품 제거
    @DeleteMapping("/{userId}/items/{productId}")
    public void removeCartItem(@PathVariable Long userId, @PathVariable Long productId) {
        cartServiceImpl.removeCartItem(userId, productId);
    }

    // 장바구니 초기화
    @DeleteMapping("/{userId}")
    public void initializeCart(@PathVariable Long userId) {
        cartServiceImpl.initializeCart(userId);
    }


}
