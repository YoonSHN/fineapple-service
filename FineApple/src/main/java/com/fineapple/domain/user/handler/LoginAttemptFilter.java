package com.fineapple.domain.user.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fineapple.domain.user.service.LoginAttemptService;

import java.io.IOException;

@RequiredArgsConstructor
public class LoginAttemptFilter extends OncePerRequestFilter {
    private final LoginAttemptService loginAttemptService;

    // 로그인 시도 전에 차단된 계정이면 로그인 페이지로 리다이렉트.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String username = request.getParameter("username");

        if (username != null && loginAttemptService.isBlocked(username)) {
            response.sendRedirect("/login?error=blocked");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
