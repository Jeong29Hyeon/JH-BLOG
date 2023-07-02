package com.study.blog.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.study.blog.mapper.TempPostImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/application-cloud.yml")
public class S3Uploader {
    private final AmazonS3Client amazonS3Client;
    private final TempPostImageMapper tempPostImageMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            return null;
        }
        String contentType = file.getContentType();
        String extension = checkExtension(contentType);
        String saveFileName = "images/"+System.nanoTime() + extension;
        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            amazonS3Client.putObject(bucket,saveFileName,file.getInputStream(),metadata);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("아마존 S3 파일 업로드 실패");
        }
        return saveFileName;
    }

    public String uploadPostImage(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            return null;
        }
        String contentType = file.getContentType();
        String extension = checkExtension(contentType);
        String saveFileName = "postImages/"+System.nanoTime() + extension;
        ObjectMetadata metadata= new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        try {
            amazonS3Client.putObject(bucket,saveFileName,file.getInputStream(),metadata);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("아마존 S3 파일 업로드 실패");
        }
        tempPostImageMapper.insertImageName(saveFileName);
        return saveFileName;
    }

    private String checkExtension(String contentType) {
        Map<String, String> response = new HashMap<>();
        String extension;
        if(!StringUtils.hasText(contentType)) {
            throw new RuntimeException("파일의 확장자가 존재하지 않습니다.");
        }
        if(contentType.equals("image/jpeg")) {
            extension = ".jpg";
        } else if (contentType.equals("image/png")) {
            extension = ".png";
        } else if (contentType.equals("image/gif")) {
            extension = ".gif";
        }else {
            throw new RuntimeException("jpg, png만 업로드 할 수 있습니다.");
        }
        return extension;
    }

    public void deleteImage(String saveFileName) {
        try {
            amazonS3Client.deleteObject(bucket,saveFileName);
        } catch (SdkClientException e) {
            e.printStackTrace();
            throw new RuntimeException("Fail Amazon S3 delete file");
        }
    }

    public void deleteTempPostImages(String content) {
        String pattern = "postImages\\/[0-9]+(.gif|.png|.jpg)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(content);
        // 포스트 content에서 최종적으로 업로드한 파일들 목록 뽑기
        Map<String,String> contentImages = new HashMap<>();
        while (matcher.find()) {
            contentImages.put(matcher.group(),"1");
        }
        //findAll로 List 뽑아오기
        List<String> tempImages = tempPostImageMapper.findAllImageName();
        for (String tempImage : tempImages) { //임시저장된 이미지들
            String temp = contentImages.get(tempImage); //포스트 내용에서 찾아서
            if(temp == null) { //수정을 통해 없어진 이미지면
                deleteImage(tempImage); //삭제
            }
        }
        tempPostImageMapper.deleteAll(); //DB에 모든 임시 이미지들 지우기
    }
}
