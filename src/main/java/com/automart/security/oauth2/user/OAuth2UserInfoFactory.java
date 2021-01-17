package com.automart.security.oauth2.user;

import com.automart.advice.exception.OAuth2AuthenticationProcessingException;
import com.automart.user.domain.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(AuthProvider.google.name())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.kakao.name())) {
            return new KakaoOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.naver.name())) {
            return new NaverOAuth2UserInfo(userNameAttributeName, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("현재 소셜로그인은 구글, 네이버, 카카오만 가능합니다.");
        }
    }
}
