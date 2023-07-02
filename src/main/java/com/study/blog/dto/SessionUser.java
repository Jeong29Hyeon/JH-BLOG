package com.study.blog.dto;

import com.study.blog.domain.SocialType;
import com.study.blog.domain.Users;
import com.study.blog.domain.Role;
import lombok.Getter;

@Getter
public class SessionUser {
    private final Long id; //db상의 유저 pk
    private final String nickname;
    private final String image;
    private final Role role;
    private final SocialType socialType;

    public SessionUser(Users user) {
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.image = user.getImageUrl();
        this.role = user.getRole();
        this.socialType = user.getSocialType();
    }
}
