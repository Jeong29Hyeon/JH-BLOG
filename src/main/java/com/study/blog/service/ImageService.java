package com.study.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ImageService {
    private final String path = "c://upload/blog/image";
    private final String postPath = "c://upload/blog/post";

    public String uploadImage(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        String contentType = file.getContentType();
        String extension = checkExtension(contentType);
        String saveFileName = System.nanoTime() + extension;
        File saveFile = new File(path,saveFileName);
        if(!saveFile.exists()) {
            saveFile.mkdirs();
        }
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return saveFileName;
    }

    public String uploadPostImage(MultipartFile file) {
        if(file == null || file.isEmpty()) {
            return null;
        }
        String contentType = file.getContentType();
        String extension = checkExtension(contentType);
        String saveFileName = System.nanoTime() + extension;
        File saveFile = new File(postPath,saveFileName);
        if(!saveFile.exists()) {
            saveFile.mkdirs();
        }
        try {
            file.transferTo(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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


    public void deleteImage(String fileName) {
        File deleteFile = new File(path,fileName);
        if(deleteFile.exists()) {
            deleteFile.delete();
        }
    }
}
