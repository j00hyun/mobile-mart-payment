package com.automart.admin.controller;

import com.automart.admin.domain.Admin;
import com.automart.admin.dto.AuthResponseDto;
import com.automart.admin.dto.AdminSignInRequestDto;
import com.automart.admin.service.AdminService;
import com.automart.advice.exception.NotFoundUserException;
import com.automart.advice.exception.SignInTypeErrorException;
import com.automart.security.UserPrincipal;
import com.automart.security.jwt.JwtTokenProvider;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Api(tags = {"4. Managing Admin Authentication "})
@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminSignController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @ApiOperation(value = "1 관리자 로그인", notes = "관리자 로그인을 시도한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 로그인 되었습니다.", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "아이디 또는 비밀번호가 일치하지 않습니다."),
            @ApiResponse(code = 406, message = "아이디에는 '@'가 포함될 수 없습니다.")
    })
    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> signIn(@ApiParam("로그인 정보") @Valid @RequestBody AdminSignInRequestDto requestDto) throws NotFoundUserException, SignInTypeErrorException {
        /* '@'포함여부, 관리자 일치여부 체크 */
        Admin admin = adminService.checkLogIn(requestDto.getId(), requestDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getId(),
                        requestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        /* access 토큰과 refresh 토큰을 발급 */
        String accessToken = jwtTokenProvider.generateAccessToken(userPrincipal);
        String refreshToken = jwtTokenProvider.generateRefreshToken(userPrincipal);

        /* refresh 토큰을 redis에 저장 */
        Date expirationDate = jwtTokenProvider.getExpirationDate(refreshToken, JwtTokenProvider.TokenType.REFRESH_TOKEN);
        redisTemplate.opsForValue().set(
                userPrincipal.getPrincipal(), refreshToken,
                expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
        log.info("redis value : " + redisTemplate.opsForValue().get(userPrincipal.getPrincipal()));

        return ResponseEntity.ok(new AuthResponseDto(accessToken));
    }
}
