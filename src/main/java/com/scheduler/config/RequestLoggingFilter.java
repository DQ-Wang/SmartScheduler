package com.scheduler.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        ContentCachingRequestWrapper wrapped = new ContentCachingRequestWrapper(request);
        try {
            filterChain.doFilter(wrapped, response);
        } finally {
            long cost = System.currentTimeMillis() - start;
            String body = new String(wrapped.getContentAsByteArray(), StandardCharsets.UTF_8);
            if (body.length() > 2000) {
                body = body.substring(0, 2000) + "...";
            }
            log.info("请求 {} {} | 参数={} | body={} | 状态={} | 耗时={}ms",
                    request.getMethod(),
                    request.getRequestURI(),
                    request.getQueryString(),
                    body.isBlank() ? "-" : body,
                    response.getStatus(),
                    cost);
        }
    }
}
