package com.tensquare.friend.interceptor;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("====拦截器=====");
        //无论如何都放行，具体能不能操作还在具体的操作中判断
        //拦截只是负责把有请求头中包含token的令牌进行一个解析
        //获取请求头
        String authHeader = request.getHeader("Authorization");
        //如果头部包含Bearer 就进行解析
        if (authHeader!=null && authHeader.startsWith("Bearer ")){
            //截去头部，提取token
            String token = authHeader.substring(7);
            try {
                Claims claims = jwtUtil.parseJWT(token);
                String role = (String) claims.get("roles");
                if (role!=null && role.equals("admin")) {
                    request.setAttribute("claims_admin",claims);
                    System.out.println("claims_admin");
                }else if(role!=null && role.equals("user")){
                    request.setAttribute("claims_user",claims);
                    System.out.println("claims_user");
                }
            }catch (Exception e){
                throw new RuntimeException("令牌错误");
            }
        }
        return true;
    }
}
