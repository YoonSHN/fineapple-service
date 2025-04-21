package com.fineapple.domain.order_payment.repository;


import com.fineapple.domain.order_payment.dto.PaymentAmountByWeekdayDto;
import com.fineapple.domain.order_payment.entity.Payment;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface PaymentMapper {

    void insertPayment(Payment payment);

    Long selectPaymentAmountCountToday(LocalDate startDate, LocalDate endDate);

    List<PaymentAmountByWeekdayDto> selectPaymentAmountByWeekday();
}
