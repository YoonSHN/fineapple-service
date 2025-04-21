package com.fineapple.domain.order_payment.service;

import com.fineapple.Infrastructure.exception.OrderNotFoundException;
import com.fineapple.domain.order_payment.dto.OrderResponseDto;
import com.fineapple.domain.order_payment.dto.OrderUserDto;
import com.fineapple.domain.order_payment.dto.OrderUserInfoDto;
import com.fineapple.domain.order_payment.repository.OrderStockMapper;
import com.fineapple.domain.order_payment.repository.OrderUserMapper;
import com.fineapple.domain.order_payment.repository.OrderCartMapper;
import com.github.pagehelper.PageInfo;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderUserService {

    private final OrderUserMapper orderUserMapper;
    private final OrderStockMapper orderStockMapper;
    private final OrderCartMapper orderCartMapper;
    private final OrderPaymentService orderPaymentService;
    private final OrderValidator orderValidator;
    private final OrderCodeGenerator orderCodeGenerator;
    private final OrderCartService orderCartService;
    private final OrderStockService orderStockService;
    private final IamportClient iamportClient;

    private final Map<String, String> codeNameCache = new ConcurrentHashMap<>();

    private String getCodeNameCached(String code) {
        if (code == null) return null;
        return codeNameCache.computeIfAbsent(code, key -> {
            String result = orderUserMapper.getCommonName(key);
            return result != null ? result : key; // 값 없으면 코드 자체 반환
        });
    }

    /**
     * 회원/비회원의 주문 목록 조회
     *
     * @param id       회원 혹은 비회원 ID
     * @param pageNum  페이지 번호
     * @param pageSize 페이지 크기
     */
    public PageInfo<OrderUserDto> selectRecentOrdersById(Long id, int pageNum, int pageSize) {
        Map<String, Long> params = new HashMap<>();

        if (id != null) {
            params.put("userId", id);
        } else {
            params.put("guestId", id);
        }

        List<OrderUserDto> orders = orderUserMapper.selectRecentOrdersById(params);

        for (OrderUserDto order : orders) {
            order.setOrderStatusName(getCodeNameCached(order.getOrderStatus()));
            order.setPaymentMethodName(getCodeNameCached(order.getPaymentMethod()));
        }

        Map<String, OrderUserDto> uniqueOrderMap = new LinkedHashMap<>();
        for (OrderUserDto order : orders) {
            uniqueOrderMap.putIfAbsent(order.getOrderCode(), order);
        }

        List<OrderUserDto> distinctOrders = new ArrayList<>(uniqueOrderMap.values());

        int total = distinctOrders.size();
        int fromIndex = Math.min((pageNum - 1) * pageSize, total);
        int toIndex = Math.min(fromIndex + pageSize, total);
        List<OrderUserDto> pagedOrders = distinctOrders.subList(fromIndex, toIndex);

        PageInfo<OrderUserDto> pageInfo = new PageInfo<>(pagedOrders);
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages((int) Math.ceil((double) total / pageSize));

        return pageInfo;
    }

    /**
     * 회원/비회원의 주문상세정보 조회
     *
     * @param orderCode 주문코드
     */
    public OrderUserDto selectOrderItemDetailByOrderCode(Long orderCode) {
        OrderUserDto order = orderUserMapper.selectOrderItemDetailByOrderCode(orderCode);

        if (order != null) {
            String statusCode = order.getOrderStatus();
            String paymentCode = order.getPaymentMethod();

            order.setOrderStatusName(getCodeNameCached(statusCode));
            order.setPaymentMethodName(getCodeNameCached(paymentCode));

            log.info("상태 코드: {}", order.getOrderStatus());
            log.info("상태명: {}", getCodeNameCached(order.getOrderStatus()));
            log.info("상태 코드: {}", order.getPaymentMethod());
            log.info("상태명: {}", getCodeNameCached(order.getPaymentMethod()));

        }

        return order;
    }


    /**
     * 주문 ID로 주문 정보 조회
     *
     * @param orderId 주문 ID
     * @return 주문 DTO
     */
    public OrderUserDto getOrderById(Long orderId) {
        OrderUserDto order = orderUserMapper.selectOrderById(orderId);

        if (order == null) {
            return null;
        }
        return order;
    }

    /**
     * 주문 생성 목적 유저 정보 가져오기
     * @param userId
     * @return
     */
    public OrderUserInfoDto getOrderUserInfo(Long userId) {
        OrderUserInfoDto dto = orderUserMapper.getUserInfo(userId);
        if (dto == null) {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
        return dto;
    }
    /**
     * 회원/비회원 주문 생성
     * @param orderUserDto 주문 정보 (OrderUserDto)
     * @return 생성된 주문 코드
     */
    @Transactional
    public OrderResponseDto createOrder(OrderUserDto orderUserDto) {

        orderValidator.validateUser(orderUserDto.getUserId(), orderUserDto.getGuestId());

//        List<OrderItemDetailUserDto> orderItems = orderCartService.fetchItemsFromCart(orderUserDto);
//        orderUserDto.setOrderItems(orderItems);
        orderValidator.validateOrder(orderUserDto);

        Long orderCode = orderCodeGenerator.generateOrderCode();
        orderUserDto.setOrderCode(orderCode.toString());
        log.info(orderCode.toString());

        orderUserMapper.insertOrderAndItems(orderUserDto);
        Long orderId = orderUserDto.getOrderId();
        log.info(orderId.toString());

        if (orderId == null) {
            throw new IllegalStateException("주문 ID 생성 실패");
        }

        insertOrderStatus(orderId);

        orderUserMapper.insertOrderItemDetails(orderId, orderUserDto.getOrderItems());

//        validatePayment(orderUserDto.getTotalPrice(), orderUserDto.getDiscountPrice(), orderUserDto.getPaymentMethod(), orderUserDto);

        orderCartService.removeOrderedItemsFromCart(orderUserDto);
        orderStockService.reduceStock(orderUserDto.getOrderItems());
        log.info(orderUserDto.toString());
        return new OrderResponseDto(orderId, orderCode.toString());
    }

    public void insertOrderStatus(Long orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("orderId가 null입니다.");
        }
        String orderStatus = "OR0401"; // 주문 완료
        String paymentStatus = "OR0201"; // 결제 대기
        orderUserMapper.insertOrderStatus(orderId, orderStatus, paymentStatus);
    }

    /**
     * 결제 완료 후 호출되는 로직
     * - 결제 정보 저장
     * - 주문 상태 '결제 완료'로 갱신
     * - 출고 요청 (물류 이동 승인 대기 상태)
     */
    @Transactional(rollbackFor = Exception.class)
    public void completeOrderAfterPayment(Long orderId, String impUid) {
        try {
            if (orderId == null || impUid == null || impUid.isBlank()) {
                throw new OrderNotFoundException("유효하지 않은 주문 ID 또는 결제 식별자입니다.");
            }

            OrderUserDto order = orderUserMapper.selectOrderById(orderId);
            if (order == null) {
                throw new OrderNotFoundException("주문이 존재하지 않습니다.");
            }

            // 결제 정보 조회
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
            Payment iamportPayment = response.getResponse();

            if (iamportPayment == null || !"paid".equalsIgnoreCase(iamportPayment.getStatus())) {
                log.info("결제 실패: orderId = {}, impUid = {}", orderId, impUid);
                orderUserMapper.updateOrderStatus(orderId, "OR0204");
                return;
            }

            // 결제 정보 저장
            orderPaymentService.savePaymentInfo(orderId, impUid, orderUserMapper.findOrderItemByOrderId(orderId));

            // 주문 상태 변경
            orderUserMapper.updateOrderStatus(orderId, "OR0202");

            // 출고 요청
            // orderUserMapper.insertShipmentRequest(orderId, "ST0701");
        } catch (Exception e) {
            log.info("결제 처리 중 오류 발생: orderId = {}, impUid = {}, error = {}", orderId, impUid, e.getMessage(), e);
            orderUserMapper.updateOrderStatus(orderId, "OR0204");
            throw new RuntimeException("결제 확인 중 예외 발생: " + e.getMessage(), e);
        }
    }


// 주문취소(결제 취소 요청 시)
//1. 주문 정보 조회
// - 주문 코드로 주문 정보 조회
// - 주문 상태(orderStatus), 결제 상태(paymentStatus), 배송 상태(deliveryStatus)를 확인
// - 각 상태에 따라 취소가 가능한지 여부를 결정

// - 주문 상태: orderStatus가 "주문 취소 완료(OR0104)"인 경우 취소 불가.
// - 결제 상태 확인 (결제 했는지)


//2. 주문 상태 '주문 취소 요청'으로 변경
//3. 관리자 검수


//4. 환불
// - 검수 시에 환불이 결정되면, 주문 상태를 "환불 승인 상태(OR0302)"로 변경
//   전액 환불(이체, 쿠폰 발급)후, 주문 상태를 "주문 취소 완료(OR0104)"로 변경

// - 환불 실패: 환불이 실패할 경우, 주문 상태를 "환불 거절 상태(OR0303)"로 변경


//5. 재고 복구

}

