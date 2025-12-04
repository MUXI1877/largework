package com.it.quanxianguanli.config;

import com.it.quanxianguanli.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String path = request.getRequestURI();
        
        // 允许的公开路径
        if (path.startsWith("/api/auth/") || path.startsWith("/api/user/register") || path.startsWith("/api/user/verify")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                String username = jwtUtil.extractUsername(token);
                if (username != null && jwtUtil.validateToken(token, username)) {
                    request.setAttribute("username", username);
                    request.setAttribute("roleId", jwtUtil.extractRoleId(token));
                    return true;
                }
            } catch (Exception e) {
                // Token无效
            }
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
}

