package com.automart.user.controller;

import com.automart.exception.NotFoundUserException;
import com.automart.exception.SigninFailedException;
import com.automart.jwt.JwtTokenProvider;
import com.automart.user.domain.User;
import com.automart.user.dto.SignInRequestDto;
import com.automart.user.dto.SignUpRequestDto;
import com.automart.user.repository.UserRepository;
import com.automart.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

@Api(tags = {"2. User"})
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    /**
     * 로컬 회원가입
     */
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDto requestDto) {
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

    /**
     * 로컬 로그인
     */
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


    /**
     * 이메일로 특정 회원 조회
     */


    /**
     * 회원 전체 조회
     */

    /**
     * 회원 탈퇴
     */

    /**
     * 회원 비밀번호 변경
     */

}