package com.fineapple.domain.support.service;

import com.fineapple.domain.support.dto.InquiryRequestDto;
import com.fineapple.domain.support.dto.InquiryResponseDetailDto;
import com.fineapple.domain.support.dto.InquiryResponseDto;
import com.fineapple.domain.support.entity.SupportInquiry;
import com.fineapple.domain.support.repository.SupportInquiryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final SupportInquiryMapper inquiryMapper;

    public void createInquiry(InquiryRequestDto dto, Long userId) {
        Long userInfo = inquiryMapper.findUserInfoIdByUserId(userId);

        SupportInquiry inquiry = SupportInquiry.builder()
                .subject(dto.getSubject())
                .content(dto.getContent())
                .userInfo(userInfo)
                .inquiryStatus("IN0101")
                .inquiryType(dto.getInquiryType())
                .email(dto.getEmail())
                .name(dto.getName())
                .assignedTo(null)
                .resolvedBy(null)
                .companyName(null)
                .countryRegion(null)
                .acceptPrivacyPolicy(true)
                .responseDueDate(LocalDateTime.now().plusDays(3))
                .priorityCode("IN0302") // 문의 중간 우선순위
                .orderId(null)
                .orderItemDetailId(null)
                .languageCode("IN0401") // 한국어
                .build();

        inquiryMapper.insertInquiry(inquiry);
    }

    public List<InquiryResponseDto> getMyInquiries(Long userId) {
        Long userInfo = inquiryMapper.findUserInfoIdByUserId(userId);
        List<InquiryResponseDto> entities = inquiryMapper.selectInquiriesByUserId(userInfo);

        // Entity → Dto 변환
        return entities.stream()
                .map(inquiry -> InquiryResponseDto.builder()
                        .inquiryId(inquiry.getInquiryId())
                        .subject(inquiry.getSubject())
                        .createdAt(inquiry.getCreatedAt())
                        .updatedAt(inquiry.getUpdatedAt())
                        .responseDueDate(inquiry.getResponseDueDate())
                        .inquiryStatus(inquiry.getInquiryStatus())
                        .build())
                .collect(Collectors.toList());
    }

    public List<InquiryResponseDto> getAllInquiries() {
        List<InquiryResponseDto> entities = inquiryMapper.selectInquiriesAll();
        return entities.stream()
                .map(inquiry -> InquiryResponseDto.builder()
                        .inquiryId(inquiry.getInquiryId())
                        .subject(inquiry.getSubject())
                        .createdAt(inquiry.getCreatedAt())
                        .updatedAt(inquiry.getUpdatedAt())
                        .responseDueDate(inquiry.getResponseDueDate())
                        .inquiryStatus(inquiry.getInquiryStatus())
                        .build())
                .collect(Collectors.toList());
    }

    public List<InquiryResponseDto> filterInquiries(String searchTerm, String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("searchTerm", searchTerm);
        params.put("status", status);

        return inquiryMapper.findByFilters(params);
    }


    public InquiryResponseDetailDto getMyDetailInquiry(Long userId, Long inquiryId) {
        SupportInquiry inquiry = inquiryMapper.selectInquiryByInquiryId(inquiryId);

        if (inquiry == null) {
            throw new NoSuchElementException("해당 문의글이 존재하지 않습니다.");
        }

        if (!Objects.equals(inquiry.getUserInfo(), userId)) {
            throw new AccessDeniedException("본인의 문의글만 조회할 수 있습니다.");
        }

        return InquiryResponseDetailDto.builder()
                .subject(inquiry.getSubject())
                .content(inquiry.getContent())
                .createdAt(inquiry.getCreatedAt())
                .updatedAt(inquiry.getUpdatedAt())
                .inquiryStatus(inquiry.getInquiryStatus())
                .inquiryType(inquiry.getInquiryType())
                .email(inquiry.getEmail())
                .name(inquiry.getName())
                .assignedTo(inquiry.getAssignedTo())
                .resolvedBy(inquiry.getResolvedBy())
                .responseDueDate(inquiry.getResponseDueDate())
                .build();
    }


    // 필터링된 문의글 리스트를 반환하는 메서드
//    public List<InquiryResponseDto> filter(Long userId, String searchTerm, String status) {
//        // DB에서 조회한 결과를 반환하기 전에 DTO로 변환하여 반환
//        List<SupportInquiry> inquiries = inquiryMapper.findFilteredInquiries(userId, searchTerm, status);
//        return inquiries.stream()
//                .map(inquiry -> new InquiryResponseDto(inquiry))
//                .collect(Collectors.toList());
//    }
}
