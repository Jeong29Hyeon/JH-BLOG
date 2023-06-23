package com.study.blog.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hashtag {
    private Long id;
    private String name;
    @Builder
    public Hashtag(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
