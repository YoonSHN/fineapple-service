package com.fineapple.domain.order_payment.service;

import com.fineapple.domain.order_payment.dto.OrderItemDetailUserDto;
import com.fineapple.domain.order_payment.entity.OrderItemDetail;
import com.fineapple.domain.order_payment.entity.PaymentDetail;
import com.fineapple.domain.order_payment.repository.OrderPaymentMapper;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderPaymentService {

    private final IamportClient iamportClient;
    private final OrderPaymentMapper orderPaymentMapper;

    public String createPaymentRequest(String ImpUid) {
        try {
            IamportResponse<Payment> response = iamportClient.paymentByImpUid(ImpUid);
            Payment payment = response.getResponse();

            if (payment != null) {
                return payment.getImpUid(); // impUid 반환
            } else {
                throw new RuntimeException("결제 정보를 가져올 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("결제 정보 저장 중 예외 발생", e);
            throw new RuntimeException("결제 정보 저장 실패");
        }
    }

    //orderId 기준으로 결제정보 저장
    @Transactional
    public void savePaymentInfo(Long orderId, String impUid, List<OrderItemDetailUserDto> orderItemDetails) {
        try {
            IamportResponse<com.siot.IamportRestClient.response.Payment> response = iamportClient.paymentByImpUid(impUid);
            com.siot.IamportRestClient.response.Payment iamportPayment = response.getResponse();

            if (iamportPayment == null) {
                throw new RuntimeException("결제 정보가 null입니다.");
            }

            LocalDateTime paidAt = iamportPayment.getPaidAt() != null
                    ? iamportPayment.getPaidAt().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    : null;

            com.fineapple.domain.order_payment.entity.Payment payment = com.fineapple.domain.order_payment.entity.Payment.builder()
                    .orderId(orderId)
                    .pgApprovalCode(iamportPayment.getPgTid())
                    .pgType(iamportPayment.getPgProvider())
                    .receiptUrl(iamportPayment.getReceiptUrl())
                    .cardName(iamportPayment.getCardName())
                    .cardQuota(iamportPayment.getCardQuota())
                    .paymentStatus("OR0202")
                    .failureCode(iamportPayment.getFailReason())
                    .totalAmount(iamportPayment.getAmount())
                    .paidAt(paidAt)
                    .pgUniqueId(iamportPayment.getImpUid())
                    .requestedAt(paidAt)
                    .currency("KRW")
                    .paymentMethod("OR0501")
                    .build();

            int result = orderPaymentMapper.insertPaymentInfo(payment);
            if (result == 0) {
                throw new RuntimeException("결제 정보 저장 실패: insert row 없음");
            }

            savePaymentDetails(payment.getPaymentId(), orderItemDetails);

        } catch (Exception e) {
            log.error("결제 저장 중 오류 발생", e);
            throw new RuntimeException("결제 정보 저장 실패: " + e.getMessage(), e);
        }
    }

    private void savePaymentDetails(Long paymentId, List<OrderItemDetailUserDto> orderItemDetails) {
        for (OrderItemDetailUserDto orderItemDetailUserDto : orderItemDetails) {
            // OrderItemDetailUserDto에서 결제 상세 정보를 추출
            BigDecimal paidAmount = orderItemDetailUserDto.getItemPrice().multiply(BigDecimal.valueOf(orderItemDetailUserDto.getItemQuantity()));
            String productName = orderItemDetailUserDto.getItemName();
            Integer quantity = orderItemDetailUserDto.getItemQuantity();
            Integer cancelledQuantity = 0;  // 취소된 수량 (기본값)
            BigDecimal cancelledAmount = BigDecimal.ZERO;  // 취소 금액 (기본값)
            String failReason = null;  // 실패 이유 (기본값)
            LocalDateTime cancelledAt = null;  // 취소 시간 (기본값)

            // PaymentDetail 객체 생성
            PaymentDetail paymentDetail = PaymentDetail.builder()
                    .paymentId(paymentId)  // Payment ID (결제 ID)
                    .orderItemDetailId(orderItemDetailUserDto.getProductId())  // 상품 ID
                    .productName(productName)  // 상품명
                    .paidAmount(paidAmount)  // 결제 금액
                    .quantity(quantity)  // 수량
                    .cancelledQuantity(cancelledQuantity)  // 취소된 수량
                    .cancelledAmount(cancelledAmount)  // 취소 금액
                    .failReason(failReason)  // 실패 사유
                    .cancelledAt(cancelledAt)  // 취소 시간
                    .createdAt(LocalDateTime.now())  // 생성 시간
                    .updatedAt(LocalDateTime.now())  // 업데이트 시간
                    .build();

            int result = orderPaymentMapper.insertPaymentDetailInfo(paymentDetail);
            if (result == 0) {
                throw new RuntimeException("결제 상세 저장 실패: 상품명 [" + orderItemDetailUserDto.getItemName() + "] insert row 없음");
            }
        }
    }
}