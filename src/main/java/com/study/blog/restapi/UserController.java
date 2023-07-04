package com.study.blog.restapi;

import com.study.blog.domain.Users;
import com.study.blog.dto.SessionUser;
import com.study.blog.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserMapper userMapper;

    @PatchMapping("/users/{userId}")
    public Map<String,Object> changeUserInfo(
            @PathVariable final Long userId,
            final String nickname,
            HttpSession session) {
        Map<String,Object> response = new HashMap<>();
        SessionUser user = (SessionUser) session.getAttribute("user");

        if(!Pattern.matches("[a-z0-9가-힣]{3,12}",nickname)) {
            response.put("errorMsg","3~12자 사이의 영문 소문자와 한글만 입력가능합니다.");
            return response;
        }
        int count = userMapper.existNickname(nickname);
        if(count != 0) {
            response.put("errorMsg","이미 존재하는 닉네임입니다.");
            return response;
        }
        Users users = Users.builder().id(user.getId()).nickname(nickname).build();
        int result = userMapper.update(users);
        if(result != 1) {
            response.put("errorMsg","닉네임 변경에 실패했습니다.");
            return response;
        }
        response.put("msg","정상적으로 변경되었습니다.");
        Users findUser = userMapper.findById(user.getId());
        SessionUser sessionUser = new SessionUser(findUser);
        session.setAttribute("user",sessionUser);
        return response;
    }
}
