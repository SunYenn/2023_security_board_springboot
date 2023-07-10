package com.example.back.Service;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthProvider {

    private static final String BEARER_TYPE = "bearer ";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String ACCESS_USER_ID = "id";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 60;

    @Value("${spring.jwt.secret.signature}")
    private String signatureKey;

    @PostConstruct
    protected void init() {
        signatureKey = Base64.getEncoder().encodeToString(signatureKey.getBytes());
    }

    // 토큰 발행
    public String createToken(String id, String username, String role) {

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        final JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setExpiration(accessTokenExpiresIn)
                .claim(AUTHORITIES_KEY, role)
                .claim(ACCESS_USER_ID, id)
                .signWith(SignatureAlgorithm.HS256, signatureKey);

        return BEARER_TYPE + builder.compact();
    }

    // request 객체 헤더에 담겨 있는 토큰 가져오기
    public String resolveToken(HttpServletRequest request) {
        if (request.getHeader("accesstoken") == null) return null;
        return request.getHeader("accesstoken").replace(BEARER_TYPE, "");
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token).getBody().getSubject();
    }

    //컨텍스트에 해당 유저에 대한 권한을 전달하고 API 접근 전 접근 권한을 확인하여 접근 허용 또는 거부
    @SuppressWarnings("unchecked")
    public Authentication getAuthentication(String token) {

        // 토큰 기반으로 유저의 정보 파싱
        Claims claims = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token).getBody();

        String username = claims.getSubject();
        String id = claims.get(ACCESS_USER_ID, String.class);
        String role = claims.get(AUTHORITIES_KEY, String.class);

        CustomUserDetails userDetails = new CustomUserDetails(id, username, role);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 유효시간 만료여부 검사 실행
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

}
