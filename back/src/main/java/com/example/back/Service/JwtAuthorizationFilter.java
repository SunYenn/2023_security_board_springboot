package com.example.back.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final AuthProvider jwtTokenProvider;

    public JwtAuthorizationFilter(AuthProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // accesstoken 헤더에서 JWT 추출
        String token = jwtTokenProvider.resolveToken(request);

        // JWT 검증
        if (token != null) {
            if(jwtTokenProvider.validateToken(token)) {
                // 토큰 만료여부 검사 후 유저정보 파싱
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        // 다음 필터로 요청과 응답 전달.
        filterChain.doFilter(request, response);
    }
}

