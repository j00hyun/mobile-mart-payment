package com.automart.category.domain;

import com.automart.product.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_no")
    private int no; // 카테고리 고유번호

    @Column(name = "category_name", length = 45, unique = true)
    private String name; // 카테고리 이름

    @Column(name = "category_code", length = 45, unique = true)
    private String code; // 카테고리 코드

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    /**
     * 카테고리 수정
     */
    public void setName(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /**
     * 카테고리 생성
     */
    @Builder
    public Category(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
