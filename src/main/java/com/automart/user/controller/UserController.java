package com.automart.user.controller;

import com.automart.advice.exception.InvalidTokenException;
import com.automart.advice.exception.SMSException;
import com.automart.user.dto.AuthResponseDto;
import com.automart.advice.exception.NotFoundUserException;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.security.UserPrincipal;
import com.automart.user.domain.User;
import com.automart.user.dto.FindRequestDto;
import com.automart.user.dto.UserResponseDto;
import com.automart.user.repository.UserRepository;
import com.automart.user.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
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
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    @ApiOperation(value = "7 이메일 찾기", notes = "해당 회원의 이메일 주소를 찾는다.")
    @ApiResponses({
            @ApiResponse(code = 302, message = "일치하는 회원이 존재합니다."),
            @ApiResponse(code = 403, message = "일치하는 회원이 존재하지 않습니다.")
    })
    @PostMapping("/find/email")
    public ResponseEntity<String> findEmail(@ApiParam("이름, 휴대폰번호 정보") @Valid @RequestBody FindRequestDto requestDto) throws NotFoundUserException {
        User user = userService.findUserByNameAndTel(requestDto.getName(), requestDto.getPhone());
        return ResponseEntity.status(HttpStatus.OK).body(user.getEmail());
    }


    @ApiOperation(value = "8 비밀번호 찾기 시 핸드폰 본인인증", notes = "비밀번호를 찾기 위해 본인인증 메세지를 전송한다.")
    @ApiImplicitParam(name = "phoneNo", value = "인증번호를 전송할 핸드폰번호", required = true, dataType = "string", defaultValue = "01012345678")
    @ApiResponses({
            @ApiResponse(code = 200, message = "전송된 인증번호 반환"),
            @ApiResponse(code = 500, message = "메세지 전송 실패"),
    })
    @PostMapping("/find/valid/phone")
    public ResponseEntity<Integer> validPhoneForFindPw(@RequestParam String phoneNo) throws SMSException {
        int validNum = userService.validatePhone(phoneNo);
        return ResponseEntity.status(HttpStatus.OK).body(validNum);
    }


    @ApiOperation(value = "8 새 비밀번호 발급", notes = "해당 회원이 존재하면 새 비밀번호를 문자로 발급한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "새 비밀번호 발급 후 전송 완료"),
            @ApiResponse(code = 500, message = "메세지 전송 실패"),
            @ApiResponse(code = 403, message = "일치하는 회원이 존재하지 않습니다.")
    })
    @PostMapping("/find/reissue/password")
    public ResponseEntity<Void> findPassword(@ApiParam("이름, 휴대폰번호 정보") @Valid @RequestBody FindRequestDto requestDto) throws NotFoundUserException, SMSException {
        User user = userService.findUserByNameAndTel(requestDto.getName(), requestDto.getPhone());
        String newPassword = userService.generateTempPw(requestDto.getPhone());
        userService.changePassword(user.getNo(), newPassword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


    @ApiOperation(value = "내정보 조회", notes = "현재 인증된 유저의 정보를 가져온다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 내정보가 불러와졌습니다."),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class)
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return userRepository.findByNo(userPrincipal.getNo())
                .orElseThrow(() -> new NotFoundUserException("해당 유저를 찾을 수 없습니다."));
    }

    @ApiOperation(value = "(어드민)이메일로 회원 조회", notes = "(어드민)이메일로 회원을 조회한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "email", value = "조회할 회원의 이메일주소", required = true, dataType = "string", defaultValue = "example@google.com")
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 해당 회원정보가 조회되었습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/byEmail")
    public ResponseEntity<UserResponseDto> showUser(@ApiIgnore @RequestHeader("Authorization") String token,
                                                    @RequestParam String email) {
        UserResponseDto userResponseDto = userService.showUser(email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @ApiOperation(value = "(어드민)회원 전체 조회", notes = "(어드민)회원목록을 조회한다", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원목록을 조회하는데 성공했습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/list")
    public ResponseEntity<List<UserResponseDto>> showUsers(@ApiIgnore @RequestHeader("Authorization") String token) {
        List<UserResponseDto> userResponseDtos = userService.showUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDtos);
    }

    @ApiOperation(value = "(어드민)회원 탈퇴", notes = "(어드민)특정 회원을 삭제한다", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원이 정상적으로 탈퇴되었습니다.")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/withdraw")
    public ResponseEntity<String> withdraw(@ApiIgnore @RequestHeader("Authorization") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.withdraw(token, JwtTokenProvider.TokenType.ACCESS_TOKEN));
    }

    @ApiOperation(value = "비밀번호 변경 전 확인", notes = "회원이 비밀번호 변경 전 비밀번호가 일치하는지 확인한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "userPassword", value = "해당 회원의 현재 비밀번호", required = true, dataType = "string", defaultValue = "oldpassword")
    @ApiResponses({
            @ApiResponse(code = 200, message = "토큰에 해당하는 유저가 존재합니다.\n(비밀번호가 일치하면 true, 일치하지 않으면 false를 반환)"),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class)
    })

    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/varifyPassword")
    public ResponseEntity<Boolean> verifyPassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                  @RequestParam String userPassword) {
        User user = getCurrentUser(userPrincipal);

        if (!passwordEncoder.matches(userPassword, user.getPassword()))
            return ResponseEntity.status(HttpStatus.OK).body(false);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @ApiOperation(value = "비밀번호 변경", notes = "회원의 비밀번호를 변경한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "userPassword", value = "변경할 새로운 비밀번호", required = true, dataType = "string", defaultValue = "newpassword")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공적으로 비밀번호가 변경되었습니다. \n변경된 비밀번호로 다시 로그인해주세요. \nPOST: /logout이 이후에 필요"),
            @ApiResponse(code = 401, message = "토큰 만료", response = AuthResponseDto.class)
    })
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(value = "/changePassword")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @RequestParam String userPassword) {
        userService.changePassword(userPrincipal.getNo(), userPassword);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


//    // access 토큰이 만료되었으나 refresh 토큰이 살아있는 경우 재발급(그러나 필터통과를 못함(?)..)
//    @ApiOperation(value = "토큰 만료", notes = "refresh 토큰을 이용하여 access 토큰을 재생성한다.", authorizations = { @Authorization(value = "jwtToken")})
//    @PostMapping(value = "/check")
//    public ResponseEntity<?> checkForGenerate(@AuthenticationPrincipal UserPrincipal userPrincipal,
//                                              HttpServletRequest request) {
//        String accessToken = jwtTokenProvider.extractToken(request);
//
//        if (!jwtTokenProvider.validateToken(accessToken)){
//            String userEmail = userPrincipal.getEmail();
//            if (redisTemplate.opsForValue().get(userEmail) != null) {
//                String newAccessToken = jwtTokenProvider.generateAccessToken(userPrincipal);
//                return ResponseEntity.ok(new AuthResponseDto(newAccessToken));
//            }
//        }
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
}
