package com.automart.user.service;

import com.automart.security.UserPrincipal;
import com.automart.advice.exception.*;
import com.automart.security.jwt.JwtTokenProvider;
import com.automart.user.domain.User;
import com.automart.user.dto.UserResponseDto;
import com.automart.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
// @RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService implements UserDetailsService {

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
     * @param email : 이메일 주소
     */
    public void checkDuplicateEmail(String email) throws ForbiddenSignUpException {
        log.info("이메일 중복 검증");

        Optional<User> findUser = userRepository.findByEmail(email);

        if(findUser.isPresent()) {
            throw new ForbiddenSignUpException("동일한 이메일의 회원이 이미 존재합니다.");
        }
    }

    /**
     * 휴대폰 중복확인
     * @param phone_no : 휴대폰 번호
     */
    public void checkDuplicateTel(String phone_no) throws ForbiddenSignUpException {
        log.info("휴대폰 번호 중복 검증");

        Optional<User> findUser = userRepository.findByTel(phone_no);

        if(findUser.isPresent()) {
            throw new ForbiddenSignUpException("동일한 휴대폰 번호의 회원이 이미 존재합니다.");
        }
    }

    /**
     * 본인확인 인증번호 핸드폰으로 전송
     * @param phone_no : 핸드폰 번호
     * @return : 생성된 5자리 인증번호
     */
    public int validatePhone(String phone_no) throws SMSException {
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
    @Transactional
    public User changePassword(int no, String password) throws NotFoundUserException {
        log.info("비밀번호 변겅");

        User user = userRepository.findByNo(no)
                .orElseThrow(() -> new NotFoundUserException("해당하는 회원을 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(password)); // 패스워드를 인코딩을 써서 암호화한다.
        userRepository.save(user);

        return user;
    }

    /**
     * 아이디, 비밀번호 올바른지 확인
     * @param email : 이메일 주소
     * @param password : 비밀번호
     * @return : 해당 유저 정보
     */
    public User checkLogIn(String email, String password) throws NotFoundUserException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundUserException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new NotFoundUserException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    /**
     * 회원 생성
     * @param user : 생성하려는 회원 정보
     * @return : 회원 고유 번호
     */
    @Transactional
    public Integer saveUser(User user) throws ForbiddenSignUpException {
        log.info("회원 생성");

        user.setPassword(passwordEncoder.encode(user.getPassword())); // 패스워드 인코딩을 써서 암호화한다.
        userRepository.save(user);
        return user.getNo();
    }

    /**
     * 이름과 휴대폰 번호로 유저 정보 찾기
     * @param name : 이름
     * @param phone : 휴대폰 번호
     */
    public User findUserByNameAndTel(String name, String phone) throws NotFoundUserException {
        User user = userRepository.findByNameAndTel(name, phone)
                .orElseThrow(() -> new NotFoundUserException("해당하는 회원을 찾을 수 없습니다."));

        return user;
    }

    /**
     * 회원 탈퇴
     * @param token jwt 토큰
     */
    @Transactional
    public String withdraw(String token, JwtTokenProvider.TokenType tokenType) {
        if (jwtTokenProvider.validateToken(token)) {
            User user = userRepository.findByEmail(jwtTokenProvider.getUserEmail(token, JwtTokenProvider.TokenType.ACCESS_TOKEN))
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

    /**
     * Spring Security가 UserDetails 구현객체 클래스를 사용해 Authentication을 사용
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // User정보를 DB에서 가져온다
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일로 유저를 찾을 수 없습니다."));

        // DB에서 가져온 User 정보는 UserPrincipal 클래스로 변경해 Spring Security로 전달한다.
        // UserPrincipal은 Spring Security의 UserDetails를 implements 하였으므로, 이제 Spring Security는 User 클래스를 사용해 Authentication을 사용 할수 있게 되었다.
        return UserPrincipal.create(user);
    }

}
