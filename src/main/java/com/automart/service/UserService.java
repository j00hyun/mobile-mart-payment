package com.automart.service;

import com.automart.domain.User;
import com.automart.exception.ForbiddenSignUpException;
import com.automart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final SMSService smsService;

    /**
     * 로컬 회원 이메일 확인
     */
    public void checkDuplicateEmail(User user) throws ForbiddenSignUpException {
        log.info("이메일 중복 검증");

        Optional<User> findUser = userRepository.findByEmailAndSnsType(user.getEmail(), "LOCAL");

        if(findUser.isPresent()) {
            throw new ForbiddenSignUpException("동일한 이메일의 회원이 이미 존재합니다.");
        }
    }

    /**
     * 중복 휴대폰 확인
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
     * 회원가입
     */
    @Transactional
    public Integer saveUser(User user) {
        checkDuplicateEmail(user);
        checkDuplicateTel(user);
        userRepository.save(user);
        return user.getNo();
    }
}
