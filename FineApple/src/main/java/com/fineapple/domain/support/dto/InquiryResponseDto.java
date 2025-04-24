package com.fineapple.domain.support.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InquiryResponseDto {
    private Long inquiryId;
    private String subject;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime responseDueDate;
    private String inquiryStatus;

}
