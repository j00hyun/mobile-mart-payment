package com.automart.security.jwt;

import com.automart.config.AppProperties;
import com.automart.security.UserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

// Json 웹 토큰을 생성하고 확인
@Service
public class JwtTokenProvider {

    private AppProperties appProperties;

    public JwtTokenProvider(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * 인증된 유저의 authentication에서 userPrincipal을 추출해 token을 생성한다.
     */
    public String createToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        return Jwts.builder()
                .setSubject(Integer.toString(userPrincipal.getNo()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    // Jwt 토큰 유효성검사
    public boolean validateToken(String jwtToken) {
        try {
            // 1. setSigningKey를 통해 디지털 서명되었는지를 확인한다.
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(appProperties.getAuth().getTokenSecret())
                    .parseClaimsJws(jwtToken);
            // 2. 만료일자가 지났는지 확인한다.
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // Jwt 토큰에서 회원 고유번호 추출
    public String getUserNo(String token) {
        return Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Request의 Header에서 token 파싱 : "JAuth_TOKEN: jwt토큰"
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("HEADER_NAME");
    }
}