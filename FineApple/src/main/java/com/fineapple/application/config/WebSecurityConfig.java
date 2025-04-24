package com.fineapple.application.config;


import com.fineapple.domain.user.handler.CustomAccessDeniedHandler;
import com.fineapple.domain.user.handler.CustomAuthSuccessHandler;
import com.fineapple.domain.user.handler.CustomAuthenticationFailureHandler;
import com.fineapple.domain.user.handler.LoginAttemptFilter;
import com.fineapple.domain.user.service.LoginAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {
    private final UserDetailsService userDetailsService; // 인터페이스 타입 주입
    private final CustomAuthSuccessHandler customAuthSuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final LoginAttemptService loginAttemptService;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(loginAttemptFilter(), UsernamePasswordAuthenticationFilter.class)
                .formLogin(form -> form
                        .successHandler(customAuthSuccessHandler)
                        .loginPage("/login")
                        .failureHandler(customAuthenticationFailureHandler) // 커스텀 핸들러 등록
                        .defaultSuccessUrl("/", true)
                )
                .rememberMe(remember -> remember
                        .rememberMeParameter("remember-me") // 로그인 폼의 체크박스 이름
                        .key("uniqueAndSecretKey") // 보안을 위한 고유 키
                        .tokenValiditySeconds(86400 * 7) // 7일 동안 유효
                        .userDetailsService(userDetailsService) // 사용자 정보 서비스
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("remember-me")
                )
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
                .build();
    }

    @Bean
    public LoginAttemptFilter loginAttemptFilter() {
        return new LoginAttemptFilter(loginAttemptService);
    }


}