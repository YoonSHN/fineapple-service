package com.fineapple.domain.support.repository;

import com.fineapple.domain.support.dto.InquiryResponseDto;
import com.fineapple.domain.support.entity.SupportInquiry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SupportInquiryMapper {
    void insertInquiry(SupportInquiry inquiry);

    List<InquiryResponseDto> selectInquiriesByUserId(Long UserInfo);

    Long findUserInfoIdByUserId(Long userId);

    List<InquiryResponseDto> selectInquiriesAll();

    SupportInquiry selectInquiryByInquiryId(Long inquiryId);

    List<InquiryResponseDto> findByFilters(Map<String, Object> params);

}
