package com.study.blog.domain.post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private Long id;             // PK
    private String title;        // 제목
    private String content;      // 내용
    private Boolean noticeYn;    // 공지글 여부

    @Builder
    public PostRequest(Long id, String title, String content, Boolean noticeYn) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.noticeYn = noticeYn;
    }
}
