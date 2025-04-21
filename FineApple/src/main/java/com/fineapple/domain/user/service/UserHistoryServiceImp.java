package com.fineapple.domain.user.service;

import com.fineapple.domain.user.dto.LoginHistoryDto;
import com.fineapple.domain.user.dto.LoginHistorySearchParam;
import com.fineapple.domain.user.dto.UserProfileHistoryDto;
import com.fineapple.domain.user.dto.UserProfileHistorySearchParam;
import com.fineapple.domain.user.repository.UserHistoryMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * 사용자 활동 이력 기능을 제공하는 서비스 구현체
 * <p>
 * - 사용자 프로필 변경 이력, 로그인 이력, 방문자 수 통계를 제공
 * - 관리자 대시보드
 */
@Service
@RequiredArgsConstructor
public class UserHistoryServiceImp implements UserHistoryService {

    private final UserHistoryMapper userHistoryMapper;

    /**
     * 유저프로파일 히스토리 검색
     *
     * @param param    검색 파라미터 (userId, fieldChanged, startDate, endDate 등)
     * @param pageNum  페이지 번호
     * @param pageSize 페이지 크기
     * @return UserProfileHistoryDto
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<UserProfileHistoryDto> searchUserProfileHistory(UserProfileHistorySearchParam param, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserProfileHistoryDto> historyList = userHistoryMapper.searchUserProfileHistory(param);
        return new PageInfo<>(historyList);
    }

    /**
     * 로그인 히스토리 검색
     *
     * @param param    검색 파라미터 (userId, ipAddress, loginStatus, startDate, endDate 등)
     * @param pageNum  페이지 번호
     * @param pageSize 페이지 크기
     * @return LoginHistoryDto
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<LoginHistoryDto> searchLoginHistory(LoginHistorySearchParam param, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<LoginHistoryDto> loginHistoryList = userHistoryMapper.searchLoginHistory(param);
        return new PageInfo<>(loginHistoryList);
    }

    /**
     * 방문자수 통계
     *
     * @return Integer
     */
    @Transactional(readOnly = true)
    @Override
    public Integer countUserByPeriod(LocalDate startDate, LocalDate endDate) {
        return userHistoryMapper.countUserWeekday(startDate, endDate);
    }
}
