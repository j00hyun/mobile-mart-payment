package com.automart.security.jwt;

import com.automart.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

// Json 웹 토큰을 생성하고 확인
@Component
public class JwtTokenProvider {

    @Value("spring.jwt.access.token.secret")
    private String accessTokenSecret;
    @Value("spring.jwt.refresh.token.secret")
    private String refreshTokenSecret;

    private String secretKey;

    private static final long accessTokenExpiredMsc = 1000L * 60 * 60; // 1시간만 유효
    private static final long refreshTokenExpiredMsc = 1000L * 60 * 60 * 24 * 14; // 2주간 유효

    private static final String HEADER_NAME = "Authorization";

    @PostConstruct
    protected void init() {
        accessTokenSecret = Base64.getEncoder().encodeToString(accessTokenSecret.getBytes());
        refreshTokenSecret = Base64.getEncoder().encodeToString(refreshTokenSecret.getBytes());
    }

    // Access Token 발급
    public String generateToken(UserPrincipal userPrincipal) {
        secretKey = accessTokenSecret;
        return createToken(String.valueOf(userPrincipal.getNo()), accessTokenExpiredMsc);
    }

    // Refresh Token 발급
    public String generateRefreshToken(UserPrincipal userPrincipal) {
        secretKey = refreshTokenSecret;
        return createToken(String.valueOf(userPrincipal.getNo()), refreshTokenExpiredMsc);
    }

    // 토큰 생성
    public String createToken(String userNo, long expireTime) {

        Claims claims = Jwts.claims().setSubject(userNo);
        // claims.put("roles", roles); // need params List<String> roles;
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발급일자
                .setExpiration(new Date(now.getTime() + expireTime)) // 토큰 만료기간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Jwt 토큰 유효성검사
    public boolean validateToken(String jwtToken) {
        try {
            // 1. setSigningKey를 통해 디지털 서명되었는지를 확인한다.
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwtToken);
            // 2. 만료일자가 지났는지 확인한다.
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Jwt 토큰에서 회원 구별 정보 추출
    public String getUserNo(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Request의 Header에서 token 파싱 : "JAuth_TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(HEADER_NAME);
    }
}