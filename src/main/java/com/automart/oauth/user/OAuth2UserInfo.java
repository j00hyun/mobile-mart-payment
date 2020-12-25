package com.automart.oauth.user;

import java.util.Map;

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
