package com.study.blog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class MyErrorController implements ErrorController {
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                log.error("404에러 발생 => URI : " + request.getRequestURI());
                return "error/404";
            }  else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                log.error("405에러 발생 => " + request.getMethod());
                return "error/405";
            } else if(statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                log.error("500 서버 에러 발생 => ");
                return "error/500";
            }
        }
        return "error/404";
    }
}
