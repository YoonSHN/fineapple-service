package com.fineapple.domain.order_payment.service;

import com.fineapple.domain.order_payment.dto.OrderItemDetailUserDto;
import com.fineapple.domain.order_payment.dto.OrderUserDto;
import com.fineapple.domain.order_payment.repository.OrderStockMapper;
import com.fineapple.domain.order_payment.repository.OrderUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
public class OrderValidator {

    private final OrderUserMapper orderUserMapper;
    private final OrderStockMapper orderStockMapper;

    public OrderValidator(OrderUserMapper orderUserMapper, OrderStockMapper orderStockMapper) {
        this.orderUserMapper = orderUserMapper;
        this.orderStockMapper = orderStockMapper;
    }

    // 사용자 검증 (회원/비회원)
    public void validateUser(Long userId, Long guestId) {
        if (userId == null && guestId == null) {
            throw new IllegalArgumentException("사용자 ID가 제대로 조회되지 않습니다.");
        }
        if (userId != null && guestId != null) {
            throw new IllegalArgumentException("회원 ID와 비회원 ID가 동시에 존재할 수 없습니다.");
        }
    }

    // 주문 유효성 검사
    public void validateOrder(OrderUserDto orderUserDto) {
        validateUser(orderUserDto.getUserId(), orderUserDto.getGuestId());
        validateOrderItems(orderUserDto.getOrderItems());
        validatePayment(orderUserDto.getTotalPrice(), orderUserDto.getDiscountPrice(), orderUserDto.getPaymentMethod(), orderUserDto);
//        validateDelivery(orderUserDto.getDeliveryId());
    }

    // 주문 항목 검증 (상품 유효성 + 재고 체크)
    private void validateOrderItems(List<OrderItemDetailUserDto> orderItems) {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("주문 항목이 없습니다.");
        }
        for (OrderItemDetailUserDto item : orderItems) {
            System.out.println("주문 아이템: " + item.getItemName() + ", 수량: " + item.getItemQuantity() + ", 가격: " + item.getItemPrice());
            if (item.getProductId() == null || item.getItemQuantity() == null || item.getItemQuantity() <= 0 || item.getItemPrice() == null || item.getItemPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("상품 정보가 올바르지 않습니다.");
            }
            String productName = orderUserMapper.getProductNameById(item.getProductId());
            if (productName == null || productName.trim().isEmpty()) {
                throw new IllegalArgumentException("상품명이 존재하지 않습니다: product_id = " + item.getProductId());
            }
            item.setItemName(productName);
            System.out.println("상품명 조회: product_id = " + item.getProductId() + ", productName = " + productName);

            if (item.getOptionId() == null) {
                System.out.println("optionId가 없어서 기본값 1을 설정합니다.");
                item.setOptionId(1L); // optionId를 1로 하드코딩
            }
            if (item.getItemQuantity() == null || item.getItemQuantity() <= 0) {
                throw new IllegalArgumentException("상품 수량은 1개 이상이어야 합니다.");
            }
            if (item.getItemPrice() == null || item.getItemPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("상품 가격은 0보다 커야 합니다.");
            }
            // 재고 체크
            int availableStock = orderStockMapper.selectStockByProductId(item.getProductId());
            System.out.println("상품 ID: " + item.getProductId() + " / 재고 수량: " + availableStock + " / 주문 수량: " + item.getItemQuantity());

            if (availableStock <= 5) {
                log.warn("STOCK_LOW | productId={} | stock={}", item.getProductId(), availableStock);
            }

            if (availableStock == 0 || availableStock < item.getItemQuantity()) {
                throw new IllegalArgumentException("상품 " + item.getProductId() + "의 재고가 부족합니다."); // 프론트에 뜨게 하기
            }
        }
    }

    /**
     * 결제 정보 검증
     */
    private void validatePayment(BigDecimal totalPrice, BigDecimal discountPrice, String paymentMethod, OrderUserDto orderUserDto) {
        if (totalPrice == null || totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("총 결제 금액은 0보다 커야 합니다.");
        }
        if (discountPrice != null && discountPrice.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("할인 금액이 총 금액보다 클 수 없습니다.");
        }
        // 할인된 결제 금액 계산
        BigDecimal finalPrice = totalPrice.subtract(discountPrice != null ? discountPrice : BigDecimal.ZERO);
        // 결제 금액을 클라이언트에 전달할 수 있도록 설정
        orderUserDto.setFinalPrice(finalPrice);

        List<String> validPaymentMethods = List.of("OR0501", "OR0502");
        if (paymentMethod == null || !validPaymentMethods.contains(paymentMethod)) {
            throw new IllegalArgumentException("유효한 결제 방식이 아닙니다.");
        }
    }

}
