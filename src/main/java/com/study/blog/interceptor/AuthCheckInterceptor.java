package com.study.blog.interceptor;

import com.study.blog.domain.Role;
import com.study.blog.dto.SessionUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SessionUser user = (SessionUser) request.getSession().getAttribute("user");
        if(user == null || user.getRole() != Role.USER) {
            response.sendRedirect("/post");
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
