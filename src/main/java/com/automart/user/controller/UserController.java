package com.automart.user.controller;

import com.automart.exception.ForbiddenSignUpException;
import com.automart.exception.NotFoundUserException;
import com.automart.exception.SMSException;
import com.automart.exception.SigninFailedException;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.security.UserPrincipal;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import com.automart.user.dto.AuthResponseDto;
import com.automart.user.dto.SignInRequestDto;
import com.automart.user.dto.SignUpRequestDto;
import com.automart.user.dto.UserResponseDto;
import com.automart.user.repository.UserRepository;
import com.automart.user.service.UserService;
import io.swagger.annotations.*;
import org.apache.http.HttpResponse;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;
import java.util.*;

@Api(tags = {"2. User"})
@RestController
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


    @ApiOperation(value = "내정보 조회", notes = "현재 인증된 유저의 정보를 가져온다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiResponses({
            @ApiResponse(code = 200, message = "정상적으로 내정보가 불러와졌습니다.")
    })
    @PreAuthorize("hasRole('USER')")
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
        return ResponseEntity.status(HttpStatus.OK).body(userService.withdraw(token));
    }

    @ApiOperation(value = "비밀번호 변경 전 확인", notes = "회원이 비밀번호 변경 전 비밀번호가 일치하는지 확인한다.", authorizations = { @Authorization(value = "jwtToken")})
    @ApiImplicitParam(name = "userPassword", value = "해당 회원의 현재 비밀번호", required = true, dataType = "string", defaultValue = "oldpassword")
    @ApiResponses({
            @ApiResponse(code = 200, message = "토큰에 해당하는 유저가 존재합니다.\n(비밀번호가 일치하면 true, 일치하지 않으면 false를 반환)")
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
            @ApiResponse(code = 200, message = "성공적으로 비밀번호가 변경되었습니다.")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/changePassword")
    public ResponseEntity<UserResponseDto> changePassword(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                          @RequestParam String userPassword) {
        User user = userService.changePassword(userPrincipal.getNo(), userPassword);
        return ResponseEntity.status(HttpStatus.OK).body(UserResponseDto.of(user));
    }
}
