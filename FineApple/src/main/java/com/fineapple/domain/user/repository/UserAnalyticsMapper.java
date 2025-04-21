package com.fineapple.domain.user.repository;

import com.fineapple.application.dto.TimeSeriesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserAnalyticsMapper {

    List<TimeSeriesDto> getDailyLoginCount();

    List<TimeSeriesDto> getDailySignupCount();
}
