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
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {
    private final UserMapper userMapper;

    public String getNaverAccessToken(String authorizeCode, String state) {
        String accessToken = "";
        String refreshToken = "";
        String reqUrl = "https://nid.naver.com/oauth2.0/token";
        try {
            StringBuilder queryString = new StringBuilder();
            queryString.append("?grant_type=authorization_code");
            queryString.append("&client_id=pcoAzgHoNqxyxgYcRFuv");
            queryString.append("&client_secret=tLuZdqMWtg");
            queryString.append("&redirect_uri=http://jeong29hyeon.org/login/oauth/naver");
            queryString.append("&code=").append(authorizeCode);
            queryString.append("&state=").append(state);
            URL url = new URL(reqUrl + queryString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode(); //200이면 성공
            log.info("access token request responseCode = " + responseCode);
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                        StandardCharsets.UTF_8));
                String line = "";
                StringBuilder result = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                log.info("naver response body= " + result);
                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(result.toString());
                accessToken = (String) obj.get("access_token");
                br.close();
            }
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return accessToken;
    }

    public Users getNaverUserInfo(String accessToken) {
        Users user = new Users();
        String reqURL = "https://openapi.naver.com/v1/nid/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            log.info("user info request responseCode : " + responseCode);
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                        StandardCharsets.UTF_8));
                String line = "";
                StringBuilder result = new StringBuilder();

                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
                log.info("response body : " + result);

                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(result.toString());
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
                user.setNickname(nickname);
                //이미 서버에 등록돼있는 유저인지 체크
                Map<String, Object> map = new HashMap<>();
                map.put("socialType", user.getSocialType());
                map.put("socialId", user.getSocialId());
                Users findUser = userMapper.findBySocialTypeAndSocialId(map);
                if (findUser != null) {
                    findUser.setImageUrl(user.getImageUrl()); //있는 유저일시 프로필 이미지 최신화
                    userMapper.updateLoginUserInfo(findUser);
                    return findUser;
                }
                user.setRole(Role.GUEST);
                userMapper.save(user);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return user;
    }


    public void kakaoLogout(String accessToken) {
        String reqUrl = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();

            String line = "";
            StringBuilder result = new StringBuilder();
            if (responseCode == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    result.append(line);
                }
            }
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
