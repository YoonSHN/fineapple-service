package com.fineapple.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor // 기본 생성자 추가
public class UserRegistrationDto {
    private Long userId;
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private String tel;
    private String userRole;
    private String userStatus;
    private LocalDateTime createDate;
    private String confirmPassword;

    // 비밀번호 검증 메서드
    public boolean isPasswordConfirmed() {
        return password != null && password.equals(confirmPassword);
    }

    // 모든 필드를 포함하는 생성자는 유지
    @Builder
    public UserRegistrationDto(String email, String password, String name, LocalDate birth,
                               String tel, String userRole, String userStatus, LocalDateTime createDate) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birth = birth;
        this.tel = tel;
        this.userRole = userRole;
        this.userStatus = userStatus;
        this.createDate = createDate;
    }
}
