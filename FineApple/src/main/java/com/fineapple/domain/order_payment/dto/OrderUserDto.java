package com.fineapple.domain.order_payment.dto;

import com.fineapple.domain.user.dto.CartItemDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderUserDto {

    private Long orderId;
    private String orderCode;

    private LocalDateTime createdAt;
    private BigDecimal totalPrice;
    private BigDecimal finalPrice;
    private BigDecimal discountPrice;

    private String paymentMethod;
    private String paymentMethodName;
    private String orderStatus;
    private String orderStatusName;

    private Long guestId;
    private Long userId;
    private String userName;
    private String userPhone;
    private String userEmail;

    private String userAddress;
    private String userAddressDetail;

    private List<OrderItemDetailUserDto> orderItems;
//    private List<CartItemDto> orderItems;

    public Long cartId;

}
