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
public class UserDetailDto {
    private Long userId;
    private String email;
    private boolean isActive;
    private LocalDate createDate;
    private LocalDate updatedAt;
    private String name;
    private String tel;
    private LocalDate birth;
    private String userRole;
    private String userStatus;
    private Long deliveryId;
    private String address;
    private String city;
    private String country;
    private String region;
    private String postNum;

    public UserDetailDto(
            Long id, String email, String name, String tel, LocalDate birth, String address,
            String city, String country, String region, String postNum) {
        this.userId = id;
        this.email = email;
        this.name = name;
        this.tel = tel;
        this.birth = birth;
        this.address = address;
        this.city = city;
        this.country = country;
        this.region = region;
        this.postNum = postNum;
    }

}
