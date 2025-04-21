package com.fineapple.application.service;

import com.fineapple.application.dto.TimeSeriesDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdminAnalyticsServiceTest {

    @Autowired
    private AdminAnalyticsService analyticsService;

    @Test
    @DisplayName("일별 주문 건수 조회")
    void getDailyOrderCount() {
        List<TimeSeriesDto> result = analyticsService.getDailyOrderCount();
        assertThat(result).isNotNull();
        System.out.println(" 일별 주문 건수: " + result);
    }

    @Test
    @DisplayName("상품별 판매량 조회")
    void getProductSalesCount() {
        List<TimeSeriesDto> result = analyticsService.getProductSalesCount();
        assertThat(result).isNotNull();
        System.out.println(" 상품별 판매량: " + result);
    }

    @Test
    @DisplayName("일별 매출 조회")
    void getDailyRevenue() {
        List<TimeSeriesDto> result = analyticsService.getDailyRevenue();
        assertThat(result).isNotNull();
        System.out.println(" 일별 매출: " + result);
    }

    @Test
    @DisplayName("월별 매출 조회")
    void getMonthlyRevenue() {
        List<TimeSeriesDto> result = analyticsService.getMonthlyRevenue();
        assertThat(result).isNotNull();
        System.out.println(" 월별 매출: " + result);
    }

    @Test
    @DisplayName("일별 결제 금액 조회")
    void getDailyPaidAmount() {
        List<TimeSeriesDto> result = analyticsService.getDailyPaidAmount();
        assertThat(result).isNotNull();
        System.out.println(" 결제 금액: " + result);
    }

    @Test
    @DisplayName("결제 수단별 결제 금액 조회")
    void getDailyPaidByMethod() {
        List<TimeSeriesDto> result = analyticsService.getDailyPaidByMethod();
        assertThat(result).isNotNull();
        System.out.println(" 결제 수단별 매출: " + result);
    }

    @Test
    @DisplayName("일별 환불 금액 조회")
    void getDailyRefundAmount() {
        List<TimeSeriesDto> result = analyticsService.getDailyRefundAmount();
        assertThat(result).isNotNull();
        System.out.println(" 환불 금액: " + result);
    }

    @Test
    @DisplayName("환불 실패 건수 조회")
    void getRefundFailCount() {
        List<TimeSeriesDto> result = analyticsService.getRefundFailCount();
        assertThat(result).isNotNull();
        System.out.println(" 환불 실패 수: " + result);
    }

    @Test
    @DisplayName("일별 재고 입고량 조회")
    void getDailyStockIn() {
        List<TimeSeriesDto> result = analyticsService.getDailyStockIn();
        assertThat(result).isNotNull();
        System.out.println(" 재고 입고량: " + result);
    }

    @Test
    @DisplayName("일별 재고 출고량 조회")
    void getDailyStockOut() {
        List<TimeSeriesDto> result = analyticsService.getDailyStockOut();
        assertThat(result).isNotNull();
        System.out.println(" 재고 출고량: " + result);
    }

    @Test
    @DisplayName("일별 재고 변화량 조회")
    void getDailyNetStockChange() {
        List<TimeSeriesDto> result = analyticsService.getDailyNetStockChange();
        assertThat(result).isNotNull();
        System.out.println(" 재고 변화량: " + result);
    }

    @Test
    @DisplayName("일별 로그인 수 조회")
    void getDailyLoginCount() {
        List<TimeSeriesDto> result = analyticsService.getDailyLoginCount();
        assertThat(result).isNotNull();
        System.out.println(" 로그인 수: " + result);
    }

    @Test
    @DisplayName("일별 회원가입 수 조회")
    void getDailySignupCount() {
        List<TimeSeriesDto> result = analyticsService.getDailySignupCount();
        assertThat(result).isNotNull();
        System.out.println(" 가입 수: " + result);
    }

    @Test
    @DisplayName("일별 고객 문의 수 조회")
    void getDailyInquiryCount() {
        List<TimeSeriesDto> result = analyticsService.getDailyInquiryCount();
        assertThat(result).isNotNull();
        System.out.println(" 문의 수: " + result);
    }

    @Test
    @DisplayName("일별 쿠폰 사용 수 조회")
    void getDailyCouponUseCount() {
        List<TimeSeriesDto> result = analyticsService.getDailyCouponUseCount();
        assertThat(result).isNotNull();
        System.out.println(" 쿠폰 사용 수: " + result);
    }

    @Test
    @DisplayName("쿠폰 코드별 일별 사용 수 조회")
    void getDailyCouponCountByCoupon() {
        List<TimeSeriesDto> result = analyticsService.getDailyCouponCountByCoupon();
        assertThat(result).isNotNull();
        System.out.println(" 쿠폰 코드별 사용 수: " + result);
    }

    @Test
    @DisplayName("일별 출고 수량 조회")
    void getDailyDispatchCount() {
        List<TimeSeriesDto> result = analyticsService.getDailyDispatchCount();
        assertThat(result).isNotNull();
        System.out.println(" 출고 수량: " + result);
    }

    @Test
    @DisplayName("배송 지연 건수 조회")
    void getDailyDelayCount() {
        List<TimeSeriesDto> result = analyticsService.getDailyDelayCount();
        assertThat(result).isNotNull();
        System.out.println(" 배송 지연 수: " + result);
    }


    @Test
    @DisplayName("일별 예측 매출 조회")
    void getPredictDailyDelayCount() {
        List<TimeSeriesDto> result = analyticsService.getPredictedDailyRevenueFromDb();
        assertThat(result).isNotNull();
        System.out.println(" 예측 일별 매출 수: " + result);
    }

    @Test
    @DisplayName("월별 예측 매출 조회")
    void getPredictDailyMonthlyCount() {
        List<TimeSeriesDto> result = analyticsService.getPredictedMonthlyRevenueFromDb();
        assertThat(result).isNotNull();
        System.out.println(" 예측 일별 매출 수: " + result);
    }
}
