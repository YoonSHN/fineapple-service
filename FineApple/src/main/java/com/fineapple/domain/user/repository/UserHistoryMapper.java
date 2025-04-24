package com.fineapple.domain.user.repository;

import com.fineapple.domain.user.dto.LoginHistoryDto;
import com.fineapple.domain.user.dto.LoginHistorySearchParam;
import com.fineapple.domain.user.dto.UserProfileHistoryDto;
import com.fineapple.domain.user.dto.UserProfileHistorySearchParam;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserHistoryMapper {

    List<UserProfileHistoryDto> searchUserProfileHistory(UserProfileHistorySearchParam param);

    List<LoginHistoryDto> searchLoginHistory(LoginHistorySearchParam param);

    Integer countUserWeekday(LocalDate startDate, LocalDate endDate);

}
