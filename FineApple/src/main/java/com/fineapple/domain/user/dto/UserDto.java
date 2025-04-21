package com.fineapple.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userType;
    private Long userId;
    private String email;
    private String name;
    private String tel;
    private LocalDateTime createDate;
    private LocalDateTime updatedAt;
    private Boolean isActive;

    public UserDto(Long userId, String email, LocalDateTime createDate, LocalDateTime updatedAt, Boolean isActive) {

        this.userId = userId;
        this.email = email;
        this.createDate = createDate;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
    }
}