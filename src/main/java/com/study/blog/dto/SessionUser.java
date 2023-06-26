package com.study.blog.dto;

import com.study.blog.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SessionUser {
    private final String nickname;
    private final String image;

    public SessionUser(Users user) {
        this.nickname = user.getNickname();
        this.image = user.getImageUrl();
    }
}
