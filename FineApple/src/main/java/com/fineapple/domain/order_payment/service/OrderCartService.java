package com.fineapple.domain.order_payment.service;

import com.fineapple.domain.order_payment.dto.OrderItemDetailUserDto;
import com.fineapple.domain.order_payment.dto.OrderUserDto;
import com.fineapple.domain.order_payment.repository.OrderCartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderCartService {

    private final OrderCartMapper orderCartMapper;

    //장바구니에서 주문 아이템을 가져옴
    protected List<OrderItemDetailUserDto> fetchItemsFromCart(OrderUserDto orderUserDto) {
        List<OrderItemDetailUserDto> orderItems = new ArrayList<>();

        if (orderUserDto.getUserId() != null) {
            orderItems = orderCartMapper.fetchItemsFromUserCart(orderUserDto.getUserId(), orderUserDto.getCartId());
        } else if (orderUserDto.getGuestId() != null) {
            orderItems = orderCartMapper.fetchItemsFromGuestCart(orderUserDto.getGuestId(), orderUserDto.getCartId());
        }

        if (orderItems.isEmpty()) {
            throw new IllegalArgumentException("장바구니에 주문 항목이 없습니다.");
        }
        return orderItems;
    }

    // 장바구니 비우기
    public void removeOrderedItemsFromCart(OrderUserDto orderUserDto) {
        if (orderUserDto.getUserId() != null) {
            orderCartMapper.removeOrderedItemsFromUserCart(orderUserDto.getUserId(), orderUserDto.getOrderItems());
        } else if (orderUserDto.getGuestId() != null) {
            orderCartMapper.removeOrderedItemsFromGuestCart(orderUserDto.getGuestId(), orderUserDto.getOrderItems());
        }
    }
}
