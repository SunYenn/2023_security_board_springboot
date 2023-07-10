package com.example.back.Config;

import com.example.back.Service.AuthProvider;
import com.example.back.Service.JwtAuthorizationFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@EnableWebSecurity
@Configuration
@AllArgsConstructor
//웹 보안 기능의 초기화 및 설정을 가능하도록 해주는 WebSecurityConfigurerAdapter 클래스 상속
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProvider jwtTokenProvider;

    @Bean
    // CORS(Cross-Origin Resource Sharing) 구성을 정의하는 메서드
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        /**
         * cors : 웹 어플리케이션의 도메인이 다른 도메인의 리소스 대해 접근이 허용되는지 체크하는 것.
         * cross-origin HTTP request 요청 실행.
         */
        configuration.addAllowedOrigin("http://localhost:3000"); //교차 출처 요청에 대한 허용 도메인 설정
        configuration.addAllowedMethod("*"); // 허용 메소드 설정
        configuration.addAllowedHeader("*"); // 허용 헤더 설정
        configuration.addExposedHeader("accesstoken");
        configuration.addExposedHeader("content-disposition");
        configuration.setMaxAge((long) 3600); // CORS에서 응답이 브라우저에 의해 얼마 동안 캐시될 수 있는지 지정
        configuration.setAllowCredentials(false); // 다른 출처의 요청에 자격 증명이 예상되지 않거나 허용되지 않음

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 URL 패턴의 cors 구성 객체 등록

        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(jwtTokenProvider);

        http
                .httpBasic().disable()
                // csrf : 사이즈간 위조 요청.
                // rest api 서버는 session 기반 인증과 다르게 stateless 하기에 서버에 인증정보를 보관하지 않는다. 그러므로 disabled
                .csrf().disable()

                // 서버가 클라이언트의 상태를 기억하지 않고 세션을 생성하지 않도록 하는 것.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 커스텀 필터를 거쳐 JWT 인증 수행
                .and()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests() //요청에 대한 접근 제한을 설정하는 메서드
                // Preflight Request라는 실제 요청 전에 보내지는 CORS 요청에 대해 모두 접근 허용
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/api/user/*").permitAll() // 회원가입, 로그인, 중복계정 체크는 토큰 없어도 허용
                .anyRequest().hasRole("USER")			  // 이외 나머지는 USER 권한필요
                .and()
                .cors(); // CORS(Cross-Origin Resource Sharing)를 활성화
    }
}

