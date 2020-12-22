package com.automart.user.controller;

import com.automart.exception.NotFoundUserException;
import com.automart.exception.SigninFailedException;
import com.automart.jwt.JwtTokenProvider;
import com.automart.user.domain.User;
import com.automart.user.dto.SignInRequestDto;
import com.automart.user.dto.SignUpRequestDto;
import com.automart.user.dto.UserResponseDto;
import com.automart.user.repository.UserRepository;
import com.automart.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(tags = {"2. User"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    @ApiOperation(value = "회원가입", notes = "회원가입을 한다")
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> signUp(@RequestBody SignUpRequestDto requestDto) {
        User user = User.builder()
                .email(requestDto.getEmail())
                .password(requestDto.getPassword())
                .tel(requestDto.getTel())
                .name(requestDto.getName())
                .snsType(requestDto.getSnsType())
                .snsKey(requestDto.getSnsKey())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @ApiOperation(value = "로컬 로그인", notes = "이메일로 회원 로그인을 한다")
    @PostMapping(value = "/signin")
    public ResponseEntity<String> singIn(@RequestBody SignInRequestDto requestDto) throws SigninFailedException {
        User user = userRepository.findByEmail(requestDto.getEmail()).
                orElseThrow(() -> new NotFoundUserException("해당하는 회원을 찾을 수 없습니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword()))
            throw new SigninFailedException("패스워드가 일치하지 않습니다.");

        return ResponseEntity.status(HttpStatus.OK).body(jwtTokenProvider.createToken(String.valueOf(user.getNo()), user.getRoles()));
    }

    /**
     * 소셜 로그인
     */


    @ApiOperation(value = "(어드민)이메일로 회원 조회", notes = "(어드민)이메일로 회원을 조회한다.")
    @GetMapping
    public ResponseEntity<UserResponseDto> showUser(@RequestHeader("JAuth_TOKEN") String token,
                                                    @RequestParam String email) {
        UserResponseDto userResponseDto = userService.showUser(email);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }


    @ApiOperation(value = "회원 전체 조회", notes = "회원목록을 조회한다")
    @GetMapping(value = "/list")
    public ResponseEntity<List<UserResponseDto>> showUsers(@RequestHeader("JAuth_TOKEN") String token) {
        List<UserResponseDto> userResponseDtos = userService.showUsers();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDtos);
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴")
    @DeleteMapping(value = "/withdraw")
    public ResponseEntity<String> withdraw(@RequestHeader("JAuth_TOKEN") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.withdraw(token));
    }

    @ApiOperation(value = "비밀번호 변경 전 확인", notes = "회원이 비밀번호 변경 전 비밀번호가 일치하는지 확인한다.")
    @PostMapping(value = "/varifyPassword")
    public ResponseEntity<Boolean> verifyPassword(@RequestHeader("JAuth_TOKEN") String token,
                                                  @RequestParam String userPassword) {
        User user = userRepository.findByNo(Integer.valueOf(jwtTokenProvider.getUserNo(token)))
                .orElseThrow(() -> new NotFoundUserException("해당하는 회원을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(userPassword, user.getPassword()))
            return ResponseEntity.status(HttpStatus.OK).body(false);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @ApiOperation(value = "비밀번호 변경", notes = "회원의 비밀번호를 변경한다.")
    @PostMapping(value = "/changePassword")
    public ResponseEntity<UserResponseDto> changePassword(@RequestHeader("JAuth_TOKEN") String token,
                                                               @RequestParam String userPassword) {
        Integer userNo = Integer.valueOf(jwtTokenProvider.getUserNo(token));
        User user = userService.changePassword(userNo, userPassword);
        return ResponseEntity.status(HttpStatus.OK).body(UserResponseDto.of(user));
    }
}
