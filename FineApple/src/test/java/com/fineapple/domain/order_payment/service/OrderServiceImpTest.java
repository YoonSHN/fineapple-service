package com.fineapple.domain.order_payment.service;

import com.fineapple.application.exception.OrderItemDetailNotFoundException;
import com.fineapple.application.exception.OrderNotFoundException;
import com.fineapple.application.exception.RefundNotFoundException;
import com.fineapple.domain.order_payment.dto.*;
import com.fineapple.domain.order_payment.entity.Orders;
import com.fineapple.domain.order_payment.repository.OrdersMapper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImpTest {

    @Mock
    private OrdersMapper ordersMapper;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private OrderServiceImp orderService;

    @Test
    void selectOrderById_정상조회() {
        Orders orders = Orders.builder()
                .orderId(1L)
                .orderCode(2024040500001L)
                .totalPrice(new BigDecimal(100000))
                .discountPrice(new BigDecimal(5000))
                .orderStatus("OR0101") // ORDER_PLACED
                .paymentMethod("OR0501") // CARD
                .build();

        when(ordersMapper.selectOrderById(1L)).thenReturn(orders);

        OrderInfoDto result = orderService.selectOrderById(1L);

        assertThat(result.orderCode()).isEqualTo(2024040500001L);
        assertThat(result.paymentStatus()).isEqualTo("OR0501");
    }


    @Test
    void findAllOrdersStatus_주문없으면예외() {
        Long invalidOrderId = 100L;
        when(ordersMapper.existsOrderById(invalidOrderId)).thenReturn(false);
        when(messageSource.getMessage(eq("error.order.not_found"), any(), any(Locale.class)))
                .thenReturn("주문을 찾을 수 없습니다.");

        assertThatThrownBy(() -> orderService.findAllOrdersStatus(1, 10, invalidOrderId))
                .isInstanceOf(OrderNotFoundException.class)
                .hasMessageContaining("주문을 찾을 수 없습니다.");
    }

    @Test
    void findAllOrdersDetails_정상조회() {
        Long orderId = 1L;
        when(ordersMapper.existsOrderById(orderId)).thenReturn(true);
        when(ordersMapper.selectAllOrdersDetails(orderId)).thenReturn(
                List.of(new OrderAdminDto())
        );

        PageInfo<OrderAdminDto> result = orderService.findAllOrdersDetails(1, 10, orderId);

        assertThat(result.getList()).hasSize(1);
    }

    @Test
    void findAllOrdersDetailHistory_주문상세없으면예외() {
        Long detailId = 99999L;
        when(ordersMapper.existsOrderItemDetailById(detailId)).thenReturn(false);
        when(messageSource.getMessage(eq("error.order.item_detail.not_found"), any(), any(Locale.class)))
                .thenReturn("주문 상세 내역을 찾을 수 없습니다.");

        assertThatThrownBy(() -> orderService.findAllOrdersDetailHistory(1, 10, detailId))
                .isInstanceOf(OrderItemDetailNotFoundException.class)
                .hasMessageContaining("주문 상세 내역을 찾을 수 없습니다.");
    }

    @Test
    void findOrderItemDetailByOrderItemDetailId_null이면예외() {
        Long detailId = 5L;
        when(ordersMapper.existsOrderItemDetailById(detailId)).thenReturn(true);
        when(ordersMapper.selectOrderItemDetailByOrderItemDetailId(detailId)).thenReturn(null);
        when(messageSource.getMessage(eq("error.order.detail.null"), any(), any(Locale.class)))
                .thenReturn("조회 결과가 없습니다.");

        assertThatThrownBy(() -> orderService.findOrderItemDetailByOrderItemDetailId(detailId))
                .isInstanceOf(OrderItemDetailNotFoundException.class)
                .hasMessageContaining("조회 결과가 없습니다.");
    }

    @Test
    void countOrdersByPeriod_정상조회() {
        LocalDate start = LocalDate.of(2025, 4, 1);
        LocalDate end = LocalDate.of(2025, 4, 6);

        when(ordersMapper.countOrdersByPeriod(start, end)).thenReturn(15);

        Integer result = orderService.countOrdersByPeriod(start, end);

        assertThat(result).isEqualTo(15);
    }

    @Test
    void findAllRefund_정상조회() {
        RefundSearchParam param = new RefundSearchParam();
        when(ordersMapper.selectAllRefund(param)).thenReturn(List.of(new RefundDto()));

        PageInfo<RefundDto> result = orderService.findAllRefund(param, 1, 10);

        assertThat(result.getList()).hasSize(1);
    }

    @Test
    void findRefundDetails_환불없으면예외() {
        Long refundId = 100L;
        when(ordersMapper.existsRefundById(refundId)).thenReturn(false);
        when(messageSource.getMessage(eq("error.refund.not_found"), any(), any(Locale.class)))
                .thenReturn("환불 내역을 찾을 수 없습니다.");

        assertThatThrownBy(() -> orderService.findRefundDetails(refundId))
                .isInstanceOf(RefundNotFoundException.class)
                .hasMessageContaining("환불 내역을 찾을 수 없습니다.");
    }

    @Test
    void findRefundDetails_정상조회() {
        Long refundId = 1L;
        RefundDto refundDto = new RefundDto();
        when(ordersMapper.existsRefundById(refundId)).thenReturn(true);
        when(ordersMapper.selectRefundByIdWithDetails(refundId)).thenReturn(refundDto);

        RefundDto result = orderService.findRefundDetails(refundId);

        assertThat(result).isEqualTo(refundDto);
    }

    @Test
    void toOrderInfoDto_정상변환() {
        Orders orders = Orders.builder()
                .orderId(1L)
                .orderCode(2024040500001L)
                .totalPrice(new BigDecimal(10000))
                .discountPrice(new BigDecimal(5000))
                .orderStatus("SH0102") // SH_IN_TRANSIT
                .paymentMethod("OR0501")
                .build();

        OrderInfoDto result = orderService.toOrderInfoDto(orders);

        assertThat(result.orderId()).isEqualTo(1L);
        assertThat(result.orderCode()).isEqualTo(2024040500001L);
        assertThat(result.paymentStatus()).isEqualTo("OR0501");
    }
}