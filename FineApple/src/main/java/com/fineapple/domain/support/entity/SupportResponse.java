package com.fineapple.domain.support.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SupportResponse {
    private Long answerId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long inquiryId;
    private Long userInfo;
    private String languageCode;

    @Builder
    public SupportResponse(Long answerId, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Long inquiryId,
                           Long userInfo, String languageCode) {
        this.answerId = answerId;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.inquiryId = inquiryId;
        this.userInfo = userInfo;
        this.languageCode = languageCode;
    }
}

