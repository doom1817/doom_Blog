package com.doom.web.filter;
import com.doom.web.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
/**
 * JWT认证过滤器
 * 继承OncePerRequestFilter，确保每个请求只执行一次
 *
 * 功能说明：
 * - 从请求头中提取JWT Token
 * - 解析Token并验证有效性
 * - 将用户信息设置到Spring Security上下文
 * - 支持跨域OPTIONS预检请求
 *
 * 执行流程：
 * 1. 检查是否为OPTIONS请求，直接放行
 * 2. 从Authorization请求头获取Token（格式：Bearer xxx）
 * 3. 解析Token获取用户名和角色
 * 4. 将用户信息和权限封装到SecurityContext
 * 5. 继续执行后续过滤器链
 *
 * Token格式：
 * Header: Authorization: Bearer <token>
 *
 * 注意事项：
 * - 角色必须加"ROLE_"前缀，否则hasRole()会匹配失败
 * - Token解析失败时记录日志，但不中断请求链
 *
 * @Author: doom
 * @Date: 2025/12/25/00:17
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String header = request.getHeader("Authorization");
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            Claims claims = jwtUtils.parseToken(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("JWT 校验失败: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
