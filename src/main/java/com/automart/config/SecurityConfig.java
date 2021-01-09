package com.automart.config;

import com.automart.security.UserPrincipal;
import com.automart.security.jwt.*;
import com.automart.security.oauth2.CustomOAuth2UserService;
import com.automart.user.repository.UserRepository;
import com.automart.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
/**
 * 설정하는 순간 스프링 부트가 제공하는 스프링 시큐리티 자동설정은 더이상 제공되지 않음
 * 하지만 WebSecurityConfigurerAdapter 클래스를 상속 받는 순간 모든 요청은 인증을 필요로 하게됨
 */
//@EnableGlobalMethodSecurity( // 특정 메서드에 권한 처리를 하는 MethodSecurity 설정 기능 제공
//        securedEnabled = true, // @Secured 사용하여 인가처리하는 옵션
//        jsr250Enabled = true, // @RolesAllowed 사용하여 인가처리 옵션
//        prePostEnabled = true // @PreAuthorize, @PostAuthorize 사용하여 인가처리 옵션
//)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 인증시 사용할 custom User Service
    private UserService userService;

    private CustomOAuth2UserService customOAuth2UserService;

    private UserRepository userRepository;

    private JwtTokenProvider tokenProvider;

    private RedisTemplate<String, Object> redisTemplate;

    public SecurityConfig(@Lazy UserService userService, CustomOAuth2UserService customOAuth2UserService, UserRepository userRepository, JwtTokenProvider tokenProvider, RedisTemplate<String, Object> redisTemplate) {
	this.userService = userService;
	this.customOAuth2UserService = customOAuth2UserService;
	this.userRepository = userRepository;
	this.tokenProvider = tokenProvider;
	this.redisTemplate = redisTemplate;
    }

    /*
     * 다른 AuthorizationServer나 ResourceServer가 참조할 수 있도록 오버라이딩 해서 빈으로 등록
     */
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Authorization에 사용할 userDetailService와 password Encoder를 정의한다.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    /*
     * HTTP로 거르며, 스프링 시큐리티 필터를 통해 모든 필터링을 서버가 처리
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors() // cors 허용
                    .and()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은 필요없으니 생성안함
                    .and()
                .csrf().disable() // rest api이므로 csrf 보안이 필요없으므로 disable 처리
                .httpBasic().disable() // rest api이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .formLogin().disable()
                .addFilterBefore(new JwtBasicAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilter(new JwtCommonAuthorizationFilter(authenticationManager(), tokenProvider, userRepository, redisTemplate))
                .authorizeRequests() // 이후 요청에 대한 사용권한 체크
                    .antMatchers("/", "/*/signin", "/*/signup", "/*/valid/**", "/*/find/**", "/oauth2/**", "/login**", "/logout**", "/error**").permitAll() // 가입 및 인증 주소는 누구나 접근가능
//                        .anyRequest().hasRole("USER") // 그외 나머지 요청은 모두 인증된 회원만 접근가능 (모든 컨트롤러 작동 여부 확인 뒤 주석 해제하고 다시 테스트 할 것!)
                    .anyRequest().authenticated()
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // access 토큰 만료 예외 처리
                    .and()
                // oauth2 login 설정
                .oauth2Login()
                    .userInfoEndpoint() // 로그인시 사용할 User Service를 정의
                        .userService(customOAuth2UserService)
                        .and()
                    .successHandler(new AuthenticationSuccessHandler() {

                        // 로그인 성공시 invoke할 Handler를 정의
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                            String accessToken = tokenProvider.generateAccessToken(userPrincipal);
                            String refreshToken = tokenProvider.generateRefreshToken(userPrincipal); // redis에 담아야함
                            response.addHeader("Authorization", "Bearer " +  accessToken);
                            String targetUrl = "/"; // 로그인 후 이동할 주소
                            RequestDispatcher dis = request.getRequestDispatcher(targetUrl);
                            dis.forward(request, response);
                        }
                    })

                    .failureHandler(new AuthenticationFailureHandler() {

                        // 로그인 실패시 invoke할 Handler를 정의
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        }
                    });
    }

    /*
     * HTTP를 적용하기 전에 시큐리티 필터를 적용할지 말지를 먼저 결정
     * 서버가 일을 조금이라도 덜하게 하기 위해 정적 리소스들은 웹 필터로 걸러주는것을 권장
     * PathRequest.toStaticResources() : 스프링부트가 제공하는 정적리소스들의 기본 위치를 다 가져와서 시큐리티에서 제외
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
        web.ignoring().mvcMatchers("/docs/index.html");
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    /*
     * 다양한 인코딩 타입을 지원하며 어떠한 방식으로 인코딩된 건지 알 수 있도록 패스워드 앞에 prefix를 붙여줌
     * prefix값에 따라 적절한 인코더를 적용해서 패스워드 값이 매칭되는지 확인
     * prefix값에 따라 적절한 인코더를 적용해서 패스워드 값이 매칭되는지 확인
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
