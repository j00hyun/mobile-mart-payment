package com.automart.jwt;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Component
// Jwt 토큰 생성 및 유효성 검증을 위한 컴포넌트
public class JwtTokenProvider {

    @Value("spring.jwt.secret")
    private String secretKey;

    private long tokenValidMilisecond = 1000L * 60 * 60; // 1시간만 토큰 유효
    public static final String HEADER_NAME = "JAuth_TOKEN";

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // Jwt 토큰 생성
    public String createToken(String userNo, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userNo); // 토큰제목, 여기서는 UserNo로 사용했음. 이메일도 가능은함.
        claims.put("roles", roles); // 권한설정
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발급일자
                .setExpiration(new Date(now.getTime() + tokenValidMilisecond)) // 토큰 만료일자
                .signWith(SignatureAlgorithm.HS256, secretKey) // secretKey를 Hash
                .compact();
    }

    // Jwt 토큰 유효성검사
    public boolean validateToken(String jwtToken) {
        try {
            // 1. setSigningKey를 통해 디지털 서명되었는지를 확인한다.
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            // 2. 만료일자가 지났는지 확인한다.
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Jwt 토큰으로 인증 정보를 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserNo(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserNo(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // Request의 Header에서 token 파싱 : "X-AUTH_TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HEADER_NAME);
    }
}