package com.study.blog.domain;
import com.study.blog.oauth2.Role;
import com.study.blog.oauth2.SocialType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
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

    public String getRoleKey() {
        return this.role.getKey();
    }
}