package com.study.blog.controller;
import com.study.blog.domain.SocialType;
import com.study.blog.domain.Users;
import com.study.blog.dto.SessionUser;
import com.study.blog.service.KakaoService;
import com.study.blog.service.NaverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.SecureRandom;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {
    private final KakaoService kakaoService;
    private final NaverService naverService;

    @GetMapping("/")
    public String index() {
        return "redirect:/post";
    }

    @GetMapping("/oauth2-login")
    public String oauth2Login() {
        return "login";
    }

    @GetMapping("/kakao-login")
    public String kakaoLogin() {
        return "redirect:https://kauth.kakao.com/oauth/authorize?client_id=ff5fbe39cb292ecd96bd27eb8415bf9a&redirect_uri=http://sian0369.cafe24.com/login/oauth/kakao&response_type=code";
    }

    @GetMapping("/naver-login")
    public String naverLogin(HttpSession session) {
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130,random).toString();
        session.setAttribute("state",state);
        return "redirect:https://nid.naver.com/oauth2.0/authorize?client_id=pcoAzgHoNqxyxgYcRFuv&redirect_uri=http://jeong29hyeon.org/login/oauth/naver&state="+state+"&response_type=code";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if(session.getAttribute("user") != null) {
            SessionUser user = (SessionUser) session.getAttribute("user");
            SocialType socialType = user.getSocialType();
            if(socialType == SocialType.KAKAO) {
                return "redirect:https://kauth.kakao.com/oauth/logout?client_id=ff5fbe39cb292ecd96bd27eb8415bf9a&logout_redirect_uri=http://sian0369.cafe24.com/logout/oauth/kakao";
            } else if(socialType == SocialType.NAVER) {
                session.invalidate();
            }
        }
        return "redirect:/post";
    }

    @GetMapping("/login/oauth/kakao")
    public String kakaoAuthCode(@RequestParam(value="code", required = false) String code,
                                RedirectAttributes ra,
                                HttpSession session) {
        if(code == null) {
            ra.addFlashAttribute("msg","카카오 로그인을 취소하였습니다.");
            return "redirect:/post";
        }
        log.info("카카오 인가 코드: " + code);
        String accessToken = kakaoService.getKakaoAccesstoken(code);
        log.info("카카오 엑세스토큰: " + accessToken);
        Users user = kakaoService.getKakaoUserInfo(accessToken);
        session.setAttribute("user",new SessionUser(user));
        return "redirect:/post";
    }

    @GetMapping("/login/oauth/naver")
    public String naverAuthCode(@RequestParam(value="code", required = false) String code,
                                RedirectAttributes ra,
                                HttpSession session) {
        if(code == null) {
            ra.addFlashAttribute("msg","카카오 로그인을 취소하였습니다.");
            return "redirect:/post";
        }
        String state = (String) session.getAttribute("state");
        String naverAccessToken = naverService.getNaverAccessToken(code, state);
        Users user = naverService.getNaverUserInfo(naverAccessToken);
        session.setAttribute("user",new SessionUser(user));
        return "redirect:/post";
    }

    @GetMapping("/logout/oauth/kakao")
    public String kakaoLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/post";
    }
}

