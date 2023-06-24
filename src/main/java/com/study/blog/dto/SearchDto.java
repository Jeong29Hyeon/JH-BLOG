package com.study.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDto {
    private int page; //현재 페이지 번호
    private int recordSize; //한 화면에 보여줄 글 갯수
    private int pageSize; //페이지네이션 사이즈
    private String hashtag; //해시태그
    private String keyword; //검색어
    private String searchType; //검색 타입
    private Pagination pagination; //페이지네이션 정보

    public SearchDto() {
        this.page = 1;
        this.recordSize = 5;
        this.pageSize = 5;
    }

    @Override
    public String toString() {
        return "SearchDto{" +
                "page=" + page +
                ", recordSize=" + recordSize +
                ", pageSize=" + pageSize +
                ", hashtag='" + hashtag + '\'' +
                ", keyword='" + keyword + '\'' +
                ", searchType='" + searchType + '\'' +
                ", pagination=" + pagination +
                '}';
    }
}
