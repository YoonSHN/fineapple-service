package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class LoginHistory {
    private Long loginhistoryId;
    private Long userId;
    private LocalDateTime loginTime;
    private String ipAddress;
    private String deviceInfo;
    private String loginStatus;

    @Builder
    public LoginHistory(Long loginhistoryId, Long userId, LocalDateTime loginTime, String ipAddress, String deviceInfo, String loginStatus) {
        this.loginhistoryId = loginhistoryId;
        this.userId = userId;
        this.loginTime = loginTime;
        this.ipAddress = ipAddress;
        this.deviceInfo = deviceInfo;
        this.loginStatus = loginStatus;
    }
}
