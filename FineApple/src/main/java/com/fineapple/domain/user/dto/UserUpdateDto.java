package com.fineapple.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserUpdateDto {
    @Pattern(
            regexp = "^[가-힣a-zA-Z]{2,20}$",
            message = "이름은 특수문자 없이 2~20자 이내로 입력해주세요."
    )
    private String name;

    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "전화번호는 010-0000-0000 형식으로 입력해주세요."
    )
    private String tel;

//    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private LocalDate birth;

    private Long userInfoId;
    private String country;
    private String city;
    private String region;
    private String postNum;
    private String roadNum;
    private String address;
    private Boolean isDefault;
    private Long deliveryId;
    private LocalDateTime updated_at;
    private long userId;

}