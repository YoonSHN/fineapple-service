package com.fineapple.service;

import com.fineapple.domain.order_payment.dto.OrderInfoDto;
import com.fineapple.domain.order_payment.service.OrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    public void testSelectOrderById() {

        Long insertedId = 1L;
        OrderInfoDto orderInfoDto = orderService.selectOrderById(insertedId);

        assertNotNull(orderInfoDto, "조회된 주문 정보는 null이 아니어야 합니다.");
        assertEquals(insertedId, orderInfoDto.orderId());
        assertEquals(2024040500001L, orderInfoDto.orderCode());
        assertEquals(new BigDecimal("14085332.00"), orderInfoDto.totalPrice());
        assertEquals(new BigDecimal("119601.00"), orderInfoDto.discountPrice());
        assertEquals("OR0204", orderInfoDto.orderStatus());
        assertEquals("OR0502", orderInfoDto.paymentStatus(), "테스트 성공 조건을 만족하지 않습니다.");
    }

}