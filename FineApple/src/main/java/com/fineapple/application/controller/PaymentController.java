package com.fineapple.application.controller;


import com.fineapple.domain.order_payment.service.OrderPaymentService;
import com.fineapple.domain.order_payment.service.OrderUserService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {


    private final IamportClient iamportClient;
    private final OrderUserService orderUserService;
    private final OrderPaymentService orderPaymentService;



    public PaymentController(IamportClient iamportClient, OrderUserService orderUserService, OrderPaymentService orderPaymentService) {
        this.iamportClient = iamportClient;
        this.orderUserService = orderUserService;
        this.orderPaymentService = orderPaymentService;
    }


    // 결제 요청 처리
    @PostMapping("/request")
    public ResponseEntity<String> requestPayment(@RequestBody String orderId) {
        // 주문 처리 후 결제 요청
        String impUid = orderPaymentService.createPaymentRequest(orderId);
        return ResponseEntity.ok("Payment Request Initiated: " + impUid);
    }

    // 결제 상태 조회
    @GetMapping("/{impUid}/status")
    public ResponseEntity<String> getPaymentStatus(@PathVariable String impUid) {
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(impUid);
            Payment payment = response.getResponse(); // IamportResponse에서 Payment 객체 추출

            if (payment != null) {
                return ResponseEntity.ok("Payment Status: " + payment.getStatus());
            }
            return ResponseEntity.status(404).body("Payment Not Found");
        } catch (IamportResponseException | IOException e) {
            // 예외 처리
            return ResponseEntity.status(500).body("Error fetching payment status: " + e.getMessage());
        }
    }


}