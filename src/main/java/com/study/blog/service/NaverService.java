package com.study.blog.service;

import com.study.blog.domain.Role;
import com.study.blog.domain.SocialType;
import com.study.blog.domain.Users;
import com.study.blog.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {
    private final UserMapper userMapper;

    public String getNaverAccessToken(String authorizeCode, String state) {
        String accessToken = "";
        String refreshToken = "";
        String reqUrl = "https://nid.naver.com/oauth2.0/token";
        RestTemplate restTemplate = new RestTemplate();

        //쿼리 스트링 설정
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(reqUrl)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", "pcoAzgHoNqxyxgYcRFuv")
                .queryParam("client_secret", "tLuZdqMWtg")
                .queryParam("redirect_uri", "http://jeong29hyeon.org/login/oauth/naver")
                .queryParam("code", authorizeCode)
                .queryParam("state", state)
                .encode()
                .build();


        ResponseEntity<String> result = restTemplate.exchange(uri.toString(), HttpMethod.GET, new HttpEntity<>("", null), String.class);
        if (result.getStatusCode() == HttpStatus.OK) {
            log.info("naver response body= " + result.getBody());
            JSONParser parser = new JSONParser();
            JSONObject obj = null;
            try {
                obj = (JSONObject) parser.parse(result.getBody());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            accessToken = (String) obj.get("access_token");
        }
        return accessToken;
    }

    public Users getNaverUserInfo(String accessToken) {
        Users user = new Users();
        String reqURL = "https://openapi.naver.com/v1/nid/me";
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            // 요청에 필요한 Header에 포함될 내용
            headers.add("Authorization", "Bearer " + accessToken);
            HttpEntity<String> httpEntity = new HttpEntity<>("",headers);
            ResponseEntity<String> result = restTemplate.exchange(reqURL, HttpMethod.GET, httpEntity, String.class);

            if (result.getStatusCode() == HttpStatus.OK) {
                log.info("response body : " + result.getBody());
                JSONParser parser = new JSONParser();
                JSONObject obj = null;
                try {
                    obj = (JSONObject) parser.parse(result.getBody());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                JSONObject response = (JSONObject) obj.get("response");
                String email = response.get("email").toString();
                String socialId = response.get("id").toString();
                String imageUrl = response.get("profile_image").toString();
                String nickname = response.get("nickname").toString();
                // 프로필 이미지 설정
                user.setImageUrl(imageUrl);
                // 이메일 설정
                user.setEmail(email);
                // 소셜로그인 PK
                user.setSocialId(socialId);
                user.setSocialType(SocialType.NAVER);
                user.setSocialName(nickname);
                //이미 서버에 등록돼있는 유저인지 체크
                Map<String, Object> map = new HashMap<>();
                map.put("socialType", user.getSocialType());
                map.put("socialId", user.getSocialId());
                Users findUser = userMapper.findBySocialTypeAndSocialId(map);
                if (findUser != null) {
                    findUser.setSocialName(user.getSocialName()); // 소셜 이름 최신화
                    findUser.setImageUrl(user.getImageUrl()); //있는 유저일시 프로필 이미지 최신화
                    userMapper.updateLoginUserInfo(findUser);
                    return findUser;
                }
                user.setRole(Role.GUEST);
                userMapper.save(user);
                user.setNickname("네이버유저" + user.getId());
                userMapper.update(user);
            }
        return user;
    }
}
