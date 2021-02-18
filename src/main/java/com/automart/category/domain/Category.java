package com.automart.category.domain;

import com.automart.product.domain.Product;
import com.automart.subdivision.domain.Subdivision;
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
    private int no; // 카테고리 고유 번호

    @Column(name = "category_name", length = 45, unique = true)
    private String name; // 카테고리 이름

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Subdivision> subdivisions = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    /**
     * 카테고리 수정
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 카테고리 생성
     */
    @Builder
    public Category(String name) {
        this.name = name;
    }
}
