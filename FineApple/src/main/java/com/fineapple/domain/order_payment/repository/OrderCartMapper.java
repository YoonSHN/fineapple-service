package com.fineapple.domain.order_payment.repository;

import com.fineapple.domain.order_payment.dto.OrderItemDetailUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface OrderCartMapper {
    List<OrderItemDetailUserDto> fetchItemsFromGuestCart(@Param("guestId") Long guestId, @Param("cartId") Long cartId);
    List<OrderItemDetailUserDto> fetchItemsFromUserCart(@Param("userId") Long userId, @Param("cartId") Long cartId);

    void removeOrderedItemsFromUserCart(Long userId, List<OrderItemDetailUserDto> orderItems);
    void removeOrderedItemsFromGuestCart(Long guestId, List<OrderItemDetailUserDto> orderItems);
}
