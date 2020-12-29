package com.automart.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.automart.security.CustomUserDetailsService;
import com.automart.security.UserPrincipal;
import com.automart.user.domain.User;
import com.automart.user.dto.SignInRequestDto;
import com.automart.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 이렇게 상속해버리면 SecurityConfig 아래와 같이 걸지 않아도 된다.
 * http.addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
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

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        SignInRequestDto credentials = null;
        try {
            credentials = new ObjectMapper().registerModule(new ParameterNamesModule()).readValue(request.getInputStream(), SignInRequestDto.class);
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

//    @SuppressWarnings("사용하지 않음")
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // Grab principal
        UserPrincipal principal = (UserPrincipal)authentication.getPrincipal();
        String accessToken = tokenProvider.generateToken(principal);
        String refreshToken = tokenProvider.generateRefreshToken(principal);
        response.addHeader("Authorization", "Bearer " +  accessToken);
    }
}
