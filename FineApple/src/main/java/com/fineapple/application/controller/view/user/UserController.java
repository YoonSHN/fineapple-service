package com.fineapple.application.controller.view.user;

import com.fineapple.domain.user.dto.UserDetailDto;
import com.fineapple.domain.user.dto.UserRegistrationDto;
import com.fineapple.domain.user.dto.UserUpdateDto;
import com.fineapple.domain.user.entity.User;
import com.fineapple.application.exception.DuplicateEmailException;
import com.fineapple.application.exception.UserRegistrationException;
import com.fineapple.domain.user.service.UserDetailServiceImp;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequiredArgsConstructor
@Controller
@Slf4j
public class UserController {

    private final UserDetailServiceImp userDetailService;

    @PostMapping("/login")
    public void loginSuccess(HttpSession session, Authentication authentication,
                             HttpServletRequest request,
                             Model model,
                             @RequestParam(value = "error", required = false) String error,
                             @RequestParam(value = "errorMessage", required = false) String errorParam
                             ) {
        if (authentication != null) {
            log.debug("로그인 성공");
            String username = authentication.getName();  // 현재 로그인한 사용자 이메일 가져오기
            UserDetailDto userDetail = userDetailService.getLoginUserByUsername(username); // DB에서 사용자 정보 조회

            if (userDetail != null) {
                log.info("세션 사용자 이름: {}", userDetail);
                session.setAttribute("loginUser", userDetail); // 세션에 저장
            }
        }

        if (error != null) {
            String errorMessage = errorParam != null ? errorParam :
                    (String) request.getSession().getAttribute("errorMessage");
            if (errorMessage != null) {
                model.addAttribute("errorMessage", errorMessage);
                request.getSession().removeAttribute("errorMessage");
            }
        }

    }


    @GetMapping("/userDetail")
    public String getUserDetail(Model model, Authentication authentication) {
        // 1. 현재 인증된 사용자 정보에서 이메일(사용자 이름) 가져오기
        String email = authentication.getName(); // Spring 시큐리티에서 제공하는 Authentication 객체를 통해 로그인된 사용자의 이메일을 가져옴

        // 2. 이메일을 이용해 사용자 상세 정보 조회
        UserDetailDto userDetail = userDetailService.getUserDetailByEmail(email);
        log.info("우편번호 조회: {}", userDetail.getPostNum());
        log.info("주소: {}", userDetail.getAddress());

        // 3. 조회한 사용자 정보를 모델에 추가하여 뷰에서 사용할 수 있도록 설정
        model.addAttribute("userDetail", userDetail);

        // 4. "memberdetail" 뷰 반환하여 사용자 상세 정보를 화면에 출력
        return "memberdetail";
    }


    // 회원가입 처리
    @PostMapping("/save")
    public String registerUser(
            @Valid UserRegistrationDto registrationDto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            // BindingResult 에러를 flash로 저장하고 redirect
            redirectAttributes.addFlashAttribute("errors", result.getAllErrors());
            redirectAttributes.addFlashAttribute("dto", registrationDto); // 입력값 유지
            return "redirect:/signup";
        }

        try {
            userDetailService.registerUser(registrationDto);
            return "redirect:/login?success";
        } catch (DuplicateEmailException | UserRegistrationException e) {
            // 예외 메시지를 flash로 전달하고 redirect
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("dto", registrationDto); // 입력값 유지
            return "redirect:/signup";
        }
    }



    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        String errorMessage = (String) session.getAttribute("errorMessage");
        if (errorMessage != null) {
            model.addAttribute("errorMessage", errorMessage);
            session.removeAttribute("errorMessage");
        }
        return "login";
    }


    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        // 이전 입력값이 있다면 유지
        if (!model.containsAttribute("dto")) {
            model.addAttribute("dto", new UserRegistrationDto());
        }
        return "signup";
    }

    // 주소 수정 폼 페이지 이동
    @GetMapping("/addressUpdatePage")
    public String showUpdateAddressForm(Model model, @AuthenticationPrincipal User user) {
        Long userId = user.getUserId();

        // 현재 주소 정보 가져오기 (UserUpdateDto에 userId, userInfoId 등 포함되어야 함)
        UserUpdateDto currentAddress = userDetailService.getAddressByUserId(userId);

        // 로그인 사용자 정보가 DTO에 포함되도록 설정 (없다면)
        currentAddress.setUserId(userId);

        // 모델에 "UserUpdateDto" 이름으로 담아 View에 전달
        model.addAttribute("UserUpdateDto", currentAddress);

        return "updateAddress"; // updateAddress.html 페이지로 이동
    }

    // 주소 수정 처리 + 사용자 이름, 생일, 전화번호
    @PostMapping("/addressUpdate")
    public String updateAddress(@Valid @ModelAttribute("addressUpdateDto") UserUpdateDto dto,
                                @AuthenticationPrincipal User user) {
        Long userId = user.getUserId();
        // 로그인 사용자 정보가 DTO에 누락되지 않도록 설정
        dto.setUserId(userId);

        // 서비스에서 User, UserInfo, Address 테이블 모두 업데이트 처리
        userDetailService.updateUserAll(dto);

        return "redirect:/userDetail"; // 수정 후 사용자 상세 페이지로 리다이렉트
    }

    /**
     * 유저 상태코드, 로그인 가능여부 업데이트 (소프트 삭제 처리 및 로그아웃)
     *
     * @param user 인증된 사용자 정보
     * @param request HTTP 요청 객체 (세션 접근용)
     * @return 리다이렉트
     */
    @PostMapping("/deactivation")
    public String updateUserStatusAndLogout(
            @AuthenticationPrincipal User user,
            HttpServletRequest request
    ) {
        // 1. 상태코드 업데이트 (소프트 삭제)
        userDetailService.updateUserStatus(user.getUserId());

        // 2. 세션 무효화 → 자동 로그아웃 처리
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // 현재 로그인 세션 종료
        }

        // 3. 로그아웃 이후 로그인 페이지 등으로 리다이렉트
        return "redirect:/login";
    }

}
