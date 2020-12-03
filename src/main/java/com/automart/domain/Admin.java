package com.automart.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "admin")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Admin {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "admin_no")
    private int no; // 관리자 고유번호

    @Column(name = "admin_id", length = 45, nullable = false)
    private String id; // 관리자 아이디

    @Column(name = "admin_pw", length = 45, nullable =false)
    private String password; // 관리자 비밀번호

}
