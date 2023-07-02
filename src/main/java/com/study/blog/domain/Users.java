package com.study.blog.domain;
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
    private String nickname;
    private String imageUrl;

    @Builder
    public Users(Long id, String email, Role role, SocialType socialType, String socialId, String nickname, String imageUrl) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }
}
