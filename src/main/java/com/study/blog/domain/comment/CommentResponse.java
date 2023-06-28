package com.study.blog.domain.comment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {
    private Long id;
    private Long postId;
    private String content;
    private Long userId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long parentId; //댓글의 그룹을 확인하는 용으로, 댓글 그룹의 부모 PK를 가지게 된다.
    private int depth; //댓글일시 1, 대댓글일시 2
}
