package com.fineapple.domain.user.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.fineapple.domain.user.service.LoginAttemptService;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final LoginAttemptService loginAttemptService;
    private final UserDetailsService userDetailsService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");

        if (username == null) {
            response.sendRedirect("/login?error=true");
            return;
        }

        boolean userExists = true;

        try {
            userDetailsService.loadUserByUsername(username); // 여기서 사용자 존재 확인
        } catch (UsernameNotFoundException e) {
            userExists = false;
        }

        // 존재하는 사용자만 로그인 실패 처리
        if (userExists) {
            loginAttemptService.loginFailed(username);
        }

        String errorMessage;
        if (!userExists) {
            errorMessage = "아이디 또는 비밀번호가 잘못 입력됬습니다.";
        } else {
            errorMessage = getErrorMessage(exception, username);
        }

        request.getSession().setAttribute("errorMessage", errorMessage);
        response.sendRedirect("/login?error=true");
    }

    private String getErrorMessage(AuthenticationException ex, String username) {
        String lockMessage = loginAttemptService.getLockMessage(username);
        if (lockMessage != null) return lockMessage;

        int remainingAttempts = loginAttemptService.getRemainingAttempts(username);

        if (ex instanceof BadCredentialsException) {
            return "잘못된 계정 정보입니다. (남은 시도 횟수: " + remainingAttempts + ")";
        } else if (ex instanceof DisabledException) {
            return "비활성화된 계정입니다.";
        } else if (ex instanceof LockedException) {
            return "이미 잠긴 계정입니다.";
        }
        return "로그인에 실패했습니다.";
    }

}
