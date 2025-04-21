package com.fineapple.domain.user.service;

import com.fineapple.domain.user.dto.LoginHistoryDto;
import com.fineapple.domain.user.dto.LoginHistorySearchParam;
import com.fineapple.domain.user.dto.UserProfileHistoryDto;
import com.fineapple.domain.user.dto.UserProfileHistorySearchParam;
import com.github.pagehelper.PageInfo;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

public interface UserHistoryService {
    @Transactional(readOnly = true)
    PageInfo<UserProfileHistoryDto> searchUserProfileHistory(UserProfileHistorySearchParam param, int pageNum, int pageSize);

    @Transactional(readOnly = true)
    PageInfo<LoginHistoryDto> searchLoginHistory(LoginHistorySearchParam param, int pageNum, int pageSize);

    @Transactional(readOnly = true)
    Integer countUserByPeriod(LocalDate startDate, LocalDate endDate);
}
