package com.automart.user.controller;

import com.automart.advice.exception.*;
import com.automart.security.UserPrincipal;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import com.automart.user.dto.AuthResponseDto;
import com.automart.user.dto.FindRequestDto;
import com.automart.user.dto.SignInRequestDto;
import com.automart.user.dto.SignUpRequestDto;
import com.automart.user.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Api(tags = {"1. Managing User Authentication "})
@RestController
@Slf4j
@RequestMapping("/users")
public class SignController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @ApiOperation(value = "1-3 이메일 중복검사", notes = "초기화면에서 이메일 입력시 중복확인")
    @ApiImplicitParam(name = "email", value = "중복확인을 진행할 이메일주소", required = true, dataType = "string", defaultValue = "example@google.com")
    @ApiResponses({
            @ApiResponse(code = 200, message = "이메일이 중복되지 않습니다."),
            @ApiResponse(code = 406, message = "동일한 이메일의 회원이 이미 존재합니다.")
    })
    @PostMapping("/valid/email")
    public ResponseEntity<Void> validEmail(@RequestParam String email) throws ForbiddenSignUpException {
        userService.checkDuplicateEmail(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiOperation(value = "2-1 로컬 로그인", notes = "로컬 회원 로그인을 시도한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 로그인 되었습니다.", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "아이디 또는 비밀번호가 일치하지 않습니다."),
            @ApiResponse(code = 428, message = "비밀번호를 변경해야 합니다.", response = AuthResponseDto.class)
    })
    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> signIn(@ApiParam("로그인 정보") @Valid @RequestBody SignInRequestDto requestDto) throws NotFoundUserException {
        User user = userService.checkLogIn(requestDto.getEmail(), requestDto.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
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
                userPrincipal.getEmail(), refreshToken,
                expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
        log.info("redis value : " + redisTemplate.opsForValue().get(userPrincipal.getEmail()));

        /* 임시 비밀번호 여부 체크 */
        if(user.isTempPassword()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(new AuthResponseDto(accessToken));
        }

        return ResponseEntity.ok(new AuthResponseDto(accessToken));
    }


    @ApiOperation(value = "3-2 회원가입시 핸드폰 본인인증", notes = "회원가입시 핸드폰 중복확인 후 본인인증 메세지를 전송한다.")
    @ApiImplicitParam(name = "phoneNo", value = "인증번호를 전송할 핸드폰번호", required = true, dataType = "string", defaultValue = "01012345678")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 500, message = "메세지 전송 실패"),
            @ApiResponse(code = 406, message = "동일한 휴대폰 번호의 회원이 이미 존재합니다.")
    })
    @PostMapping("/valid/phone")
    public ResponseEntity<Integer> validPhoneForSignUp(@RequestParam String phoneNo) throws SMSException, ForbiddenSignUpException {
        userService.checkDuplicateTel(phoneNo);
        int validNum = userService.validatePhone(phoneNo);
        return ResponseEntity.status(HttpStatus.OK).body(validNum);
    }


    @ApiOperation(value = "3-2 로컬 회원가입", notes = "로컬 회원가입을 한다")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 회원가입이 완료되었습니다."),
            @ApiResponse(code = 406, message = "이메일 또는 핸드폰번호가 중복되어 회원가입에 실패하였습니다.")
    })
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@ApiParam("가입 회원 정보") @Valid @RequestBody SignUpRequestDto requestDto) throws ForbiddenSignUpException{
        userService.checkDuplicateEmail(requestDto.getEmail());
        userService.checkDuplicateTel(requestDto.getTel());

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .tel(requestDto.getTel())
                .name(requestDto.getName())
                .snsType(AuthProvider.local)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @ApiOperation(value = "8 로그인 후 비밀번호 변경", notes = "로그인 후 임시비밀번호를 변경한다.", authorizations = {@Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "newPassword", value = "해당 회원의 새 비밀번호", required = true, dataType = "string", defaultValue = "newpassword")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 비밀번호를 변경 후, 새로운 토큰을 발급한다."),
            @ApiResponse(code = 401, message = "토큰 만료로 인해 비밀번호 변경 불가 -> 새로운 토큰 발급", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "일치하는 회원이 존재하지 않아 비밀번호 변경에 실패하였습니다.")
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/change/password")
    public ResponseEntity<AuthResponseDto> changePassword(@ApiIgnore @RequestHeader("Authorization") String token,
                                                          @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @RequestParam String newPassword) throws NotFoundUserException {
        /* password 변경 */
        User user = userService.changePassword(userPrincipal.getNo(), newPassword);
        user.makeFalseTempPw();

        /* access token이 유효한 토큰인 경우 더 이상 사용하지 못하게 블랙리스트에 등록 */
        if (jwtTokenProvider.validateToken(token)) {
            Date expirationDate = jwtTokenProvider.getExpirationDate(token, JwtTokenProvider.TokenType.ACCESS_TOKEN);
            redisTemplate.opsForValue().set(
                    token, true,
                    expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
            log.info("redis value : " + redisTemplate.opsForValue().get(token));
        }

        /* 새로운 access 토큰 발급 */
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), newPassword)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal newUserPrincipal = (UserPrincipal) authentication.getPrincipal();
        String newAccessToken = jwtTokenProvider.generateAccessToken(newUserPrincipal);

        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDto(newAccessToken));
    }




    /*@ApiOperation(value = "로그아웃", notes = "로그인된 계정을 로그아웃한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponse(code = 200, message = "로그아웃 되었습니다.")
    //@PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       HttpServletRequest request) {
        String accessToken = jwtTokenProvider.extractToken(request);
        String userEmail = null;

        *//* access token을 통해 userEmail을 찾아 redis에 저장된 refresh token을 삭제한다.*//*
        try {
            userEmail = userPrincipal.getEmail();
        } catch (InvalidTokenException e) {
            log.error("userEmail이 유효한 토큰에 존재하지 않음.");
        }

        try {
            if (redisTemplate.opsForValue().get(userEmail) != null) {
                redisTemplate.delete(userEmail);
            }
        } catch (IllegalArgumentException e) {
            redisTemplate.delete(userEmail);
        }

        *//* access token이 유효한 토큰인 경우 더 이상 사용하지 못하게 블랙리스트에 등록 *//*
        if (jwtTokenProvider.validateToken(accessToken)) {
            Date expirationDate = jwtTokenProvider.getExpirationDate(accessToken, JwtTokenProvider.TokenType.ACCESS_TOKEN);
            redisTemplate.opsForValue().set(
                    accessToken, true,
                    expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
            log.info("redis value : " + redisTemplate.opsForValue().get(accessToken));
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }*/
}
