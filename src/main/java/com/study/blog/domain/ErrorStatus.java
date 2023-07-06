package com.study.blog.domain;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {
    NotFoundPost(1,"해당 포스트를 찾을 수 없습니다."),
    NotAuthorize(2,"해당 요청에 접근할 수 있는 권한이 없습니다.");

    private final int code;

    private final String reason;


}
