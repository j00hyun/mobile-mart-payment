package com.automart.admin.domain;

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

    @Column(name = "admin_id", length = 45, nullable = false, unique = true)
    private String id; // 관리자 아이디

    @Column(name = "admin_pw", length = 45, nullable =false)
    private String password; // 관리자 비밀번호

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 관리자 생성
     */
    public static Admin createAdmin(String id, String password) {
        Admin admin = new Admin();
        admin.setId(id);
        admin.setPassword(password);

        return admin;
    }
}
