package com.fineapple.domain.user.dto;

import com.fineapple.domain.user.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {

    private Long cartId;
    private Long userId;

    private List<CartItemDto> cartProducts;

    //카트내 총 상품 가격
    public BigDecimal getTotalprice() {
        BigDecimal totalprice = BigDecimal.ZERO;
        if (cartProducts != null) {
            for (CartItemDto cartItem : cartProducts) {
                totalprice = totalprice.add(cartItem.getSubTotal());
            }
            return totalprice;
        }return BigDecimal.ZERO;
    }

}
