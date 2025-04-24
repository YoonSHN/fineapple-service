package com.fineapple.domain.support.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SupportAttachment {
    private Long attachmentId;
    private String fileUrl;
    private String fileName;
    private Integer fileSize;
    private LocalDateTime createdAt;
    private Long inquiryId;

    @Builder
    public SupportAttachment(Long attachmentId, String fileUrl, String fileName, Integer fileSize, LocalDateTime createdAt, Long inquiryId) {
        this.attachmentId = attachmentId;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.createdAt = createdAt;
        this.inquiryId = inquiryId;
    }
}
