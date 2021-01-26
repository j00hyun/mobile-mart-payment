package com.automart.config;

import com.automart.security.CustomUserDetailsService;
import com.automart.security.UserPrincipal;
import com.automart.security.jwt.*;
import com.automart.security.oauth2.CustomOAuth2UserService;
import com.automart.user.repository.AdminRepository;
import com.automart.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 인증시 사용할 custom User Service
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    /*
     * HTTP로 거르며, 스프링 시큐리티 필터를 통해 모든 필터링을 서버가 처리
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 로그인 성공시 invoke할 Handler를 정의
        // 로그인 실패시 invoke할 Handler를 정의
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
                .addFilter(new JwtCommonAuthorizationFilter(authenticationManager(), tokenProvider, userRepository, adminRepository, redisTemplate))
                .authorizeRequests() // 이후 요청에 대한 사용권한 체크
                    .antMatchers("/", "/*/signin", "/*/signup", "/*/find/**", "/oauth2/**", "/login**", "/logout**", "/error**").permitAll() // 가입 및 인증 주소는 누구나 접근가능
                    .antMatchers("/graphql").hasRole("ADMIN")
                    .anyRequest().authenticated() // 그 외의 모든 요청은 인증된 사용자만 접근 가능
                    .and()
                .exceptionHandling()
                    .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) // access 토큰 만료 예외 처리
                    .and()
                // oauth2 login 설정
                .oauth2Login()
                    .userInfoEndpoint() // 로그인시 사용할 User Service를 정의
                        .userService(customOAuth2UserService)
                        .and()
                    .successHandler((request, response, authentication) -> {

                        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                        String accessToken = tokenProvider.generateAccessToken(userPrincipal);
                        String refreshToken = tokenProvider.generateRefreshToken(userPrincipal); // redis에 담아야함
                        response.addHeader("Authorization", "Bearer " +  accessToken);
                        response.setStatus(HttpServletResponse.SC_OK);
//                            String targetUrl = "/"; // 로그인 후 이동할 주소
//                            RequestDispatcher dis = request.getRequestDispatcher(targetUrl);
//                            dis.forward(request, response);
                    })

                    .failureHandler((request, response, exception) -> {

                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
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
