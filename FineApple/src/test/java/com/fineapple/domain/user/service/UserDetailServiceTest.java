package com.fineapple.domain.user.service;

import com.fineapple.domain.user.dto.UserUpdateDto;
import com.fineapple.domain.user.entity.UserInfo;
import com.fineapple.domain.user.repository.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트 후 자동 롤백됨 (데이터 깨끗하게 유지 가능)
class UserDetailServiceTest {

    @Autowired
    private UserDetailServiceImp userService;

    @Autowired
    private UserMapper userMapper;

    @Test
    void testUpdateUserAll_InsertNewAddress() {
        // given
        Long userId = 1L;
        Long userInfoId = 1L;


        UserUpdateDto dto = new UserUpdateDto();
        dto.setUserId(userId);
        dto.setUserInfoId(userInfoId);
        dto.setName("홍길동");
        dto.setTel("010-1234-5678");
        dto.setCountry("대한민국");
        dto.setCity("서울");
        dto.setRegion("강남구");
        dto.setPostNum("12345");
        dto.setRoadNum("123");
        dto.setAddress("강남대로 1길");
        dto.setIsDefault(true);
        dto.setBirth(LocalDate.now());


        userService.updateUserAll(dto);


        // 1. UserInfo 갱신 확인
        UserInfo updatedUserInfo = userMapper.findUserInfoIdById(userId);
        assertThat(updatedUserInfo.getName()).isEqualTo("홍길동");
        assertThat(updatedUserInfo.getTel()).isEqualTo("010-1234-5678");

        // 2. Address 삽입 확인
        var address = userMapper.findAddressByUserInfoId(userInfoId);
        assertThat(address).isNotNull();
        assertThat(address.getCity()).isEqualTo("서울");
        assertThat(address.getRegion()).isEqualTo("강남구");
    }
}
