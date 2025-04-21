package com.fineapple.domain.user.service;

import com.fineapple.domain.user.dto.UserRegistrationDto;
import org.springframework.transaction.annotation.Transactional;

public interface UserDetailService {

    @Transactional
    void updateUserStatus(Long userId);

    @Transactional(rollbackFor = Exception.class)
    void registerUser(UserRegistrationDto dto);
}