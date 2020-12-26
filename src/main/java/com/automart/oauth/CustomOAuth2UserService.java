package com.automart.oauth;

import com.automart.exception.OAuth2AuthenticationProcessingException;
import com.automart.oauth.user.OAuth2UserInfo;
import com.automart.oauth.user.OAuth2UserInfoFactory;
import com.automart.user.domain.AuthProvider;
import com.automart.user.domain.User;
import com.automart.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 소셜 로그인 시도
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    /**
     * 소셜 로그인 유무 확인
     */
    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getId());
        User user;

        if(userOptional.isPresent()) {
            user = userOptional.get();
            // 이미 소셜로그인을 한적이 있다면 유저 정보 신규 업데이트
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            // 처음 소셜로그인을 진행한다면 새로운 유저 생성
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    /**
     * 소셜로그인을 통한 유저 생성
     */
    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = User.builder()
                .snsType(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))
                .snsKey(oAuth2UserInfo.getId())
                .email(oAuth2UserInfo.getEmail())
                .build();

        return userRepository.save(user);
    }

    /**
     * 기존 유저 정보 업데이트
     */
    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setEmail(oAuth2UserInfo.getEmail());
        return userRepository.save(existingUser);
    }
}
