package com.fineapple.domain.user.service;

import com.fineapple.domain.user.dto.UserDetailDto;
import com.fineapple.domain.user.dto.UserDto;
import com.fineapple.domain.user.dto.UserSearchParam;
import com.fineapple.domain.user.entity.User;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {

    @Transactional(readOnly = true)
    PageInfo<UserDto> getAllUsers(int pageNum, int pageSize);

    @Transactional(readOnly = true)
    UserDetailDto getUserDetail(Long userId);

    @Transactional
    void updateUserIsActive(Long userId, boolean isActive);

    @Transactional(readOnly = true)
    PageInfo<UserDto> searchUsers(UserSearchParam param, int pageNum, int pageSize);

    UserDto toUserDto(User user);
}
