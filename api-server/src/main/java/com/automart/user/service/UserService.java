package com.automart.user.service;

import com.automart.exception.NotFoundUserException;
import com.automart.product.domain.Product;
import com.automart.product.dto.ProductResponseDto;
import com.automart.user.domain.User;
import com.automart.exception.ForbiddenSignUpException;
import com.automart.user.dto.SignUpRequestDto;
import com.automart.user.dto.UserResponseDto;
import com.automart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SMSService smsService;

    /**
     * 로컬 회원 이메일 확인
     * @param user : 사용자 정보
     */
    public void checkDuplicateEmail(User user) throws ForbiddenSignUpException {
        log.info("이메일 중복 검증");

        Optional<User> findUser = userRepository.findByEmailAndSnsType(user.getEmail(), "LOCAL");

        if(findUser.isPresent()) {
            throw new ForbiddenSignUpException("동일한 이메일의 회원이 이미 존재합니다.");
        }
    }

    /**
     * 휴대폰 중복확인
     * @param user : 사용자 정보
     */
    public void checkDuplicateTel(User user) throws ForbiddenSignUpException {
        log.info("휴대폰 번호 중복 검증");

        Optional<User> findUser = userRepository.findByTel(user.getTel());

        if(findUser.isPresent()) {
            throw new ForbiddenSignUpException("동일한 휴대폰 번호의 회원이 이미 존재합니다.");
        }
    }

    /**
     * 본인확인 인증번호 핸드폰으로 전송
     * @param phone_no : 핸드폰 번호
     * @return : 생성된 4자리 인증번호
     */
    public int validatePhone(String phone_no) {
        log.info("인증번호 전송");

        int valiNum = (int) (Math.random() * 100000);
        String message = " [인증번호]\n" + valiNum;
        smsService.sendMessage(phone_no, message);
        return valiNum;
    }


    /**
     * 임시 비밀번호 핸드폰으로 전송
     * @param phone_no : 핸드폰 번호
     * @return : 생성된 임시 비밀번호
     */
    public String generateTempPw(String phone_no) {
        log.info("임시 비밀번호 전송");

        String pw = "";
        for (int i = 0; i < 12; i++) {
            pw += (char) ((Math.random() * 26) + 97);
        }
        String message = "[임시 비밀번호]\n" + pw;
        smsService.sendMessage(phone_no, message);
        return pw;
    }

    /**
     * 비밀번호 변경
     * @param no : 회원 고유번호
     * @param password : 새 비밀번호
     * @return : 유저 정보
     */
    public User changePassword(int no, String password) throws NotFoundUserException {
        log.info("비밀번호 변겅");

        User user = userRepository.findByNo(no)
                .orElseThrow(() -> new NotFoundUserException("해당하는 회원을 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(password)); // 패스워드 인코딩을 써서 암호화한다.
        return user;
    }

    /**
     * 회원 생성
     * @param user : 생성하려는 회원 정보
     * @return : 회원 고유 번호
     */
    @Transactional
    public Integer saveUser(User user) {
        log.info("회원 생성");

        checkDuplicateEmail(user); // 이메일 중복 검증
        checkDuplicateTel(user); // 연락처 중복 검증
        user.setPassword(passwordEncoder.encode(user.getPassword())); // 패스워드 인코딩을 써서 암호화한다.
        userRepository.save(user);
        return user.getNo();
    }

    // 로그인 시 토큰발급을 위한 유저의 정보를 불러온다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 로그인시 email를 입력받기 때문에 email로 조회
        User user = userRepository.findByEmail(email).orElseThrow(NotFoundUserException::new);

        UserDetails userDetails = new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                return authorities;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail(); // 여기서는 email로 식별자를 사용했으므로 email을 반환한다.
            }

            // 그 아래 설정들은 모두 true로 설정해준다.
            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }
            @Override
            public boolean isEnabled() {
                return true;
            }
        };
        return userDetails;
    }

    public UserResponseDto showUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalStateException("해당하는 회원을 찾을 수 없습니다."));
        return UserResponseDto.of(user);
    }
}
