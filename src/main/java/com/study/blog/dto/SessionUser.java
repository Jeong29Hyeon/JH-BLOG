package com.study.blog.dto;

import com.study.blog.domain.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SessionUser {
    private final Long id; //db상의 유저 pk
    private final String nickname;
    private final String image;

    public SessionUser(Users user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.image = user.getImageUrl();
    }
}
