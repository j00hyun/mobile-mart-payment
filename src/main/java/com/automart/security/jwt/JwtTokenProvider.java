package com.automart.security.jwt;

import com.automart.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

// Json 웹 토큰을 생성하고 확인
@Service
@Slf4j
public class JwtTokenProvider {

    @Value("${spring.jwt.access.token.secret}")
    private String accessTokenSecret;
    @Value("${spring.jwt.refresh.token.secret}")
    private String refreshTokenSecret;

    private static final long accessTokenExpiredMsc = 1000L * 60 * 60; // 1시간만 유효
    private static final long refreshTokenExpiredMsc = 1000L * 60 * 60 * 24 * 14; // 2주간 유효

    private static final String HEADER_NAME = "Authorization";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public enum TokenType { ACCESS_TOKEN, REFRESH_TOKEN }

    @PostConstruct
    protected void init() {
        accessTokenSecret = Base64.getEncoder().encodeToString(accessTokenSecret.getBytes());
        refreshTokenSecret = Base64.getEncoder().encodeToString(refreshTokenSecret.getBytes());
    }

    // Access Token 발급
    public String generateAccessToken(UserPrincipal userPrincipal) {
        return createToken(userPrincipal.getPrincipal(), TokenType.ACCESS_TOKEN);
    }

    // Refresh Token 발급
    public String generateRefreshToken(UserPrincipal userPrincipal) {
        return createToken(userPrincipal.getPrincipal(), TokenType.REFRESH_TOKEN);
    }

    /**
     * 인증된 유저의 authentication에서 userPrincipal을 추출해 token을 생성한다.
     */
    public String createToken(String email, TokenType tokenType) {

        String secretKey;
        long expireTime;

        if (tokenType == TokenType.ACCESS_TOKEN) {
            secretKey = accessTokenSecret;
            expireTime = accessTokenExpiredMsc;
        } else {
            secretKey = refreshTokenSecret;
            expireTime = refreshTokenExpiredMsc;
        }

        Claims claims = Jwts.claims().setSubject(email);
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
    public boolean validateToken(String token) {
        String secretKey = accessTokenSecret;
        try {
            log.debug("validateToken's secretKey : " + secretKey);
            // 1. setSigningKey를 통해 디지털 서명되었는지를 확인한다.
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            // 2. 만료일자가 지났는지 확인한다.
            boolean isNotExpire = !claims.getBody().getExpiration().before(new Date()); // 만료되면 false를 반환
            // 3. 블랙리스트인지 확인한다.
            if (redisTemplate.opsForValue().get(token) != null) { // 블랙리스트에 access token이 존재할 경우
                log.info("이미 로그아웃 처리된 사용자입니다.");
                return false;
            }
            return isNotExpire;
        } catch (Exception e) {
            return false;
        }
    }

    // Jwt 토큰에서 회원 이메일 추출
    public String getUserEmail(String token, TokenType tokenType) {
        String secretKey;
        if (tokenType == TokenType.ACCESS_TOKEN) {
            secretKey = accessTokenSecret;
        } else {
            secretKey = refreshTokenSecret;
        }
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Request의 Header에서 token 파싱
    public String extractToken(HttpServletRequest request) {
        return request.getHeader(HEADER_NAME);
    }

    public Date getExpirationDate(String token, TokenType tokenType) {
        String secretKey;

        if (tokenType == TokenType.ACCESS_TOKEN) {
            secretKey = accessTokenSecret;
        } else {
            secretKey = refreshTokenSecret;
        }

        log.debug("getExpirationDate's secretKey : " + secretKey);
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}