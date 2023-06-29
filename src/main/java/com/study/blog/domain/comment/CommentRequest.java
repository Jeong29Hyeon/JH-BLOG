package com.study.blog.domain.comment;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CommentRequest {
    private Long id;
    private Long postId;
    private String content;
    private Long userId;
    private Long parentId; //대댓글일 시, 부모댓글의 pk를 여기에 담아 요청을 받는다.
    private int depth; //댓글일시 1, 대댓글일시 2
    @Builder
    public CommentRequest(Long postId, String content, Long userId, Long parentId, int depth) {
        this.postId = postId;
        this.content = content;
        this.userId = userId;
        this.parentId = parentId;
        this.depth = depth;
    }
}
