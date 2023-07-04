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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class KakaoService {
    private final UserMapper userMapper;

    public String getKakaoAccesstoken(String authorizeCode) {
        String reqUrl = "https://kauth.kakao.com/oauth/token";
        RestTemplate restTemplate = new RestTemplate();
        //헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        //바디 설정
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "ff5fbe39cb292ecd96bd27eb8415bf9a");
        body.add("redirect_uri", "http://jeong29hyeon.org/login/oauth/kakao");
        body.add("code", authorizeCode);
        //HttpHeaders 와 body를 담을 HttpEntity 생성
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        //요청
        ResponseEntity<String> result = restTemplate.exchange(reqUrl, HttpMethod.POST, httpEntity, String.class);
        String accessToken = null;
        String refreshToken = null;
        if (result.getStatusCode() == HttpStatus.OK) {
            System.out.println("response body= " + result.getBody());
            JSONParser parser = new JSONParser();
            JSONObject obj = null;
            try {
                obj = (JSONObject) parser.parse(result.getBody());
                accessToken = obj.get("access_token").toString();
                refreshToken = obj.get("refresh_token").toString();
                log.info("accessToken = " + accessToken);
                log.info("refreshToken = " + refreshToken);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return accessToken;
    }

    public Users getKakaoUserInfo(String accessToken) {
        Users user = new Users();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        RestTemplate restTemplate = new RestTemplate();

        // 요청에 필요한 Header에 포함될 내용
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        //Http Entity 생성, body가 없을 때 이렇게 가능
        HttpEntity<String> httpEntity = new HttpEntity<>("", headers);

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
            JSONObject kakao_account = (JSONObject) obj.get("kakao_account");
            JSONObject profile = (JSONObject) kakao_account.get("profile");
            boolean email_needs_agreement = (boolean) kakao_account.get("email_needs_agreement");
            boolean profile_image_needs_agreement = (boolean) kakao_account.get("profile_image_needs_agreement");
            String socialId = obj.get("id").toString();

            // 프로필 이미지 설정
            if (!profile_image_needs_agreement) { //프로필 이미지 동의가 필요한가? (false) 면 이미 동의가 돼있는 것이니 가져올 수 있다.
                String imageUrl = profile.get("thumbnail_image_url").toString();
                user.setImageUrl(imageUrl);
            } else {
                user.setImageUrl("https://ssl.pstatic.net/static/pwe/address/img_profile.png");
            }
            // 이메일 설정
            if (!email_needs_agreement) {//이메일 동의가 필요한가 -> false 면 이메일이 있음
                String email = kakao_account.get("email").toString();
                user.setEmail(email);
            } else {
                String uuid = UUID.randomUUID().toString().substring(0, 10);
                user.setEmail(uuid + "@socialUser.com");
            }
            // 소셜로그인 PK
            user.setSocialId(socialId);
            user.setSocialType(SocialType.KAKAO);
            String nickname = profile.get("nickname").toString();
            user.setSocialName(nickname);
            //이미 서버에 등록돼있는 유저인지 체크
            Map<String, Object> map = new HashMap<>();
            map.put("socialType", user.getSocialType());
            map.put("socialId", user.getSocialId());
            Users findUser = userMapper.findBySocialTypeAndSocialId(map);
            if (findUser != null) {
                findUser.setSocialName(user.getSocialName()); //있는 유저일 시 소셜이름 최신화
                findUser.setImageUrl(user.getImageUrl()); //있는 유저일시 프로필 이미지 최신화
                userMapper.updateLoginUserInfo(findUser);
                return findUser;
            }
            user.setRole(Role.GUEST);
            userMapper.save(user);
            user.setNickname("카카오유저"+user.getId());
            userMapper.update(user);
        }
        return user;
    }
}
