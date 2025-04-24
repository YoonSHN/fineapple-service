package com.fineapple.application.service;

import com.fineapple.application.dto.AdminSalesDto;
import com.fineapple.domain.logistics_inventory.service.StockService;
import com.fineapple.domain.order_payment.service.OrderService;
import com.fineapple.domain.order_payment.service.PaymentService;
import com.fineapple.domain.user.service.UserHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
/**
 * 관리자용 통계 데이터를 제공하는 서비스
 *
 * - 매출, 주문 수, 재고 수량, 방문자 수(신규 유저 등)를 기간별로 집계
 * - 현재 기간과 지난 기간(동일 기간 전주/전월 등)을 비교하여 성장률을 계산
 *
 * 기간 설정
 * - today : 오늘 00시 ~ 내일 00시
 * - week : 이번 주 월요일 ~ 일요일
 * - month : 이번 달 1일 ~ 다음 달 1일
 * - year : 올해 1월 1일 ~ 내년 1월 1일
 */
@Service
@RequiredArgsConstructor
public class AdminService {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final StockService stockService;
    private final UserHistoryService userHistoryService;

    /**
     * 주문 매출 재고 사용자 통계
     * @param period 날짜
     * @return AdminSalesDto 통계 값 및 지난주 통계값
     */
    public AdminSalesDto getSalesReport(String period) {
        LocalDate today = LocalDate.now();
        LocalDate startDate;
        LocalDate endDate;

        switch (period.toLowerCase()) {
            case "today":
                startDate = today;
                endDate = today.plusDays(1);
                break;
            case "week":
                startDate = today.minusDays(today.getDayOfWeek().getValue() - 1);
                endDate = startDate.plusDays(7);
                break;
            case "month":
                startDate = today.with(TemporalAdjusters.firstDayOfMonth());
                endDate = today.with(TemporalAdjusters.firstDayOfNextMonth());
                break;
            case "year":
                startDate = today.with(TemporalAdjusters.firstDayOfYear());
                endDate = today.with(TemporalAdjusters.firstDayOfNextYear());
                break;
            default:
                startDate = today;
                endDate = today.plusDays(1);
        }

        int orderCount = Optional.ofNullable(orderService.countOrdersByPeriod(startDate, endDate)).orElse(0);
        long paymentCount = Optional.ofNullable(paymentService.countPaymentsByPeriod(startDate, endDate)).orElse(0L);
        int stockQuantity = Optional.ofNullable(stockService.countStockQuantityByPeriod(startDate, endDate)).orElse(0);
        int userCount = Optional.ofNullable(userHistoryService.countUserByPeriod(startDate, endDate)).orElse(0);

        LocalDate lastStart = startDate.minusDays(endDate.toEpochDay() - startDate.toEpochDay());
        LocalDate lastEnd = startDate;

        int lastOrder = Optional.ofNullable(orderService.countOrdersByPeriod(lastStart, lastEnd)).orElse(0);
        long lastSales = Optional.ofNullable(paymentService.countPaymentsByPeriod(lastStart, lastEnd)).orElse(0L);
        int lastUser = Optional.ofNullable(userHistoryService.countUserByPeriod(lastStart, lastEnd)).orElse(0);

        long salesGrowth = calculateGrowthRate(lastSales, paymentCount);
        int orderGrowth = calculateGrowthRate(lastOrder, orderCount);
        int visitorGrowth = calculateGrowthRate(lastUser, userCount);

        AdminSalesDto dto = new AdminSalesDto(paymentCount, orderCount, userCount, stockQuantity, (int) salesGrowth, orderGrowth, visitorGrowth);

        return dto;
    }

    private int calculateGrowthRate(long past, long current) {
        if (past == 0) return current > 0 ? 100 : 0;
        return Math.round(((float)(current - past) / past) * 100);
    }
}
