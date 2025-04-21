package com.fineapple.domain.order_payment.repository;

import com.fineapple.application.dto.TimeSeriesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderAnalyticsMapper {

    List<TimeSeriesDto> getDailyOrderCount();

    List<TimeSeriesDto> getProductSalesCount();

    List<TimeSeriesDto> getDailyRevenue();

    List<TimeSeriesDto> getMonthlyRevenue();

    List<TimeSeriesDto> getPredictedDailyRevenue();

    List<TimeSeriesDto> getMonthlyPredictedValues();
}
