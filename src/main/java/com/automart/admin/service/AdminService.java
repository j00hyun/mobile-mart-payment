package com.automart.admin.service;

import com.automart.admin.domain.Admin;
import com.automart.admin.repository.AdminRepository;
import com.automart.advice.exception.ForbiddenSignUpException;
import com.automart.advice.exception.NotFoundUserException;
import com.automart.advice.exception.SignInTypeErrorException;
import com.automart.security.UserPrincipal;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.user.domain.User;
import com.automart.user.dto.AuthResponseDto;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 아이디, 비밀번호 올바른지 확인
     * @param id : 아이디
     * @param password : 비밀번호
     * @return : 해당 관리자 정보
     */
    public Admin checkLogIn(String id, String password) throws NotFoundUserException, SignInTypeErrorException {
        if(id.contains("@")) {
            throw new SignInTypeErrorException("올바른 아이디 형식이 아닙니다.");
        }
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundUserException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if(!passwordEncoder.matches(password, admin.getPassword())) {
            throw new NotFoundUserException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return admin;
    }
}
