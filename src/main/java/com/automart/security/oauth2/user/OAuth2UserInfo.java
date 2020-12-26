package com.automart.security.oauth2.user;

import java.util.Map;

/**
 * 모든 OAuth2 Provider는 different JSON response 값을 return
 * Spring Security는 map의 key-value pair를 사용해 이를 parse해 사용
 */
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 회원 고유 아이디
    public abstract String getId();

    // 회원 이메일
    public abstract String getEmail();

}
