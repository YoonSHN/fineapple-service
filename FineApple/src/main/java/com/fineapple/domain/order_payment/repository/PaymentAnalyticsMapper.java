package com.fineapple.domain.order_payment.repository;

import com.fineapple.application.dto.TimeSeriesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PaymentAnalyticsMapper {

    List<TimeSeriesDto> getDailyPaidAmount();

    List<TimeSeriesDto> getDailyPaidByMethod();
}
