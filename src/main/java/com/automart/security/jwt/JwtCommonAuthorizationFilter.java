package com.automart.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.automart.security.UserPrincipal;
import com.automart.user.domain.User;
import com.automart.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * HTTP 기본 인증 헤더를 처리하여 결과를 SecurityContextHolder에 저장한다.
 */
public class JwtCommonAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;
    private RedisTemplate<String, Object> redisTemplate;


    public JwtCommonAuthorizationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, RedisTemplate<String, Object> redisTemplate) {
        super(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
    }

    /**
     * request에서 Header(jwtToken)을 획득 후, 해당 유저를 DB에서 찾아 인증을 진행한다. (Authentication 생성)
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // JWT token이 어디있는지 알기 위해, Authorization header를 찾는다.
        String header = request.getHeader("Authorization");

        // 만약 header에 Bearer가 포함되어 있지 않거나 header가 null이라면 작업을 끝낸다.
        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        // 만약 header가 존재한다면, DB로 부터 user의 권한을 확인하고, authorization을 수행한다.
        Authentication authentication = getUsernamePasswordAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // filter 수행을 계속한다.
        chain.doFilter(request, response);
    }

    /**
     * 헤더의 jwtToken 내부에 있는 정보를 통해 DB와 일치하는 유저를 찾아 인증이 완료한다. (UserPrincipal 객체 생성)
     */
    private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization")
                .replace("Bearer", "");

        // 만약 token이 존재한다면, token을 통해 user의 이메일을 얻는다.
        if (token != null) {
            String userEmail = jwtTokenProvider.getUserEmail(token, JwtTokenProvider.TokenType.ACCESS_TOKEN); // users의 email값
            if (jwtTokenProvider.validateToken(token)) { // 유효한 토큰이라면
                // 만약 user의 이메일이 존재한다면, DB에서 해당 유저를 찾고 권한을 부여한다.
                if (userEmail != null) {

                    Optional<User> oUser = userRepository.findByEmail(userEmail);
                    User user = oUser.get();
                    UserPrincipal principal = UserPrincipal.create(user);

                    // OAuth 인지 일반 로그인인지 구분할 필요가 없음. 왜냐하면 password를 Authentication이 가질 필요가 없으니!!
                    // JWT가 로그인 프로세스를 가로채서 인증다 해버림. (OAuth2.0이든 그냥 일반 로그인 이든)

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    return authentication;
                }
            }
//            else {
//                if (redisTemplate.opsForValue().get(userEmail) != null) { // refresh토큰만 살아있는 경우(access토큰이 기간만 만료된것일 때)
//                    // access token을 발급해준다.
//                }
//            }
        }
        return null;
    }
}

