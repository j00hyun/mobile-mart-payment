package com.automart.user.controller;


import com.automart.user.dto.*;
import com.automart.user.service.AdminService;
import com.automart.advice.exception.*;
import com.automart.security.UserPrincipal;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.user.domain.Admin;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Api(tags = {"1. Managing User Authentication "})
@Validated
@RestController
@Slf4j
public class UserSignController {

    @Autowired
    private AdminService adminService;

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
            @ApiResponse(code = 403, message = "동일한 이메일의 회원이 이미 존재합니다.")
    })
    @GetMapping("/users/signup")
    public ResponseEntity<Void> validEmail(@RequestParam @Email(message = "이메일 양식을 지켜주세요.") String email) throws DuplicateDataException {
        userService.checkDuplicateEmail(email);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiOperation(value = "2-1 로컬 로그인", notes = "로컬 회원 로그인을 시도한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 로그인 되었습니다.", response = AuthResponseDto.class),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "아이디 또는 비밀번호가 일치하지 않습니다."),
            @ApiResponse(code = 428, message = "비밀번호를 변경해야 합니다.", response = AuthResponseDto.class)
    })
    @PostMapping("/users/signin")
    public ResponseEntity<AuthResponseDto> signIn(@ApiParam("로그인 정보") @Valid @RequestBody UserSignInRequestDto requestDto) throws SessionUnstableException {

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
                userPrincipal.getPrincipal(), refreshToken,
                expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
        log.info("redis value : " + redisTemplate.opsForValue().get(userPrincipal.getPrincipal()));

        /* 임시 비밀번호 여부 체크 */
        if(user.isTempPassword()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_REQUIRED).body(new AuthResponseDto(accessToken));
        }

        return ResponseEntity.ok(new AuthResponseDto(accessToken));
    }


    @ApiOperation(value = "3-2 회원가입시 핸드폰 본인인증", notes = "회원가입시 핸드폰 중복확인 후 본인인증 메세지를 전송한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 403, message = "동일한 휴대폰 번호의 회원이 이미 존재합니다."),
            @ApiResponse(code = 500, message = "메세지 전송 실패")
    })
    @PostMapping("/users/signup/message")
    public ResponseEntity<Integer> validPhoneForSignUp(@ApiParam("휴대폰 번호") @Valid @RequestBody SendMessageRequestDto requestDto) throws SMSException, DuplicateDataException {
        userService.checkDuplicateTel(requestDto.getPhoneNo());
        int validNum = userService.validatePhone(requestDto.getPhoneNo());
        return ResponseEntity.status(HttpStatus.OK).body(validNum);
    }


    @ApiOperation(value = "3-2 로컬 회원가입", notes = "로컬 회원가입을 한다")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 회원가입이 완료되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 403, message = "이메일 또는 핸드폰번호가 중복되어 회원가입에 실패하였습니다.")
    })
    @PostMapping("/users/signup")
    public ResponseEntity<Void> signUp(@ApiParam("가입 회원 정보") @Valid @RequestBody UserSignUpRequestDto requestDto) throws DuplicateDataException{

        userService.checkDuplicateEmail(requestDto.getEmail());
        userService.checkDuplicateTel(requestDto.getTel());

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .tel(requestDto.getTel())
                .name(requestDto.getName())
                .snsType(AuthProvider.local)
                .build();
        userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @ApiOperation(value = "(개발용) 관리자 회원가입", notes = "개발자가 관리자를 추가하는데 사용, Front와 연결 X")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 회원가입이 완료되었습니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 403, message = "아이디가 중복되어 회원가입에 실패하였습니다.")
    })
    @PostMapping("/admins/signup")
    public ResponseEntity<Void> adminSignUp(@ApiParam("가입 회원 정보") @Valid @RequestBody AdminSignUpRequestDto requestDto) throws DuplicateDataException {

        adminService.checkDuplicateId(requestDto.getId());

        Admin admin = Admin.builder()
                .id(requestDto.getId())
                .password(requestDto.getPassword())
                .build();

        adminService.saveAdmin(admin);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @ApiOperation(value = "1 관리자 로그인", notes = "관리자 로그인을 시도한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 로그인 되었습니다.", response = AuthResponseDto.class),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "아이디 또는 비밀번호가 일치하지 않습니다."),
    })
    @PostMapping("/admins/signin")
    public ResponseEntity<AuthResponseDto> adminSignIn(@ApiParam("로그인 정보") @Valid @RequestBody AdminSignInRequestDto requestDto) throws SessionUnstableException {

        adminService.checkLogIn(requestDto.getId(), requestDto.getPassword());

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
