package com.fineapple.domain.user.repository;


import com.fineapple.domain.user.dto.*;
import com.fineapple.domain.user.entity.Address;
import com.fineapple.domain.user.entity.User;
import com.fineapple.domain.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {

    User findUserById(Long id);

    List<User> findAllUsers();

    UserDetailDto findUserDetailById(Long id);

    int updateUserIsActive(Long userId, boolean isActive);

    void updateUserStatus(Long userId,String userStatus);

    List<UserDto> searchUserList(UserSearchParam param);

    Optional<User> findByEmail(String email);

    void insertUser(UserRegistrationDto registrationDto);

    void insertUserInfo(UserRegistrationDto registrationDto);

    User findUserByIdForUpdate(Long userId);

    UserDetailDto findUserDetailByEmail(String email);

    UserDetailDto findByUsername(String username);

    void updateUserStatusByUserId(Long userId);

    UserInfo findUserInfoIdById(Long userId);

    Address findAddressByUserInfoId(Long userInfoId);

    void insertAddress(UserUpdateDto dto);

    void updateAddress(UserUpdateDto dto);

    void updateUserInfo(UserUpdateDto dto);

    String findUserRealNameById(Long id);
}
