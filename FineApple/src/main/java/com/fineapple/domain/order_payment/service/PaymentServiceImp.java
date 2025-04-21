package com.fineapple.domain.order_payment.service;


import com.fineapple.domain.order_payment.dto.PaymentAmountByWeekdayDto;
import com.fineapple.domain.order_payment.repository.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 결제(매출) 통계 관련 기능을 제공하는 서비스 구현체
 * <p>
 * - 기간별 총 결제 금액(매출)을 조회
 * - 요일별(월~일) 결제 금액 통계를 조회
 */

@Service
@RequiredArgsConstructor
public class PaymentServiceImp implements PaymentService {

    private final PaymentMapper paymentMapper;

    /**
     * 지정 기간 매출
     *
     * @param startDate 시작날찌
     * @param endDate   마지막 날짜
     */
    @Transactional(readOnly = true)
    @Override
    public Long countPaymentsByPeriod(LocalDate startDate, LocalDate endDate) {
        return paymentMapper.selectPaymentAmountCountToday(startDate, endDate);
    }

    /**
     * WEEKDAY 요일별 통계 매출
     */
    @Transactional(readOnly = true)
    @Override
    public List<PaymentAmountByWeekdayDto> getPaymentAmountByWeekday() {
        return paymentMapper.selectPaymentAmountByWeekday();
    }
}
