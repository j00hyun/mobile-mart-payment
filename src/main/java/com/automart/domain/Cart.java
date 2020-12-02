package com.automart.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
}
