package com.fineapple.domain.order_payment.service;

import com.fineapple.domain.order_payment.dto.*;
import com.fineapple.domain.order_payment.entity.Orders;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderInfoDto selectOrderById(Long id);

    @Transactional(readOnly = true)
    PageInfo<OrderAllDto> findAllOrders(OrderSearchParam orderSearchParam, int pageNum, int pageSize);

    @Transactional(readOnly = true)
    PageInfo<OrderStatusDto> findAllOrdersStatus(int pageNum, int pageSize, Long orderId);

    @Transactional(readOnly = true)
    PageInfo<OrderItemHistoryDto> findAllOrdersDetailHistory(int pageNum, int pageSize, Long orderDetailId);

    @Transactional(readOnly = true)
    PageInfo<OrderAdminDto> findAllOrdersDetails(int pageNum, int pageSize, Long orderId);

    @Transactional(readOnly = true)
    OrderItemDetailAdminDto findOrderItemDetailByOrderItemDetailId(Long orderItemDetailId);

    @Transactional(readOnly = true)
    Integer countOrdersByPeriod(LocalDate startDate, LocalDate endDate);

    OrderInfoDto toOrderInfoDto(Orders orders);

    @Transactional(readOnly = true)
    PageInfo<RefundDto> findAllRefund(RefundSearchParam refundSearchParam, int pageNum, int pageSize);

    @Transactional(readOnly = true)
    RefundDto findRefundDetails(Long refundId);
}