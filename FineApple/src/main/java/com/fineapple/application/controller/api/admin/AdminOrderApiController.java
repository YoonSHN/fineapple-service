package com.fineapple.application.controller.api.admin;

import com.fineapple.domain.order_payment.dto.*;
import com.fineapple.domain.order_payment.service.OrderService;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 전용 주문 및 환불 관리 API 컨트롤러
 * <p>
 * - 관리자 권한(ROLE_ADMIN)을 가진 사용자만 접근 가능
 * - 주문 전체 목록, 상세 내역, 상태 변경 이력, 환불 요청/상세 정보 등을 조회가능
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminOrderApiController {

    private final OrderService orderService;

    @GetMapping("/orders")
    @Operation(summary = "주문 목록 조회")
    public ResponseEntity<PageInfo<OrderAllDto>> getAllOrders(
            @Validated @ModelAttribute OrderSearchParam orderSearchParam,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {


        PageInfo<OrderAllDto> orderPage = orderService.findAllOrders(orderSearchParam, pageNum, pageSize);
        return ResponseEntity.ok(orderPage);
    }

    @GetMapping("/{orderId}/orderItemDetails")
    @Operation(summary = "주문 상세 목록 조회")
    public ResponseEntity<PageInfo<OrderAdminDto>> getAllOrdersDetails(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @PathVariable Long orderId) {

        PageInfo<OrderAdminDto> ordersDetailsPage = orderService.findAllOrdersDetails(pageNum, pageSize, orderId);
        return ResponseEntity.ok(ordersDetailsPage);
    }

    @GetMapping("/orders/orderItemDetails/{orderItemDetailId}")
    @Operation(summary = "주문 상세 조회")
    public ResponseEntity<OrderItemDetailAdminDto> getOrderDetail(@PathVariable Long orderItemDetailId) {
        OrderItemDetailAdminDto orderItemDetailAdminDto = orderService.findOrderItemDetailByOrderItemDetailId(orderItemDetailId);
        return ResponseEntity.ok(orderItemDetailAdminDto);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "주문 상태 변경 이력 목록 조회")
    public ResponseEntity<PageInfo<OrderStatusDto>> getAllOrdersStatus(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @PathVariable Long orderId) {

        PageInfo<OrderStatusDto> ordersStatusPage = orderService.findAllOrdersStatus(pageNum, pageSize, orderId);
        return ResponseEntity.ok(ordersStatusPage);
    }

    @GetMapping("/orders/itemDetail/{orderItemDetailId}/history")
    @Operation(summary = "주문 상세변경 이력 목록 조회")
    public ResponseEntity<PageInfo<OrderItemHistoryDto>> getAllOrderItemDetailHistory(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @PathVariable Long orderItemDetailId) {

        PageInfo<OrderItemHistoryDto> orderHistoryPage = orderService.findAllOrdersDetailHistory(pageNum, pageSize, orderItemDetailId);
        return ResponseEntity.ok(orderHistoryPage);
    }

    @GetMapping("/refund")
    @Operation(summary = "환불 요청 목록 조회")
    public ResponseEntity<PageInfo<RefundDto>> getAllRefund(
            @Validated @ModelAttribute RefundSearchParam refundSearchParam,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        PageInfo<RefundDto> refundPage = orderService.findAllRefund(refundSearchParam, pageNum, pageSize);
        return ResponseEntity.ok(refundPage);
    }

    @GetMapping("/refund/{refundId}")
    @Operation(summary = "환불 상세 조회")
    public ResponseEntity<RefundDto> getRefundDetails(@PathVariable Long refundId) {
        RefundDto refundDto = orderService.findRefundDetails(refundId);
        return ResponseEntity.ok(refundDto);
    }

}
