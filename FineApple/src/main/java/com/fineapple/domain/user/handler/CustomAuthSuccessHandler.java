package com.fineapple.domain.user.handler;

import com.fineapple.domain.user.dto.UserDetailDto;
import com.fineapple.domain.user.service.LoginAttemptService;
import com.fineapple.domain.user.service.UserDetailServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final UserDetailServiceImp userDetailService;
    private final LoginAttemptService loginAttemptService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {
        log.debug("CustomAuthSuccessHandler 호출됨");

        String username = authentication.getName();

        // 로그인 성공 시 오류 메시지 제거
        request.getSession().removeAttribute("errorMessage");

        // 세션에서 잠금 상태 확인
        Boolean isLocked = (Boolean) request.getSession().getAttribute("locked_" + username);

        if (isLocked != null && isLocked) {
            // 계정이 잠겨 있으면 로그인 차단
            String lockMessage = loginAttemptService.getLockMessage(username);
            request.getSession().setAttribute("errorMessage", lockMessage);
            response.sendRedirect("/login?error=blocked");
            return;
        }

        HttpSession session = request.getSession();
        UserDetailDto userDetail = userDetailService.getLoginUserByUsername(username);

        if (userDetail != null) {
            session.setAttribute("loginUser", userDetail);
            log.debug("사용자 실명: {}", userDetail);
        }

        // 로그인 후 홈으로 리다이렉트
        response.sendRedirect("/");
    }

}
