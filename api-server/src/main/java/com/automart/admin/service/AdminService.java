package com.automart.admin.service;

import com.automart.admin.domain.Admin;
import com.automart.admin.repository.AdminRepository;
import com.automart.exception.ForbiddenSignUpException;
import com.automart.exception.NotFoundUserException;
import com.automart.user.domain.User;
import com.automart.user.repository.UserRepository;
import com.automart.user.service.SMSService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminService {

    private AdminRepository adminRepository;

    /**
     * 관리자 아이디 중복 검증
     * @param admin : 중복확인하려는 관리자 정보
     */
    public void checkDuplicateId(Admin admin) throws ForbiddenSignUpException {
        log.info("아이디 중복 검증");

        Optional<Admin> findAdmin = adminRepository.findById(admin.getId());

        if(findAdmin.isPresent()) {
            throw new ForbiddenSignUpException("동일한 아이디의 관리자 이미 존재합니다.");
        }
    }

    /**
     * 관리자 생성
     * @param admin : 생성하려는 관리자 정보
     * @return : 관리자 고유 번호
     */
    @Transactional
    public int saveUser(Admin admin) {
        log.info("관리자 생성");

        checkDuplicateId(admin);
        adminRepository.save(admin);
        return admin.getNo();
    }
}
