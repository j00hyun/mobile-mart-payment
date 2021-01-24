package com.automart.user.service;

import com.automart.advice.exception.DuplicateDataException;
import com.automart.advice.exception.SessionUnstableException;
import com.automart.user.domain.Admin;
import com.automart.user.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 아이디 중복 확인
     * @param id : 관리자 아이디
     */
    public void checkDuplicateId(String id) throws DuplicateDataException {
        Optional<Admin> findAdmin = adminRepository.findById(id);

        if(findAdmin.isPresent()) {
            throw new DuplicateDataException("동일한 아이디의 관리자가 이미 존재합니다.");
        }
    }

    /**
     * 관리자 생성
     * @param admin : 생성하려는 관리자 정보
     * @return : 관리자 고유 번호
     */
    @Transactional
    public Integer saveAdmin(Admin admin) throws DuplicateDataException {
        admin.setPassword(passwordEncoder.encode(admin.getPassword())); // 패스워드 인코딩을 써서 암호화한다.
        adminRepository.save(admin);
        return admin.getNo();
    }

    /**
     * 아이디, 비밀번호 올바른지 확인
     * @param id : 아이디
     * @param password : 비밀번호
     */
    public void checkLogIn(String id, String password) throws SessionUnstableException {
        Admin admin = adminRepository.findById(id)
                .orElseThrow(() -> new SessionUnstableException("아이디 또는 비밀번호가 일치하지 않습니다."));

        if(!passwordEncoder.matches(password, admin.getPassword())) {
            throw new SessionUnstableException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

    }
}
