package com.study.blog.oauth2;

import com.study.blog.domain.Users;
import com.study.blog.dto.SessionUser;
import com.study.blog.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserMapper userMapper;
    private final HttpSession httpSession;
    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest
                .getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);

        String userNameAttributeName = userRequest
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes extractAttributes = OAuthAttributes
                .of(socialType, userNameAttributeName,
                        oAuth2User.getAttributes());

        Users createdUser = getUser(extractAttributes,socialType); //getUser 메서드로 User객체 생성후 반환

        httpSession.setAttribute("user", new SessionUser(createdUser));

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdUser.getRoleKey())),
                oAuth2User.getAttributes(),
                extractAttributes.getNameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getRole());
    }

    private Users getUser(OAuthAttributes attributes, SocialType socialType) {
        Map<String,Object> map = new HashMap<>();
        map.put("socialType",socialType);
        map.put("socialId",attributes.getOauth2UserInfo().getId());
        Users findUser = userMapper.findBySocialTypeAndSocialId(map);
        if(findUser == null) {
            return saveUser(attributes,socialType);
        }
        return findUser;
    }

    private SocialType getSocialType(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        if(KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return SocialType.GOOGLE;
    }

    private Users saveUser(OAuthAttributes attributes, SocialType socialType) {
        Users createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        userMapper.save(createdUser);
        return createdUser;
    }



}

