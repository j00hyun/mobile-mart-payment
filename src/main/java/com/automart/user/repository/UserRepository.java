package com.automart.user.repository;

import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 고객 고유번호로 회원 찾기(단건)
    public Optional<User> findByNo(Integer no);

    // 휴대폰번호로 회원 찾기
    public Optional<User> findByTel(String tel);

    // email로 회원 찾기
    public Optional<User> findByEmail(String email);

    // 이름과 휴대폰번호가 일치하는 회원 찾기
    public Optional<User> findByNameAndTel(String name, String tel);

}
