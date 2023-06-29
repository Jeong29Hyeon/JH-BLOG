package com.study.blog.domain.comment;

import com.study.blog.domain.Users;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class CommentResponse {
    private Long id;
    private Long postId;
    private String content;
    private Users user;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long parentId; //댓글의 그룹을 확인하는 용으로, 댓글 그룹의 부모 PK를 가지게 된다.
    private int depth; //댓글일시 1, 대댓글일시 2
}
