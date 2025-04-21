package com.fineapple.domain.order_payment.repository;

import com.fineapple.domain.order_payment.dto.*;
import com.fineapple.domain.order_payment.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface OrdersMapper {

    Orders selectOrderById(Long id);

    void insertOrder(Orders orders);

    List<OrderStatusDto> selectOrderStatusByOrderId(Long orderId);

    List<OrderItemHistoryDto> selectOrderItemHistoryByOrderItemDetailId(Long orderItemDetailId);

    List<OrderAllDto> selectAllOrders(OrderSearchParam orderSearchParam);

    List<OrderAdminDto> selectAllOrdersDetails(Long orderId);

    OrderItemDetailAdminDto selectOrderItemDetailByOrderItemDetailId(Long orderItemDetailId);

    Integer countOrdersByPeriod(LocalDate startDate, LocalDate endDate);

    List<RefundDto> selectAllRefund(RefundSearchParam refundSearchParam);

    RefundDto selectRefundByIdWithDetails(Long refundId);

    int countAllOrders(OrderSearchParam orderSearchParam);

    boolean existsOrderItemDetailById(Long orderDetailId);

    boolean existsOrderById(Long orderId);

    boolean existsRefundById(Long refundId);

}
