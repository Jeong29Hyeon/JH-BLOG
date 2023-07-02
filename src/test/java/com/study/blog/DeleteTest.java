package com.study.blog;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeleteTest {
    @Test
    public void regexTest1() throws Exception {
        String content = "블라블라머시기머시기" +
                "블라브랄 머시기서미시기기기긱기asmdasdaiwfnainfaf" +
                "enunfsenfsefnsefnselgnsglusnflusenlfnseflsnflesnflsenfes" +
                "nsefnsflesnflsefenslfesnlefn(postImages/533412313.gif)" +
                "asdsadsadsads(postImages/533222412313.jpg)" +
                "adhsaudsaduhsauda(postImages/533412313.png)";
        String pattern = "postImages\\/[0-9]+(.gif|.png|.jpg)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(content);
        // 포스트 content에서 최종적으로 업로드한 파일들 목록 뽑기
        Map<String,String> contentImages = new HashMap<>();
        while (matcher.find()) {
            contentImages.put(matcher.group(),"1");
        }
        //findAll로 List 뽑아오기



    }
}
