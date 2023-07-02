package com.study.blog.controller;
import com.study.blog.domain.SocialType;
import com.study.blog.domain.Users;
import com.study.blog.dto.SessionUser;
import com.study.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {
    private final UserService userService;

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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if(session.getAttribute("user") != null) {
            SessionUser user = (SessionUser) session.getAttribute("user");
            SocialType socialType = user.getSocialType();
            if(socialType == SocialType.KAKAO) {
                return "redirect:https://kauth.kakao.com/oauth/logout?client_id=ff5fbe39cb292ecd96bd27eb8415bf9a&logout_redirect_uri=http://sian0369.cafe24.com/logout/oauth/kakao";
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
        String accessToken = userService.getKakaoAccesstoken(code);
        log.info("카카오 엑세스토큰: " + accessToken);
        Users user = userService.getKakaoUserInfo(accessToken);
        session.setAttribute("user",new SessionUser(user));
        return "redirect:/post";
    }

    @GetMapping("/logout/oauth/kakao")
    public String kakaoLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/post";
    }
}

