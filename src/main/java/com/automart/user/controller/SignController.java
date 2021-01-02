package com.automart.user.controller;

import com.automart.security.UserPrincipal;
import com.automart.advice.exception.ForbiddenSignUpException;
import com.automart.advice.exception.NotFoundUserException;
import com.automart.advice.exception.SMSException;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import com.automart.user.dto.AuthResponseDto;
import com.automart.user.dto.SignInRequestDto;
import com.automart.user.dto.SignUpRequestDto;
import com.automart.user.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Api(tags = {"2. User"})
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


    @ApiOperation(value = "이메일 중복검사", notes = "1-3 초기화면에서 이메일 입력시 중복확인")
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


    @ApiOperation(value = "로컬 로그인", notes = "2-1 로컬 회원 로그인을 시도한다")
    /*@ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 로그인 되었습니다."),
            @ApiResponse(code = 403, message = "아이디 또는 비밀번호가 일치하지 않습니다.")
    })*/
    @PostMapping("/signin")
    public ResponseEntity<AuthResponseDto> signIn(@ApiParam("로그인 정보") @Valid @RequestBody SignInRequestDto requestDto) throws NotFoundUserException {
        userService.checkLogIn(requestDto.getEmail(), requestDto.getPassword());

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
        Date expirationDate = jwtTokenProvider.getExpirationDate(refreshToken);
        redisTemplate.opsForValue().set(
                userPrincipal.getEmail(), refreshToken,
                expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
        log.info("redis value : " + redisTemplate.opsForValue().get(userPrincipal.getEmail()));

        return ResponseEntity.ok(new AuthResponseDto(accessToken));
    }


    @ApiOperation(value = "핸드폰 본인인증", notes = "3-2 회원가입시 핸드폰 중복확인 후 본인인증 메세지를 전송한다.")
    @ApiImplicitParam(name = "phoneNo", value = "인증번호를 전송할 핸드폰번호", required = true, dataType = "string", defaultValue = "01012345678")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 500, message = "메세지 전송 실패"),
            @ApiResponse(code = 406, message = "동일한 휴대폰 번호의 회원이 이미 존재합니다.")
    })
    @PostMapping("/valid/phone")
    public ResponseEntity<Integer> validPhone(@RequestParam String phoneNo) throws SMSException, ForbiddenSignUpException {
        userService.checkDuplicateTel(phoneNo);
        int validNum = userService.validatePhone(phoneNo);
        return ResponseEntity.status(HttpStatus.OK).body(validNum);
    }


    @ApiOperation(value = "로컬 회원가입", notes = "3-2 로컬 회원가입을 한다")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 회원가입이 완료되었습니다."),
            @ApiResponse(code = 406, message = "회원가입에 실패하였습니다.")
    })
    @PostMapping("/signup")
    public ResponseEntity<Void> signUp(@ApiParam("가입 회원 정보") @Valid @RequestBody SignUpRequestDto requestDto) throws ForbiddenSignUpException{

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
}
