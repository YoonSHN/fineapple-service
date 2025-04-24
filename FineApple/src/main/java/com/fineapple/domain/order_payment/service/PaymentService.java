package com.fineapple.domain.order_payment.service;

import com.fineapple.domain.order_payment.dto.PaymentAmountByWeekdayDto;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface PaymentService {
    @Transactional(readOnly = true)
    Long countPaymentsByPeriod(LocalDate startDate, LocalDate endDate);

    @Transactional(readOnly = true)
    List<PaymentAmountByWeekdayDto> getPaymentAmountByWeekday();
}
