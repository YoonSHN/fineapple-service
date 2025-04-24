package com.fineapple.domain.order_payment.service;

import com.fineapple.Infrastructure.exception.OrderItemDetailNotFoundException;
import com.fineapple.Infrastructure.exception.OrderNotFoundException;
import com.fineapple.Infrastructure.exception.RefundNotFoundException;
import com.fineapple.domain.order_payment.dto.*;
import com.fineapple.domain.order_payment.entity.Orders;
import com.fineapple.domain.order_payment.repository.OrdersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import java.util.List;
import java.util.Locale;
/**
 * 주문 및 환불 관련 관리자 조회 기능을 제공하는 서비스 구현체
 *
 * - 주문/주문상세/주문상태/환불 이력에 대한 관리자 페이지 전용 API 지원을 담당
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    private final OrdersMapper ordersMapper;
    private final MessageSource messageSource;

    private String getMessages(String code, Object... args) {
        return messageSource.getMessage(code, args, Locale.getDefault());
    }

    /**
     * 주문 단건 조회 (주문 ID 기반)
     *
     * @param id 주문ID
     */
    @Transactional(readOnly = true)
    @Override
    public OrderInfoDto selectOrderById(Long id) {
        return toOrderInfoDto(ordersMapper.selectOrderById(id));
    }


    /**
     * 페이징 기반 관리자페이지 주문 목록 조회
     *
     * @param pageNum          페이지 숫자
     * @param pageSize         페이지 크기
     * @param orderSearchParam 검색 정보
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderAllDto> findAllOrders(OrderSearchParam orderSearchParam, int pageNum, int pageSize) {
        orderSearchParam.setOffset((pageNum - 1) * pageSize);
        orderSearchParam.setPageSize(pageSize);

        int total = ordersMapper.countAllOrders(orderSearchParam);
        List<OrderAllDto> list = ordersMapper.selectAllOrders(orderSearchParam);
        PageInfo<OrderAllDto> pageInfo = new PageInfo<>(list);
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        pageInfo.setPages((int) Math.ceil((double) total / pageSize));

        return pageInfo;
    }

    /**
     * 페이징 기반 관리자페이지 주문 상태 목록 조회
     *
     * @param pageNum  페이지 숫자
     * @param pageSize 페이지 크기
     * @param orderId  주문 ID
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderStatusDto> findAllOrdersStatus(int pageNum, int pageSize, Long orderId) {
        if (!ordersMapper.existsOrderById(orderId)) {
            throw new OrderNotFoundException(
                    getMessages("error.order.not_found", orderId)
            );
        }
        PageHelper.startPage(pageNum, pageSize);
        List<OrderStatusDto> orderStatusDtos = ordersMapper.selectOrderStatusByOrderId(orderId);
        return new PageInfo<>(orderStatusDtos);
    }


    /**
     * 페이징 기반 관리자페이지 주문 상태 목록 조회
     *
     * @param pageNum       페이지 숫자
     * @param pageSize      페이지 크기
     * @param orderDetailId 주문상세 ID
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderItemHistoryDto> findAllOrdersDetailHistory(int pageNum, int pageSize, Long orderDetailId) {
        PageHelper.startPage(pageNum, pageSize);
        if (!ordersMapper.existsOrderItemDetailById(orderDetailId)) {
            throw new OrderItemDetailNotFoundException(
                    getMessages("error.order.item_detail.not_found", orderDetailId)
            );
        }
        List<OrderItemHistoryDto> orderItemHistoryDtoList = ordersMapper.selectOrderItemHistoryByOrderItemDetailId(orderDetailId);
        return new PageInfo<>(orderItemHistoryDtoList);
    }


    /**
     * 페이징 기반 관리자페이지 주문 상세 목록 조회
     *
     * @param pageNum  페이지 숫자
     * @param pageSize 페이지 크기
     * @param orderId  주문 ID
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<OrderAdminDto> findAllOrdersDetails(int pageNum, int pageSize, Long orderId) {
        PageHelper.startPage(pageNum, pageSize);
        if (!ordersMapper.existsOrderById(orderId)) {
            throw new OrderNotFoundException(
                    getMessages("error.order.not_found", orderId)
            );
        }
        List<OrderAdminDto> orderAdminDtos = ordersMapper.selectAllOrdersDetails(orderId);
        return new PageInfo<>(orderAdminDtos);
    }


    /**
     * 주문 상세 조회
     *
     * @param orderItemDetailId 주문 상세 ID
     */
    @Transactional(readOnly = true)
    @Override
    public OrderItemDetailAdminDto findOrderItemDetailByOrderItemDetailId(Long orderItemDetailId) {
        if (!ordersMapper.existsOrderItemDetailById(orderItemDetailId)) {
            throw new OrderItemDetailNotFoundException(
                    getMessages("error.order.item_detail.not_found", orderItemDetailId)
            );
        }
        OrderItemDetailAdminDto orderItemDetailAdminDto = ordersMapper.selectOrderItemDetailByOrderItemDetailId(orderItemDetailId);
        if (orderItemDetailAdminDto == null) {
            throw new OrderItemDetailNotFoundException(
                    getMessages("error.order.detail.null", orderItemDetailId)
            );
        }
        return orderItemDetailAdminDto;
    }

    /**
     * TODAY 주문 총량
     *
     * @param startDate 시작날짜
     * @param endDate   종료날짜
     */
    @Transactional(readOnly = true)
    @Override
    public Integer countOrdersByPeriod(LocalDate startDate, LocalDate endDate) {
        return ordersMapper.countOrdersByPeriod(startDate, endDate);
    }

    /**
     * 페이징 기반 관리자페이지 환불 목록 조회
     *
     * @param pageNum           페이지 숫자
     * @param pageSize          페이지 크기
     * @param refundSearchParam 검색정보
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<RefundDto> findAllRefund(RefundSearchParam refundSearchParam, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<RefundDto> refundDtos = ordersMapper.selectAllRefund(refundSearchParam);
        return new PageInfo<>(refundDtos);
    }

    /**
     * 페이징 기반 관리자페이지 환불 목록 조회
     *
     * @param refundId 환불아이디
     */
    @Transactional(readOnly = true)
    @Override
    public RefundDto findRefundDetails(Long refundId) {
        if (!ordersMapper.existsRefundById(refundId)) {
            throw new RefundNotFoundException(
                    getMessages("error.refund.not_found", refundId)
            );
        }
        return ordersMapper.selectRefundByIdWithDetails(refundId);
    }

    @Override
    public OrderInfoDto toOrderInfoDto(Orders orders) {
        return new OrderInfoDto(orders.getOrderId(), orders.getOrderCode(), orders.getTotalPrice(),
                orders.getDiscountPrice(), orders.getOrderStatus(), orders.getPaymentMethod());
    }


}
