package com.automart.admin.repository;

import com.automart.admin.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

    // 관리자 고유번호로 관리자 조회하기(단건)
    public Optional<Admin> findByNo(Integer no);

    // 관리자 아이디로 관리자 조회하기(단건)
    public Optional<Admin> findById(String id);
}
