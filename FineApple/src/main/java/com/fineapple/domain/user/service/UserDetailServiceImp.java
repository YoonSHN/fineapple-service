package com.fineapple.domain.user.service;


import com.fineapple.Infrastructure.exception.*;
import com.fineapple.domain.user.dto.UserDetailDto;
import com.fineapple.domain.user.dto.UserRegistrationDto;
import com.fineapple.domain.user.dto.UserUpdateDto;
import com.fineapple.domain.user.entity.Address;
import com.fineapple.domain.user.entity.User;
import com.fineapple.domain.user.entity.UserInfo;
import com.fineapple.domain.user.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static org.aspectj.bridge.MessageUtil.getMessages;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImp implements UserDetailsService, UserDetailService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 역할 코드 상수
    private static final String ROLE_USER = "ME0101";
    private static final String ROLE_MANAGER = "ME0102";
    private static final String ROLE_ADMIN = "ME0103";
    private static final String STATUS_ACTIVE = "ME0201";

    public UserDetailDto getUserDetailByEmail(String email) {
        return userMapper.findUserDetailByEmail(email);
    }

    public UserDetailDto getLoginUserByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    public String getUserRealNameById(Long id) {
        return userMapper.findUserRealNameById(id);
    }

    /**
     * 입력된 email DB의 email 비교하여 필요한 사용자 정보를 가져옴
     * @param email 사용자 ID (이메일)
     * 사용자가 로그인할 때마다 실행
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        log.debug("DB에서 조회된 사용자 이메일: {}", user.getEmail());
        List<GrantedAuthority> authorities = getAuthorities(user.getUserRole());
        user.setAuthorities(authorities);

        return user;
    }


    /**
     * isAdminRole 반환 값 (true : 관리가 권한  false : 일반 회원)
     * @param userRole 사용자 권한 코드
     */
    private List<GrantedAuthority> getAuthorities(String userRole) {
        return Collections.singletonList(new SimpleGrantedAuthority(
                isAdminRole(userRole) ? "ROLE_ADMIN" : "ROLE_USER"
        ));
    }


    /**
     * 권한 코드가 ME0102 or ME0103 이면 true 반환
     * @param userRole 사용자 권한 코드
     */
    // 권한 코드가 ME0102 or ME0103 이면 true 반환
    private boolean isAdminRole(String userRole) {
        return ROLE_ADMIN.equals(userRole) || ROLE_MANAGER.equals(userRole);
    }


    /**
     * 트랜잭션 적용: 이 메서드가 실행되는 동안 데이터베이스 변경 사항이 일괄적으로 처리, 모든 예외 발생시 롤백
     * 시큐리티(UserDetailsService) 회원가입 메서드 오버라이딩
     * @param dto 회원가입 정보
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void registerUser(UserRegistrationDto dto) {
        log.debug("회원가입 요청: {}", dto); // 회원가입 시 어떤 데이터가 들어오는지 확인
        try {
            // 1. 유효성 검사 수행
            validateRegistrationData(dto);
            // 2. 사용자 데이터 준비 (비밀번호 암호화 포함)
            prepareUserForRegistration(dto);
            // 3. 사용자 데이터 DB에 저장
            insertUserData(dto);
        } catch (DataIntegrityViolationException e) {
            log.error("회원가입 중 예외 발생", e);
            throw new DuplicateEmailException();
        } catch (InvalidPasswordException e) {
            log.error("회원가입 중 예외 발생", e);
            throw e;
        } catch (Exception e) {
            log.error("회원가입 중 예외 발생", e);
            throw new UserRegistrationException("오류 : " + e.getMessage());
        }
    }

    /**
     * 회원가입 전 준비 단계
     * @param dto 회원가입 정보
     */
    private void prepareUserForRegistration(UserRegistrationDto dto) {
        // 비밀번호 암호화 처리
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        // 역할 코드가 없다면 기본 ROLE_USER로 설정
        dto.setUserRole(dto.getUserRole() == null || dto.getUserRole().isEmpty() ? ROLE_USER : dto.getUserRole());
        // 생성 일시 설정
        dto.setCreateDate(LocalDateTime.now());
        // 사용자 상태 활성으로 설정
        dto.setUserStatus(STATUS_ACTIVE);
    }

    /**
     * 회원가입 정보를 User, UserInfo 엔티티에 저장
     * @param dto 회원가입 정보 dto
     */
    private void insertUserData(UserRegistrationDto dto) {
        userMapper.insertUser(dto);
        userMapper.insertUserInfo(dto);
    }

    private void validateRegistrationData(UserRegistrationDto dto) {
        // 비밀번호 최소 길이 검사
        if (dto.getPassword().length() < 3) {
            throw new InvalidPasswordException("비밀번호는 3자 이상이어야 합니다.");
        }

        // 이름 유효성 검사 (한글, 영문만 허용)
        String nameRegex = "^[가-힣a-zA-Z]+$";
        if (!Pattern.matches(nameRegex, dto.getName())) {
            throw new InvalidNameException("이름에는 한글과 영문만 사용 가능합니다.");
        }

        // 비밀번호 확인 검사
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new InvalidPasswordException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        // 이메일 유효성 검사
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!Pattern.matches(emailRegex, dto.getEmail())) {
            throw new InvalidEmailException();
        }

        // 전화번호 유효성 검사 (010-xxxx-xxxx 형식)
        String phoneRegex = "^01[0-9]-\\d{3,4}-\\d{4}$";
        if (!Pattern.matches(phoneRegex, dto.getTel())) {
            throw new InvalidPhoneNumberException("전화번호 형식이 올바르지 않습니다. (예: 010-1234-5678)");
        }
    }


    /**
     * 유저 정보 수정
     * @param dto 회원정보 수정 dto
     */
    @Transactional
    public void updateUserAll(UserUpdateDto dto) {

        // 1. UserInfo ID 확인 필수
        if (dto.getUserInfoId() == null) {
            throw new UserInfoNotFoundException(); // 사용자 정의 예외 던지기
        }

        // 2. UserInfo 업데이트
        userMapper.updateUserInfo(dto);

        // 3. Address 업데이트 (존재하면 update, 없으면 insert)
        Address existing = userMapper.findAddressByUserInfoId(dto.getUserInfoId());

        if (existing == null) {
            userMapper.insertAddress(dto); // INSERT 시 deliveryId 자동 세팅됨
        } else {
            dto.setDeliveryId(existing.getDeliveryId()); // 기존 deliveryId 세팅 후 업데이트
            userMapper.updateAddress(dto);
        }
    }


    public UserUpdateDto getAddressByUserId(Long userId) {
        // 1. 유저 확인
        User user = userMapper.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");
        }

        // 2. UserInfo 조회
        UserInfo userInfo = userMapper.findUserInfoIdById(userId);
        if (userInfo == null) {
            throw new RuntimeException("UserInfo가 존재하지 않습니다.");
        }

        Long userInfoId = userInfo.getUserInfo();
        Address address = userMapper.findAddressByUserInfoId(userInfoId);

        UserUpdateDto dto = new UserUpdateDto();

        // userId, userInfoId 세팅
        dto.setUserId(userId);
        dto.setUserInfoId(userInfoId);

        if (address != null) {
            dto.setDeliveryId(address.getDeliveryId());
            dto.setName(address.getName());
            dto.setTel(address.getTel());
            dto.setCountry(address.getCountry());
            dto.setCity(address.getCity());
            dto.setRegion(address.getRegion());
            dto.setPostNum(address.getPostNum());
            dto.setRoadNum(address.getRoadNum());
            dto.setAddress(address.getAddress());
            dto.setIsDefault(address.getIsDefault());
        }

        return dto;
    }

    /**
     * 유저 상태코드 업데이트
     *
     * @param userId 사용자 ID
     */
    @Override
    public void updateUserStatus(Long userId) {
        Long id = userMapper.findUserById(userId).getUserId();

        if (id == null) {
            throw new RuntimeException("해당 사용자를 찾을 수 없습니다.");
        }

        userMapper.updateUserStatusByUserId(id);
    }

}
