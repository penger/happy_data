package com.gp.quantificat.interceptor;

import com.gp.quantificat.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author Administrator
 * @Create 2023-08-13 12:44
 */

@Configuration
@Slf4j
public class CheckLoginInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        System.out.println("登录拦截器");
//        Object user1 = request.getSession().getAttribute("user");
//        System.out.println(user1.toString());
        User user = (User) request.getSession().getAttribute("user");
        if(user == null) {
            response.sendRedirect("/toLogin");
            return false; // 终止请求继续处理
        }else {
            return true;
        }
    }



}