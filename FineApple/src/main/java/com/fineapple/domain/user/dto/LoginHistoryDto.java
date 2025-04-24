package com.fineapple.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginHistoryDto {
    private Long loginHistoryId;
    private Long userId;
    private LocalDate loginTime;
    private String ipAddress;
    private String deviceInfo;
    private String loginStatus;
}
