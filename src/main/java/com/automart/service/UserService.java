package com.automart.service;

import com.automart.domain.User;
import com.automart.exception.ForbiddenSignUpException;
import com.automart.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    /**
     * 로컬 회원 이메일 확인
     */
    public void checkDuplicateEmail(String email) throws ForbiddenSignUpException {
        log.info("이메일 중복 검증");

        Optional<User> user = userRepository.findByEmailAndSnsType(email, "LOCAL");

        if(user.isPresent()) {
            throw new ForbiddenSignUpException("동일한 이메일의 회원이 이미 존재합니다.");
        }
    }

    /**
     * Spring Security 필수 메소드
     * 로컬 로그인
     */
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailAndSnsType(email, "LOCAL")
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * 중복 휴대폰 확인
     */
    public void checkDuplicateTel(String tel) throws ForbiddenSignUpException {
        log.info("휴대폰 번호 중복 검증");

        Optional<User> user = userRepository.findByTel(tel);

        if(user.isPresent()) {
            throw new ForbiddenSignUpException("동일한 휴대폰 번호의 회원이 이미 존재합니다.");
        }
    }

    /**
     * 회원가입
     */
    public int saveUser(String email, String password, String name, String tel) {
        // 입력받은 패스워드 암호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        password = encoder.encode(password);

        return userRepository.save(User.createUserByApp(email, password, name, tel)).getNo();
    }
}
