package com.fineapple.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileHistoryDto {
    private Long profileHistoryId;
    private Long userId;
    private String fieldChanged;
    private String previousValue;
    private String newValue;
    private Long changedBy;
    private LocalDateTime changedAt;
}
