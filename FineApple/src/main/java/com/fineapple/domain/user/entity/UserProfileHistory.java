package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class UserProfileHistory {
    private Long profileHistoryId;
    private Long userId;
    private String fieldChanged;
    private String previousValue;
    private String newValue;
    private Long changedBy;
    private LocalDateTime changedAt;

    @Builder
    public UserProfileHistory(Long profileHistoryId, Long userId, String fieldChanged, String previousValue, String newValue, Long changedBy, LocalDateTime changedAt) {
        this.profileHistoryId = profileHistoryId;
        this.userId = userId;
        this.fieldChanged = fieldChanged;
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
    }
}
