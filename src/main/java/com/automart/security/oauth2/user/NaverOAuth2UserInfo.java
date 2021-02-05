package com.automart.security.oauth2.user;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(String userNameAttributeName, Map<String, Object> attributes) {
        super(userNameAttributeName, attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() { return (String) attributes.get("name"); }
}
