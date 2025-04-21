package com.fineapple.domain.logistics_inventory.repository;


import com.fineapple.application.dto.TimeSeriesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShipmentAnalyticsMapper {

    List<TimeSeriesDto> getDailyDispatchCount();

    List<TimeSeriesDto> getDailyDelayCount();
}