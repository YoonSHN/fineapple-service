package com.fineapple.domain.support.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InquiryResponseDetailDto {
    private String subject;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String inquiryStatus;
    private String inquiryType;
    private String email;
    private String name;
    private String assignedTo;
    private String resolvedBy;
    private LocalDateTime responseDueDate;
}
