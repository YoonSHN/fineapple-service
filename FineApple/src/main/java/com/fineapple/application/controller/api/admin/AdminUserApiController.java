package com.fineapple.application.controller.api.admin;

import com.fineapple.domain.user.dto.*;
import com.fineapple.domain.user.service.UserHistoryService;
import com.fineapple.domain.user.service.UserService;
import com.github.pagehelper.PageInfo;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 관리자 전용 회원 관리 API 컨트롤러
 * <p>
 * - 관리자 권한(ROLE_ADMIN)을 가진 사용자만 접근 가능
 * - 회원 목록, 상세 정보, 검색, 활성화 상태 변경, 프로필/로그인 히스토리 열람 기능을 제공
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminUserApiController {

    private final UserService userService;
    private final UserHistoryService userHistoryService;

    @Operation(summary = "회원 가입정보 전체 열람")
    @GetMapping
    public ResponseEntity<PageInfo<UserDto>> getUsers(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<UserDto> users = userService.getAllUsers(pageNum, pageSize);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "회원 가입정보 상세 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserDetailDto> getUserDetail(@PathVariable Long userId) {
        UserDetailDto userDetail = userService.getUserDetail(userId);
        return ResponseEntity.ok(userDetail);
    }

    @Operation(summary = "회원 활성 상태 업데이트")
    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserActiveStatus(
            @PathVariable Long userId,
            @RequestParam boolean isActive) {
        try {
            userService.updateUserIsActive(userId, isActive);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("업데이트 실패 : ", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "회원 검색")
    @GetMapping("/search")
    public ResponseEntity<PageInfo<UserDto>> searchUsers(
            @Validated @ModelAttribute UserSearchParam searchParam,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<UserDto> users = userService.searchUsers(searchParam, pageNum, pageSize);
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "회원 프로파일 히스토리 검색")
    @GetMapping("/history/profile")
    public ResponseEntity<PageInfo<UserProfileHistoryDto>> searchUserProfileHistory(
            @Validated @ModelAttribute UserProfileHistorySearchParam searchParam,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<UserProfileHistoryDto> history = userHistoryService.searchUserProfileHistory(searchParam, pageNum, pageSize);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "로그인 히스토리 검색")
    @GetMapping("/history/login")
    public ResponseEntity<PageInfo<LoginHistoryDto>> searchLoginHistory(
            @Validated @ModelAttribute LoginHistorySearchParam searchParam,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<LoginHistoryDto> history = userHistoryService.searchLoginHistory(searchParam, pageNum, pageSize);
        return ResponseEntity.ok(history);
    }
}

