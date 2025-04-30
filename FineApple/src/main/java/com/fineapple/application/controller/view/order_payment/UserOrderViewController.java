package com.fineapple.application.controller.view.order_payment;

import com.fineapple.domain.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fineapple.domain.order_payment.service.OrderUserService;

@Controller
public class UserOrderViewController {

    @Value("${impCode}")
    private String impCode;

    @Autowired
    private OrderUserService orderUserService;

    @GetMapping("/user/orders/list")
    public String showUserOrderList(Model model, @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "fragments/userOrderList";
    }

    @GetMapping("/user/orders/detail/{orderCode}")
    public String showUserOrderDetail(@PathVariable("orderCode") Long orderCode, Model model) {
        model.addAttribute("userOrderItemDetail", orderUserService.selectOrderItemDetailByOrderCode(orderCode));
        return "fragments/userOrderItemDetail";
    }

    // 카트 상품 결제
    @GetMapping("/CreateOrderPayment")
    public String orderPay(Model model) {
        model.addAttribute("impCode", impCode);
        return "fragments/orderForm";
    }

    // 결제 후 뷰 반환
    @GetMapping("/payments/payment-complete")
    public String showPaymentCompletePage() {
        return "fragments/payment-complete";
    }


}
