package com.fineapple.application.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;

import java.io.IOException;
import java.util.UUID;

/**
 * 사용자 방문기록 로그를 MDC에 저장
 */
@Slf4j
public class AccessLogFilter implements Filter {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        long startTime = System.currentTimeMillis();

        String traceId = (String) request.getAttribute(TRACE_ID_HEADER);
        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
        }

        MDC.put("traceId", traceId);
        MDC.put("clientIp", getClientIp(request));
        MDC.put("userAgent", request.getHeader("User-Agent"));
        MDC.put("requestUri", request.getRequestURI());
        MDC.put("method", request.getMethod());


        if (request.getUserPrincipal() != null) {
            MDC.put("userId", request.getUserPrincipal().getName());
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            MDC.put("status", String.valueOf(response.getStatus()));
            MDC.put("responseTimeMs", String.valueOf(duration));

            log.info("AccessLog | traceId={} | userId={} | ip={} | method={} | uri={} | status={} | duration={}ms",
                    MDC.get("traceId"),
                    MDC.get("userId"),
                    MDC.get("clientIp"),
                    MDC.get("method"),
                    MDC.get("requestUri"),
                    MDC.get("status"),
                    MDC.get("responseTimeMs"));

            MDC.clear();
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String[] headers = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP"
        };
        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0];
            }
        }
        return request.getRemoteAddr();
    }
}
