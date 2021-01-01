package com.automart.user.controller;

import com.automart.exception.SigninFailedException;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import com.automart.user.dto.AuthResponseDto;
import com.automart.user.dto.SignInRequestDto;
import com.automart.user.dto.SignUpRequestDto;
import com.automart.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.Collections;

@Api(tags = {"2. User"})
@RestController
@RequestMapping("/users")
public class SignController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @ApiOperation(value = "로컬 회원가입", notes = "로컬 회원가입을 한다")
    @PostMapping(value = "/signup")
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
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

    @ApiOperation(value = "로컬 로그인", notes = "이메일로 회원 로그인을 한다")
    @PostMapping(value = "/signin")
    public ResponseEntity<?> singIn(@Valid @RequestBody SignInRequestDto requestDto) throws SigninFailedException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getEmail(),
                        requestDto.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.createToken(authentication);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }
}
