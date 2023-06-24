package com.study.blog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Getter
public class Pagination {
    private int totalRecordCount; //전체 데이터 수
    private int totalPageSize; //전체 페이지 수
    private int startPage;
    private int endPage;
    private int limitStart;
    private boolean existPrevPage; //이전 페이지 존재 여부
    private boolean existNextPage; //다음 페이지 존재 여부
    List<Integer> pageList = new ArrayList<>();

    public Pagination(int totalRecordCount, SearchDto searchDto) {
        if(totalRecordCount > 0) {
            this.totalRecordCount = totalRecordCount;
            paging(searchDto);
            searchDto.setPagination(this);
        }
    }

    private void paging(SearchDto searchDto) {
        totalPageSize = (int)Math.ceil(totalRecordCount / (double) searchDto.getRecordSize());
        startPage = ((searchDto.getPage() -1) / searchDto.getPageSize()) * searchDto.getPageSize() + 1;
        endPage = Math.min(startPage + searchDto.getPageSize() -1,totalPageSize);
        existPrevPage = startPage != 1;
        existNextPage = endPage != totalPageSize;
        limitStart = (searchDto.getPage() -1) * searchDto.getPageSize();
        for(int i = startPage; i <= endPage; i++) {
            pageList.add(i);
        }
    }
}
