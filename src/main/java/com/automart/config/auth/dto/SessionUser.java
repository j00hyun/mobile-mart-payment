/**
 * 인증된 사용자 정보만 필요
 * 자식 엔티티를 갖고 있다면 직렬화 대상에 자식들까지 포함되니 성능 이슈, 부수 효과가 발생할 확률이 높다
 * 그래서 직렬화 기능을 가진 세션 Dto를 하나 추가로 만든 것이 더 좋은 방법
 */
package com.automart.config.auth.dto;

import com.automart.domain.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String email;
    private String snsType;

    public SessionUser(User user) {
        this.email = user.getEmail();
        this.snsType = user.getSnsType();
    }
}