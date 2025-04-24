package com.fineapple.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fineapple.application.dto.AdminSalesDto;
import com.fineapple.application.service.AdminService;
import com.fineapple.domain.order_payment.service.OrderService;
import com.fineapple.domain.order_payment.service.PaymentService;
import com.fineapple.domain.logistics_inventory.service.StockService;
import com.fineapple.domain.user.service.UserHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private OrderService orderService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private StockService stockService;

    @Mock
    private UserHistoryService userHistoryService;

    @InjectMocks
    private AdminService adminService;

    @Test
    public void testGetSalesReportToday() {

        LocalDate today = LocalDate.now();
        LocalDate startDate = today;
        LocalDate endDate = today.plusDays(1);


        when(orderService.countOrdersByPeriod(startDate, endDate)).thenReturn(10);
        when(paymentService.countPaymentsByPeriod(startDate, endDate)).thenReturn(20L);
        when(stockService.countStockQuantityByPeriod(startDate, endDate)).thenReturn(30);
        when(userHistoryService.countUserByPeriod(startDate, endDate)).thenReturn(40);

        AdminSalesDto result = adminService.getSalesReport("today");

        assertEquals(20, result.totalSales(), "Payment count should be 20");
        assertEquals(10, result.orderCount(), "Order count should be 10");
        assertEquals(40, result.visitors(), "User count should be 40");
        assertEquals(30, result.lowStock(), "Stock quantity should be 30");
    }

    @Test
    public void testGetSalesReportWeek() {

        LocalDate today = LocalDate.now();

        int dayOfWeek = today.getDayOfWeek().getValue();
        LocalDate startDate = today.minusDays(dayOfWeek - 1);
        LocalDate endDate = startDate.plusDays(7);

        when(orderService.countOrdersByPeriod(startDate, endDate)).thenReturn(15);
        when(paymentService.countPaymentsByPeriod(startDate, endDate)).thenReturn(25L);
        when(stockService.countStockQuantityByPeriod(startDate, endDate)).thenReturn(35);
        when(userHistoryService.countUserByPeriod(startDate, endDate)).thenReturn(45);

        AdminSalesDto result = adminService.getSalesReport("week");

        assertEquals(25, result.totalSales(), "Payment count for week should be 25");
        assertEquals(15, result.orderCount(), "Order count for week should be 15");
        assertEquals(45, result.visitors(), "User count for week should be 45");
        assertEquals(35, result.lowStock(), "Stock quantity for week should be 35");
    }
}
