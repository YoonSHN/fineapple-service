package com.fineapple.domain.support.repository;

import com.fineapple.application.dto.TimeSeriesDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InquiryAnalyticsMapper {

    List<TimeSeriesDto> getDailyInquiryCount();

}