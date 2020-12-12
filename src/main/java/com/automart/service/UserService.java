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
    private SMSService smsService;

    /**
     * Spring Security 필수 메소드
     * 로컬 로그인
     * @param email : 이메일
     * @return : 이메일에 해당하는 유저 정보
     */
    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailAndSnsType(email, "LOCAL")
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * 이메일 중복확인
     * @param email : 중복확인 요청하는 이메일 주소
     */
    public void checkDuplicateEmail(String email) throws ForbiddenSignUpException {
        log.info("이메일 중복 검증");

        Optional<User> user = userRepository.findByEmailAndSnsType(email, "LOCAL");

        if(user.isPresent()) {
            throw new ForbiddenSignUpException("동일한 이메일의 회원이 이미 존재합니다.");
        }
    }

    /**
     * 휴대폰 중복확인
     * @param tel : 중복확인 요청하는 휴대폰 번호
     */
    public void checkDuplicateTel(String tel) throws ForbiddenSignUpException {
        log.info("휴대폰 번호 중복 검증");

        Optional<User> user = userRepository.findByTel(tel);

        if(user.isPresent()) {
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

//    public int changePassword(int no, String password) {
//        log.info("비밀번호 변겅");
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        password = encoder.encode(password);
//
//        Optional<User> user = userRepository.findByNo(no);
//
//        if(user.isPresent()) {
//        }
//    }

    /**
     * 로컬 회원 생성
     * @param email : 이메일
     * @param password : 비밀번호
     * @param name : 이름
     * @param tel : 전화번호
     * @return : 유저의 고유번호
     */
    public int saveUser(String email, String password, String name, String tel) {
        log.info("로컬 회원 생성");

        // 입력받은 패스워드 암호화
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        password = encoder.encode(password);

        return userRepository.save(User.createUserByApp(email, password, name, tel)).getNo();
    }
}
