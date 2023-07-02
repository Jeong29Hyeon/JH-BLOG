package com.study.blog.interceptor;

import com.study.blog.dto.SessionUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        SessionUser user = (SessionUser) session.getAttribute("user");
        if(user == null) {
            response.sendRedirect("/oauth2-login");
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
