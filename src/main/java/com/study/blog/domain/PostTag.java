package com.study.blog.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostTag {
    private Long id;
    private Long postId;
    private Long tagId;

    @Builder
    public PostTag(Long id, Long postId, Long tagId) {
        this.id = id;
        this.postId = postId;
        this.tagId = tagId;
    }
}
