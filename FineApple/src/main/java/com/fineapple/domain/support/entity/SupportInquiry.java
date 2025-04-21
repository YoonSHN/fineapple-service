package com.fineapple.domain.support.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class SupportInquiry {
    private Long inquiryId;
    private String subject;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userInfo;
    private String inquiryStatus;
    private String inquiryType;
    private String email;
    private String name;
    private String companyName;
    private String countryRegion;
    private String assignedTo;
    private String resolvedBy;
    private Boolean acceptPrivacyPolicy;
    private LocalDateTime responseDueDate;
    private String priorityCode;
    private Long orderId;
    private Long orderItemDetailId;
    private Long parentInquiryId;
    private String languageCode;

    @Builder
    public SupportInquiry(Long inquiryId, String subject, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Long userInfo,
                          String inquiryStatus, String inquiryType, String email, String name, String companyName, String countryRegion,
                          String assignedTo, String resolvedBy, Boolean acceptPrivacyPolicy, LocalDateTime responseDueDate,
                          String priorityCode, Long orderId, Long orderItemDetailId, Long parentInquiryId, String languageCode) {
        this.inquiryId = inquiryId;
        this.subject = subject;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userInfo = userInfo;
        this.inquiryStatus = inquiryStatus;
        this.inquiryType = inquiryType;
        this.email = email;
        this.name = name;
        this.companyName = companyName;
        this.countryRegion = countryRegion;
        this.assignedTo = assignedTo;
        this.resolvedBy = resolvedBy;
        this.acceptPrivacyPolicy = acceptPrivacyPolicy;
        this.responseDueDate = responseDueDate;
        this.priorityCode = priorityCode;
        this.orderId = orderId;
        this.orderItemDetailId = orderItemDetailId;
        this.parentInquiryId = parentInquiryId;
        this.languageCode = languageCode;
    }
}

