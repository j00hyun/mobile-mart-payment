package com.automart.repository;

import com.automart.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 휴대폰번호로 회원 찾기
    public Optional<User> findByTel(String tel);

    // email로 회원 찾기
    public Optional<User> findByEmail(String email);

    // snsType별 email으로 회원 찾기
    public Optional<User> findByEmailAndSnsType(String email, String snsType);
}
