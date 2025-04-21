package com.fineapple.domain.order_payment.service;

import com.fineapple.domain.order_payment.repository.OrderUserMapper;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class OrderCodeGenerator {

    private final OrderUserMapper orderUserMapper;
    private final Random random = new Random();

    public OrderCodeGenerator(OrderUserMapper orderUserMapper) {
        this.orderUserMapper = orderUserMapper;
    }

    // 주문 코드 생성
    protected Long generateOrderCode() {
        Long orderCode;
        do {
            orderCode = createUniqueOrderCode();
        } while (orderUserMapper.existsByOrderCode(orderCode)); // DB에서 중복 확인

        return orderCode;
    }
    private Long createUniqueOrderCode() {
        // 현재 날짜를 yyyyMMddHHmmss (14자리)로 변환
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // 4~5자리 랜덤 숫자 생성 (1000 ~ 99999)
        int randomNumber = 1000 + random.nextInt(90000);
        // 최종 주문 코드(Long 타입)
        return Long.parseLong(timestamp + randomNumber);
    }
}
