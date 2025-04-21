package com.fineapple.application.controller.view.admin;

import com.fineapple.application.dto.AdminSalesDto;
import com.fineapple.application.service.AdminService;
import com.fineapple.domain.order_payment.dto.PaymentAmountByWeekdayDto;
import com.fineapple.domain.order_payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 모든 관리자페이지를 처리하는 컨트롤러
 * <p>
 * 모든 요청URL은 /admin으로 매핑
 * 사이드 프래그먼트를 제외한 모든 프래그먼트는 SPA방식으로 동작
 */
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final AdminService adminService;
    private final PaymentService paymentService;

    @GetMapping({"/admin", "/admin/**", "/users/**", "/orders/**",
            "/products/**", "/inventory/**", "/analytics/**", "/settings/**"})
    public String forwardAdmin() {
        return "admin";
    }

    // 대시보드
    @GetMapping("/dashboard")
    public String dashboard() {
        return "fragments/dashboard";
    }

    // 고객 관련
    @GetMapping("/users/list")
    public String usersList() {
        return "fragments/userList";
    }

    @GetMapping("/users/profile-history")
    public String usersProfileHistory() {
        return "fragments/usersProfileHistory";
    }

    @GetMapping("/users/login-history")
    public String usersLoginHistory() {
        return "fragments/usersLoginHistory";
    }

    // 주문 관련
    @GetMapping("/orders/list")
    public String ordersList() {
        return "fragments/ordersList";
    }

    @GetMapping("/orders/detail-list")
    public String ordersDetailList() {
        return "fragments/ordersDetailList";
    }

    @GetMapping("/orders/item-detail")
    public String ordersItemDetail() {
        return "fragments/orderItemDetail";
    }

    @GetMapping("/orders/refund")
    public String refundList() {
        return "fragments/refund";
    }

    // 상품 관련
    @GetMapping("/products/list")
    public String productsList() {
        return "fragments/product";
    }

//    @GetMapping("/products/history")
//    public String productsHistory() {
//        return "fragments/productsHistory";
//    }

    // 재고 관련
    @GetMapping("/inventory/store")
    public String inventoryStore() {
        return "fragments/stockPageStore";
    }

    @GetMapping("/inventory/stockList")
    public String inventoryStatus() {
        return "fragments/stockList";
    }

    // 분석 관련
    @GetMapping("/analytics/report")
    public String analyticsSalesReport() {
        return "fragments/analyticsReport";
    }

//    @GetMapping("/analytics/traffic-report")
//    public String analyticsTrafficReport() {
//        return "fragments/analyticsTrafficReport";
//    }

//    // 설정
//    @GetMapping("/settings")
//    public String settings() {
//        return "fragments/settings";
//    }

    //스토어 내역
    @GetMapping("/inventory/history")
    public String showStockHistory() {
        return "fragments/stockDetailHistory"; // resources/templates/ 안에 있어야 함
    }

    @Operation(summary = "매출 통계 일 주 월 년 조회")
    @ResponseBody
    @GetMapping("/api/v1/admin/sales")
    public ResponseEntity<AdminSalesDto> getSales(@RequestParam(defaultValue = "week") String period) {
        AdminSalesDto adminSalesDto = adminService.getSalesReport(period);
        return ResponseEntity.ok(adminSalesDto);
    }

    @Operation(summary = "매출 주간 통계 조회")
    @ResponseBody
    @GetMapping("/api/v1/admin/sales-data")
    public ResponseEntity<List<PaymentAmountByWeekdayDto>> getWeekSales() {
        List<PaymentAmountByWeekdayDto> paymentAmountByWeekdays = paymentService.getPaymentAmountByWeekday();
        return ResponseEntity.ok(paymentAmountByWeekdays);
    }

}
