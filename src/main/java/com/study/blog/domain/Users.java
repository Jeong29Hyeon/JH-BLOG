package com.study.blog.domain;
import jdk.nashorn.internal.runtime.Debug;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Users {
    private Long id;
    private String email;
    private Role role;
    private SocialType socialType;
    private String socialId;
    private String socialName;
    private String nickname;
    private String imageUrl;

    @Builder
    public Users(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }
}
