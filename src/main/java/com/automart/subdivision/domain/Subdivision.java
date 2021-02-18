package com.automart.subdivision.domain;

import com.automart.category.domain.Category;
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
@Table(name = "subdivision")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Subdivision {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subdiv_no")
    private int no; // 소분류 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_no", nullable = false)
    private Category category; // 카테고리 고유번호

    @Column(name = "subdiv_name", length = 45)
    private String name; // 소분류 이름

    @JsonIgnore
    @OneToMany(mappedBy = "subdivision", fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    /**
     * 소분류 수정
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 소분류 생성
     */
    @Builder
    public Subdivision(Category category, String name) {
        this.category = category;
        this.name = name;
    }
}
