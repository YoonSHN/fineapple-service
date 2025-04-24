package com.fineapple.domain.order_payment.service;

import com.fineapple.domain.order_payment.dto.OrderItemDetailUserDto;
import com.fineapple.domain.order_payment.repository.OrderStockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStockService {

    private final OrderStockMapper orderStockMapper;

    public void reduceStock(List<OrderItemDetailUserDto> orderItems) {
        for (OrderItemDetailUserDto item : orderItems) {
            orderStockMapper.decreaseStock(item.getProductId(), item.getItemQuantity());
        }
    }
}
