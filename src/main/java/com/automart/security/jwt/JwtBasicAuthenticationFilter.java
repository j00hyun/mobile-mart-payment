package com.automart.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.automart.security.CustomUserDetailsService;
import com.automart.user.domain.User;
import com.automart.user.dto.SignInRequestDto;
import com.automart.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 로그인 인증 과정을 진행한다.
 */
public class JwtBasicAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * id/pw를 통해 UsernamePasswordAuthenticationToken을 생성한 후,
     * AuthenticationManager가 AuthenticationProvider를 순회하며 인증 가능 여부를 체크한다.
     *      - 이때, AuthenticationProvider는 UserDetailService를 통해 사용자 정보 DB에서 조회
     * 인증이 완료되면 해당 authentication 객체를 SecurityContextHolder에 저장한 후 반환한다.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        SignInRequestDto credentials = null;
        try {
            credentials = new ObjectMapper().readValue(request.getInputStream(), SignInRequestDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Optional<User> oUser = userRepository.findByEmail(credentials.getEmail());
        User user = oUser.get();
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        // Create login token
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        credentials.getPassword());

        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication); // 세션에 넣기
        return authentication;
    }

    /**
     * 인증이 성공하면 JWT를 발급해준다.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // Grab principal
        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication); // redis에 담아야함
        response.addHeader("Authorization", "Bearer " +  accessToken);
    }
}
