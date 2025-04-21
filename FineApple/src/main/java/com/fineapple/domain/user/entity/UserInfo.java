package com.fineapple.domain.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserInfo {
    private Long userInfo;
    private String name;
    private Date birth;
    private String tel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
    private String userRole;
    private String userStatus;

    @Builder
    public UserInfo(Long userInfo, String name, Date birth, String tel, LocalDateTime createdAt, LocalDateTime updatedAt,
                    Long userId, String userRole, String userStatus) {
        this.userInfo = userInfo;
        this.name = name;
        this.birth = birth;
        this.tel = tel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.userId = userId;
        this.userRole = userRole;
        this.userStatus = userStatus;
    }

    // 테스트용 setter
    public void setUserInfo(Long userInfo) {
        this.userInfo = userInfo;
    }
}
