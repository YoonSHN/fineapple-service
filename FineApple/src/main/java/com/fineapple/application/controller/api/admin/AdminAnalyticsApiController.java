package com.fineapple.application.controller.api.admin;

import com.fineapple.application.dto.TimeSeriesDto;
import com.fineapple.application.service.AdminAnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 전용 분석 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analytics")
public class AdminAnalyticsApiController {

    private final AdminAnalyticsService analyticsService;

    @Operation(summary = "일별 주문 건수 조회")
    @GetMapping("/orders/daily")
    public ResponseEntity<List<TimeSeriesDto>> dailyOrders() {
        return ResponseEntity.ok(analyticsService.getDailyOrderCount());
    }

    @Operation(summary = "상품별 판매량 조회")
    @GetMapping("/orders/product-sales")
    public ResponseEntity<List<TimeSeriesDto>> productSales() {
        return ResponseEntity.ok(analyticsService.getProductSalesCount());
    }

    @Operation(summary = "일별 매출 조회")
    @GetMapping("/orders/daily-revenue")
    public ResponseEntity<List<TimeSeriesDto>> dailyRevenue() {
        return ResponseEntity.ok(analyticsService.getDailyRevenue());
    }

    @Operation(summary = " 일별 매출 예측 DB조회")
    @GetMapping("/predicted/daily-revenue")
    public ResponseEntity<List<TimeSeriesDto>> getPredictedDailyRevenue() {
        List<TimeSeriesDto> predicted = analyticsService.getPredictedDailyRevenueFromDb();
        return ResponseEntity.ok(predicted);
    }

    @Operation(summary = "월별 매출 조회")
    @GetMapping("/orders/monthly-revenue")
    public ResponseEntity<List<TimeSeriesDto>> monthlyRevenue() {
        return ResponseEntity.ok(analyticsService.getMonthlyRevenue());
    }

    @Operation(summary = "월별 매출 예측 DB조회")
    @GetMapping("/predicted/monthly-revenue")
    public ResponseEntity<List<TimeSeriesDto>> predictMonthlyRevenue() {

        return ResponseEntity.ok(analyticsService.getPredictedMonthlyRevenueFromDb());
    }

    @Operation(summary = "일별 결제 금액 조회")
    @GetMapping("/payments/daily")
    public ResponseEntity<List<TimeSeriesDto>> dailyPaid() {
        return ResponseEntity.ok(analyticsService.getDailyPaidAmount());
    }

    @Operation(summary = "결제 수단별 결제 금액 조회")
    @GetMapping("/payments/by-method")
    public ResponseEntity<List<TimeSeriesDto>> paidByMethod() {
        return ResponseEntity.ok(analyticsService.getDailyPaidByMethod());
    }

    @Operation(summary = "일별 환불 금액 조회")
    @GetMapping("/refunds/daily")
    public ResponseEntity<List<TimeSeriesDto>> dailyRefund() {
        return ResponseEntity.ok(analyticsService.getDailyRefundAmount());
    }

    @Operation(summary = "환불 실패 건수 조회")
    @GetMapping("/refunds/fail-count")
    public ResponseEntity<List<TimeSeriesDto>> refundFailCount() {
        return ResponseEntity.ok(analyticsService.getRefundFailCount());
    }

    @Operation(summary = "일별 입고량 조회")
    @GetMapping("/stocks/in")
    public ResponseEntity<List<TimeSeriesDto>> stockIn() {
        return ResponseEntity.ok(analyticsService.getDailyStockIn());
    }

    @Operation(summary = "일별 출고량 조회")
    @GetMapping("/stocks/out")
    public ResponseEntity<List<TimeSeriesDto>> stockOut() {
        return ResponseEntity.ok(analyticsService.getDailyStockOut());
    }

    @Operation(summary = "일별 재고 변동량 조회")
    @GetMapping("/stocks/net-change")
    public ResponseEntity<List<TimeSeriesDto>> netStockChange() {
        return ResponseEntity.ok(analyticsService.getDailyNetStockChange());
    }

    @Operation(summary = "일별 로그인 수 조회")
    @GetMapping("/users/logins")
    public ResponseEntity<List<TimeSeriesDto>> logins() {
        return ResponseEntity.ok(analyticsService.getDailyLoginCount());
    }

    @Operation(summary = "일별 회원 가입 수 조회")
    @GetMapping("/users/signups")
    public ResponseEntity<List<TimeSeriesDto>> signups() {
        return ResponseEntity.ok(analyticsService.getDailySignupCount());
    }

    @Operation(summary = "일별 문의 수 조회")
    @GetMapping("/inquiries")
    public ResponseEntity<List<TimeSeriesDto>> inquiries() {
        return ResponseEntity.ok(analyticsService.getDailyInquiryCount());
    }

    @Operation(summary = "일별 쿠폰 사용 수 조회")
    @GetMapping("/coupons/used")
    public ResponseEntity<List<TimeSeriesDto>> couponUsed() {
        return ResponseEntity.ok(analyticsService.getDailyCouponUseCount());
    }

    @Operation(summary = "쿠폰별 쿠폰 사용 수 조회")
    @GetMapping("/coupons/used-by-coupon")
    public ResponseEntity<List<TimeSeriesDto>> couponUsedByCoupon() {
        return ResponseEntity.ok(analyticsService.getDailyCouponCountByCoupon());
    }

    @Operation(summary = "일별 배송 수 조회")
    @GetMapping("/shipments/dispatched")
    public ResponseEntity<List<TimeSeriesDto>> dispatched() {
        return ResponseEntity.ok(analyticsService.getDailyDispatchCount());
    }

    @Operation(summary = "일별 배송 지연 수 조회")
    @GetMapping("/shipments/delayed")
    public ResponseEntity<List<TimeSeriesDto>> delayed() {
        return ResponseEntity.ok(analyticsService.getDailyDelayCount());
    }


}
