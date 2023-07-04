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

@RequiredArgsConstructor
@Slf4j
@Service
public class KakaoService {
    private final UserMapper userMapper;

    public String getKakaoAccesstoken(String authorizeCode) {
        String accessToken = "";
        String refreshToken = "";
        String reqUrl = "https://kauth.kakao.com/oauth/token";
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=ff5fbe39cb292ecd96bd27eb8415bf9a");
            sb.append("&redirect_uri=http://sian0369.cafe24.com/login/oauth/kakao");
            sb.append("&code=").append(authorizeCode);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode(); //200이면 성공
            log.info("access token request responseCode = " + responseCode);
            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),
                        StandardCharsets.UTF_8));
                String line = "";
                StringBuilder result = new StringBuilder();

                while((line = br.readLine())!=null){
                    result.append(line);
                }
                System.out.println("response body= " + result);

                JSONParser parser = new JSONParser();
                JSONObject obj = (JSONObject) parser.parse(String.valueOf(result));
                accessToken = obj.get("access_token").toString();
                refreshToken = obj.get("refresh_token").toString();
                log.info("accessToken = " + accessToken);
                log.info("refreshToken = " + refreshToken);
                br.close();
            }
            bw.close();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
        return accessToken;
    }

    public Users getKakaoUserInfo(String accessToken) {
        Users user = new Users();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");

            int responseCode = conn.getResponseCode();
            log.info("user info request responseCode : " + responseCode);
            if(responseCode == 200){
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
                JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
                JSONObject profile = (JSONObject) kakao_account.get("profile");
                boolean email_needs_agreement = (boolean) kakao_account.get("email_needs_agreement");
                boolean profile_image_needs_agreement = (boolean) kakao_account.get("profile_image_needs_agreement");
                String socialId = obj.get("id").toString();

                // 프로필 이미지 설정
                if(!profile_image_needs_agreement) { //프로필 이미지 동의가 필요한가? (false) 면 이미 동의가 돼있는 것이니 가져올 수 있다.
                    String imageUrl = profile.get("thumbnail_image_url").toString();
                    user.setImageUrl(imageUrl);
                } else {
                    user.setImageUrl("https://ssl.pstatic.net/static/pwe/address/img_profile.png");
                }
                // 이메일 설정
                if(!email_needs_agreement){//이메일 동의가 필요한가 -> false 면 이메일이 있음
                    String email = kakao_account.get("email").toString();
                    user.setEmail(email);
                }else{
                    String uuid = UUID.randomUUID().toString().substring(0,10);
                    user.setEmail(uuid+"@socialUser.com");
                }
                // 소셜로그인 PK
                user.setSocialId(socialId);
                user.setSocialType(SocialType.KAKAO);
                String nickname = profile.get("nickname").toString();
                user.setNickname(nickname);
                //이미 서버에 등록돼있는 유저인지 체크
                Map<String,Object> map = new HashMap<>();
                map.put("socialType",user.getSocialType());
                map.put("socialId",user.getSocialId());
                Users findUser = userMapper.findBySocialTypeAndSocialId(map);
                if(findUser != null) {
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
            conn.setRequestProperty("Authorization","Bearer "+accessToken);
            int responseCode = conn.getResponseCode();

            String line = "";
            StringBuilder result = new StringBuilder();
            if(responseCode == 200){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while((line = br.readLine())!=null){
                    result.append(line);
                }
            }
            System.out.println(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
