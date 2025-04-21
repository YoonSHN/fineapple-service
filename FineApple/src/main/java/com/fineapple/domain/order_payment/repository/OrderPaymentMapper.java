package com.fineapple.domain.order_payment.repository;

import com.fineapple.domain.order_payment.entity.Payment;
import com.fineapple.domain.order_payment.entity.PaymentDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderPaymentMapper {

    int insertPaymentInfo(Payment payment);
    int insertPaymentDetailInfo(PaymentDetail paymentDetail);

}
