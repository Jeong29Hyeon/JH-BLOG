package com.study.blog.domain.post;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {
    private Long id;                       // PK
    private String title;                  // 제목
    private String description;            // 설명
    private String thumbnail;
    private String content;                // 내용
    private int likeCnt;                   // 좋아요 수
    private Boolean deleteYn;              // 삭제 여부
    private LocalDateTime createdDate;     // 생성일시
    private LocalDateTime modifiedDate;    // 최종 수정일시
}
