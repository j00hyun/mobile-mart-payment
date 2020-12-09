package com.automart.repository;

import com.automart.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    // 고객 고유번호로 회원 찾기(단건)
    @Query(value = "select u from User u where u.no = :no")
    public Optional<User> findByNo(@Param("no") Integer no);

    // 휴대폰번호로 회원 찾기
    @Query(value = "select u from User u where u.tel = :tel")
    public Optional<User> findByTel(@Param("tel") String tel);

    // email로 회원 찾기
    @Query(value = "select u from User u where u.email = :email")
    public Optional<User> findByEmail(@Param("email") String email);

    // snsType별 email으로 회원 찾기
    // @Query(value = "select u from User u where u.email = :no and u.snsType = :snsType")
    public Optional<User> findByEmailAndSnsType(@Param("email") String email, @Param("snsType") String snsType);
}
