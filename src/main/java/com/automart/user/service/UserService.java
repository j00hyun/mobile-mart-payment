package com.automart.user.service;

import com.automart.exception.InvalidTokenException;
import com.automart.exception.NotFoundUserException;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import com.automart.exception.ForbiddenSignUpException;
import com.automart.user.dto.UserResponseDto;
import com.automart.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
// @RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SMSService smsService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, SMSService smsService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.smsService = smsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 로컬 회원 이메일 확인
     * @param user : 사용자 정보
     */
    public void checkDuplicateEmail(User user) throws ForbiddenSignUpException {
        log.info("이메일 중복 검증");

        Optional<User> findUser = userRepository.findByEmailAndSnsType(user.getEmail(), AuthProvider.local);

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

        user.setPassword(passwordEncoder.encode(password)); // 패스워드를 인코딩을 써서 암호화한다.
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

    /**
     * 회원 탈퇴
     * @param token jwt 토큰
     */
    @Transactional
    public String withdraw(String token) {
        if (jwtTokenProvider.validateToken(token)) {
            User user = userRepository.findByNo(Integer.valueOf(jwtTokenProvider.getUserNo(token)))
                    .orElseThrow(() -> new NotFoundUserException("해당하는 회원을 찾을 수 없습니다."));
            userRepository.delete(user);
            return "delete success";
        } else {
            throw new InvalidTokenException("Expried Token");
        }
    }

    /**
     * (어드민용) 이메일로 회원 조회하기
     * @param email 조회할 회원의 이메일
     * @return : UserResponseDto를 반환
     */
    public UserResponseDto showUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalStateException("해당하는 회원을 찾을 수 없습니다."));
        return UserResponseDto.of(user);
    }

    /**
     * (어드민용) 전체 회원 조회하기
     * @return List<UerResponseDto>를 반환
     */
    public List<UserResponseDto> showUsers() {
        List<User> users = userRepository.findAll();
        return UserResponseDto.listOf(users);
    }
}
