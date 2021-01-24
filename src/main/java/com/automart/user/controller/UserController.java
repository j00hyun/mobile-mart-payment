package com.automart.user.controller;

import com.automart.advice.exception.InvalidTokenException;
import com.automart.advice.exception.NotFoundDataException;
import com.automart.advice.exception.SMSException;
import com.automart.advice.exception.SessionUnstableException;
import com.automart.security.UserPrincipal;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.user.domain.User;
import com.automart.user.dto.*;
import com.automart.user.repository.AdminRepository;
import com.automart.user.repository.UserRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Api(tags = {"2. User Management"})
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @ApiOperation(value = "7 이메일 찾기", notes = "해당 회원의 이메일 주소를 찾는다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "일치하는 회원이 존재합니다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 404, message = "일치하는 회원이 존재하지 않습니다.")
    })
    @PostMapping("/find/email")
    public ResponseEntity<String> findEmail(@ApiParam("이름, 휴대폰번호 정보") @Valid @RequestBody FindRequestDto requestDto) throws NotFoundDataException {
        User user = userService.findUserByNameAndTel(requestDto.getName(), requestDto.getPhone());
        return ResponseEntity.status(HttpStatus.OK).body(user.getEmail());
    }


    @ApiOperation(value = "8 비밀번호 찾기 시 핸드폰 본인인증", notes = "비밀번호를 찾기 위해 본인인증 메세지를 전송한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 500, message = "메세지 전송 실패"),
    })
    @PostMapping("/find/password/message")
    public ResponseEntity<Integer> sendPhoneForFindPw(@ApiParam("휴대폰 번호") @Valid @RequestBody SendMessageRequestDto requestDto) throws SMSException {
        int validNum = userService.validatePhone(requestDto.getPhoneNo());
        return ResponseEntity.status(HttpStatus.OK).body(validNum);
    }


    @ApiOperation(value = "8 임시 비밀번호 발급", notes = "해당 회원이 존재하면 새 비밀번호를 문자로 발급한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "새 비밀번호 발급 후 전송 완료"),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 404, message = "일치하는 회원이 존재하지 않습니다."),
            @ApiResponse(code = 500, message = "메세지 전송 실패")
    })
    @PostMapping("/find/password")
    public ResponseEntity<Void> findPassword(@ApiParam("이름, 휴대폰번호 정보") @Valid @RequestBody FindRequestDto requestDto) throws NotFoundDataException, SMSException {
        User user = userService.findUserByNameAndTel(requestDto.getName(), requestDto.getPhone());
        String newPassword = userService.generateTempPw(requestDto.getPhone());
        userService.changePassword(user.getNo(), newPassword);
        user.makeTrueTempPw();
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiOperation(value = "(개발용) 내정보 조회", notes = "현재 인증된 유저의 정보를 가져온다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 내정보가 불러와졌습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal.getPrincipal().contains("@")) {
            UserResponseDto userResponseDto = UserResponseDto.of(userRepository.findByNo(userPrincipal.getNo())
                    .orElseThrow(() -> new SessionUnstableException("해당 유저를 찾을 수 없습니다.")));
            return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);

        } else {
            AdminResponseDto adminResponseDto = AdminResponseDto.of(adminRepository.findByNo(userPrincipal.getNo())
                    .orElseThrow(() -> new SessionUnstableException("해당 유저를 찾을 수 없습니다.")));
            return ResponseEntity.status(HttpStatus.OK).body(adminResponseDto);
        }
    }

    @ApiOperation(value = "(개발용) 이메일로 회원 조회", notes = "이메일로 회원을 조회한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "email", value = "조회할 회원의 이메일주소", required = true, dataType = "string", defaultValue = "example@google.com")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 해당 회원정보가 조회되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "관리자만 접근 가능"),
            @ApiResponse(code = 404, message = "일치하는 회원이 존재하지 않습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity<UserResponseDto> showUser(@ApiIgnore @RequestHeader("Authorization") String token,
                                                    @RequestParam @Email String email) throws NotFoundDataException {
        UserResponseDto userResponseDto = userService.showUser(email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @ApiOperation(value = "(개발용) 회원 전체 조회", notes = "회원목록을 조회한다", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원목록을 조회하는데 성공했습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "관리자만 접근 가능")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/all")
    public ResponseEntity<List<UserResponseDto>> showUsers(@ApiIgnore @RequestHeader("Authorization") String token) {
        List<UserResponseDto> userResponseDtos = userService.showUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDtos);
    }


    @ApiOperation(value = "(개발용) 회원 탈퇴", notes = "특정 회원을 삭제한다", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원이 정상적으로 탈퇴되었습니다."),
            @ApiResponse(code = 401, message = "1. 해당하는 회원을 찾을 수 없습니다.\n" +
                                                "2. 로그인이 필요합니다.\n" +
                                                "3. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @DeleteMapping(value = "/me")
    public ResponseEntity<String> withdraw(@ApiIgnore @RequestHeader("Authorization") String token) throws SessionUnstableException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.withdraw(token, JwtTokenProvider.TokenType.ACCESS_TOKEN));
    }


    @ApiOperation(value = "로그아웃", notes = "로그인된 계정을 로그아웃한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그아웃 되었습니다."),
            @ApiResponse(code = 401, message = "1. 로그인이 필요합니다.\n" +
                                                "2. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping(value = "/me/logout")
    public ResponseEntity<Void> logout(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       HttpServletRequest request) {
        String accessToken = jwtTokenProvider.extractToken(request);
        log.info("accessToken : " + accessToken);
        String principal = null;

        //* access token을 통해 userEmail or adminId를 찾아 redis에 저장된 refresh token을 삭제한다.*//
        try {
            principal = userPrincipal.getPrincipal();
        } catch (InvalidTokenException e) {
            log.error("userEmail or adminId가 유효한 토큰에 존재하지 않음.");
        }

        try {
            if (redisTemplate.opsForValue().get(principal) != null) {
                redisTemplate.delete(principal);
            }
        } catch (IllegalArgumentException e) {
            redisTemplate.delete(principal);
        }

        //* access token이 유효한 토큰인 경우 더 이상 사용하지 못하게 블랙리스트에 등록 *//
        if (jwtTokenProvider.validateToken(accessToken)) {
            Date expirationDate = jwtTokenProvider.getExpirationDate(accessToken, JwtTokenProvider.TokenType.ACCESS_TOKEN);
            redisTemplate.opsForValue().set(
                    accessToken, true,
                    expirationDate.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS); // 토큰의 유효기간이 지나면 자동 삭제
            log.info("redis value : " + redisTemplate.opsForValue().get(accessToken));
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @ApiOperation(value = "비밀번호 변경 전 확인", notes = "회원이 비밀번호 변경 전 비밀번호가 일치하는지 확인한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "userPassword", value = "해당 회원의 현재 비밀번호", required = true, dataType = "string", defaultValue = "oldpassword")
    @ApiResponses({
            @ApiResponse(code = 200, message = "토큰에 해당하는 유저가 존재합니다.\n(비밀번호가 일치하면 true, 일치하지 않으면 false를 반환)"),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class)
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/me/valid")
    public ResponseEntity<Boolean> verifyPassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @RequestBody VerifyPasswordRequestDto verifyPasswordRequestDto) throws SessionUnstableException{
        User user = userRepository.findByNo(userPrincipal.getNo())
                .orElseThrow(() -> new SessionUnstableException("해당 유저를 찾을 수 없습니다."));

        if (passwordEncoder.matches(verifyPasswordRequestDto.getPassword(), user.getPassword()))
            return ResponseEntity.status(HttpStatus.OK).body(true);

        return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    @ApiOperation(value = "8 로그인 후 비밀번호 변경", notes = "로그인 후 임시비밀번호를 변경한다.", authorizations = {@Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "newPassword", value = "해당 회원의 새 비밀번호", required = true, dataType = "string", defaultValue = "newpassword")
    @ApiResponses({
            @ApiResponse(code = 201, message = "정상적으로 비밀번호를 변경 후, 새로운 토큰을 발급한다."),
            @ApiResponse(code = 400, message = "유효한 입력값이 아닙니다."),
            @ApiResponse(code = 401, message = "1. 일치하는 회원이 존재하지 않아 비밀번호 변경에 실패하였습니다.\n" +
                                                "2. 로그인이 필요합니다.\n" +
                                                "3. 토큰 만료 (새로운 토큰 발급)", response = AuthResponseDto.class),
            @ApiResponse(code = 403, message = "유저만 접근 가능")
    })
    @PreAuthorize("hasRole('USER')")
    @PutMapping(value = "/me/password")
    public ResponseEntity<AuthResponseDto> changePassword(@ApiIgnore @RequestHeader("Authorization") String token,
                                                          @AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @ApiParam("새 비밀번호") @Valid @RequestBody ChangePasswordRequestDto requestDto) throws SessionUnstableException {
        /* password 변경 */
        User user = userService.changePassword(userPrincipal.getNo(), requestDto.getPassword());
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
                new UsernamePasswordAuthenticationToken(user.getEmail(), requestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal newUserPrincipal = (UserPrincipal) authentication.getPrincipal();
        String newAccessToken = jwtTokenProvider.generateAccessToken(newUserPrincipal);

        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponseDto(newAccessToken));
    }

}
