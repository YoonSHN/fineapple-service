package com.fineapple.domain.user.service;



import com.fineapple.application.common.CommonCodeService;
import com.fineapple.application.exception.DataUpdateException;
import com.fineapple.application.exception.InvalidUserStatusException;
import com.fineapple.application.exception.UserNotFoundException;
import com.fineapple.domain.user.dto.*;
import com.fineapple.domain.user.entity.User;
import com.fineapple.domain.user.repository.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * 사용자 계정 관리 기능을 제공하는 서비스 구현체
 * <p>
 * - 사용자 목록 조회, 상세 조회, 활성 상태 업데이트, 검색 등의 기능을 제공
 */
@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserMapper userMapper;
    private final CommonCodeService cs;
    private final MessageSource messageSource;

    private String getMessages(String code, Object... args) {
        return messageSource.getMessage(code, args, Locale.getDefault());
    }

    /**
     * 모든 유저 정보 조회
     *
     * @param pageNum  페이지 숫자
     * @param pageSize 페이지 크기
     */
    @Override
    @Transactional(readOnly = true)
    public PageInfo<UserDto> getAllUsers(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserDto> userDtoList = userMapper.findAllUsers().stream()
                .map(this::toUserDto).collect(Collectors.toList());
        return new PageInfo<>(userDtoList);
    }

    /**
     * 유저 상세 정보 조회
     *
     * @param userId 사용자 ID
     */
    @Transactional(readOnly = true)
    @Override
    public UserDetailDto getUserDetail(Long userId) {

        User user = userMapper.findUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(
                    getMessages("error.user.not_found", userId)
            );
        }

        return userMapper.findUserDetailById(userId);

    }

    /**
     * 유저 활성화 여부 업데이트
     *
     * @param userId   사용자 ID
     * @param isActive 활성화 여부
     */
    @Transactional
    @Override
    public void updateUserIsActive(Long userId, boolean isActive) {
        User user = userMapper.findUserByIdForUpdate(userId);
        if (user == null) {
            throw new UserNotFoundException(
                    getMessages("error.user.not_found", userId)
            );
        }

        if (user.getIsActive() == isActive) {
            throw new InvalidUserStatusException(
                    getMessages("error.user.status.duplicated", userId)
            );

        }

        int updatedRows = userMapper.updateUserIsActive(userId, isActive);
        if (updatedRows == 0) {
            throw new DataUpdateException(
                    getMessages("error.user.status.update_failed", userId)
            );
        }

        String codeKey = isActive ? "ACTIVITY" : "PAUSE";
        String statusCode = cs.getCommonCode(codeKey).getCode();

        userMapper.updateUserStatus(userId, statusCode);
    }

    /**
     * 유저 검색
     *
     * @param param    검색 키워드
     * @param pageNum  페이지 숫자
     * @param pageSize 페이지 사이즈
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo<UserDto> searchUsers(UserSearchParam param, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);
        List<UserDto> userList = userMapper.searchUserList(param);
        return new PageInfo<>(userList);
    }


    @Override
    public UserDto toUserDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getEmail(),
                user.getCreateDate(),
                user.getUpdatedAt(),
                user.getIsActive()
        );

    }


}
