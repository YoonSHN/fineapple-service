package com.fineapple.application.controller;

import com.fineapple.domain.order_payment.dto.*;
import com.fineapple.domain.order_payment.service.OrderUserService;
import com.fineapple.domain.user.dto.UserDetailDto;
import com.fineapple.domain.user.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@AllArgsConstructor
public class OrderUserController {

    private final OrderUserService orderUserService;

    @GetMapping("/api/v1/orders/{id}")
    @Operation(summary = "회원/비회원의 주문 목록 조회")
    public ResponseEntity<PageInfo<OrderUserDto>> getUserRecentOrders(
            @PathVariable("id") Long id,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<OrderUserDto> list = orderUserService.selectRecentOrdersById(id, pageNum, pageSize);
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "결제 후 주문 번호로 주문 상세 내역 조회", description = "주문 번호로 특정 사용자의 주문 상세 정보를 조회합니다.")
    @GetMapping("/api/v1/orders/{orderCode}/orderItemDetails")
    public ResponseEntity<OrderUserDto> getOrderDetail(@PathVariable String orderCode) {
        Long orderCodeLong = Long.parseLong(orderCode);
        OrderUserDto order = orderUserService.selectOrderItemDetailByOrderCode(orderCodeLong);

        if (order == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 주문 정보를 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(order);
    }

    @Operation(summary = "주문 생성")
    @PostMapping("/api/v1/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderUserDto orderUserDto) {
        if (orderUserDto == null) {
            throw new IllegalArgumentException("orderUserDto가 null입니다.");
        }
        // 전체 객체 출력
        System.out.println("📦 orderUserDto: " + orderUserDto);

        // 개별 필드 확인
        System.out.println("🛍 주문 항목 개수: " + (orderUserDto.getOrderItems() != null ? orderUserDto.getOrderItems().size() : 0));

        // orderItems 내부 데이터 확인
        if (orderUserDto.getOrderItems() != null) {
            for (OrderItemDetailUserDto item : orderUserDto.getOrderItems()) {
                System.out.println("🛍 상품명: " + item.getItemName());
                System.out.println("📦 수량: " + item.getItemQuantity());
                System.out.println("💰 가격: " + item.getItemPrice());
            }
        } else {
            System.out.println("⚠ orderItems가 null입니다.");
        }

        OrderResponseDto response = orderUserService.createOrder(orderUserDto);

        Long orderId = response.getOrderId();
        String orderCode = response.getOrderCode();

        System.out.println("orderId: " + orderId);
        System.out.println("orderCode: " + orderCode);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문 시 회원 정보 가져오기")
    @GetMapping("/api/v1/users/{userId}")
    public ResponseEntity<OrderUserInfoDto> getUserInfo(@PathVariable Long userId) {
        OrderUserInfoDto userInfo = orderUserService.getOrderUserInfo(userId);
        return ResponseEntity.ok(userInfo);

    }

    @Operation(summary = "결제 후 결제정보 저장")
    @PostMapping("/api/v1/payments/complete")
    public ResponseEntity<String> completePayment(@RequestBody PaymentCompleteRequestDto request) {
        try {
            orderUserService.completeOrderAfterPayment(request.getOrderId(), request.getImpUid());
            return ResponseEntity.ok("결제 완료 처리 성공");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("요청 파라미터 오류: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("결제 완료 처리 중 오류 발생: " + e.getMessage());
        }
    }



}
