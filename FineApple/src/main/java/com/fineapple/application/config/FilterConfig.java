package com.fineapple.application.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  로그인 방묵기록을 필터로 등록
 *  필터 순서는 1번
 *
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AccessLogFilter> filterRegistrationBean() {
        FilterRegistrationBean<AccessLogFilter> registrationBean = new FilterRegistrationBean<AccessLogFilter>();
        registrationBean.setFilter(new AccessLogFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
