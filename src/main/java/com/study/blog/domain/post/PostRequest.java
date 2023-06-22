package com.study.blog.domain.post;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {
    private Long id;             // PK
    private String title;        // 제목
    private String description;  // 포스트 설명
    private String content;      // 내용

    @Builder
    public PostRequest(Long id, String title, String description, String content) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.content = content;
    }
}
