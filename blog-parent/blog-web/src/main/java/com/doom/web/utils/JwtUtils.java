package com.doom.web.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
/**
 * JWT工具类
 * 用于生成和解析JWT令牌，实现用户身份认证
 *
 * 功能说明：
 * - 生成Token：用户登录成功后生成JWT令牌
 * - 解析Token：从JWT令牌中提取用户信息
 *
 * 配置说明：
 * - SECRET: JWT签名密钥，用于Token的签名和验证
 * - EXPIRATION: Token有效期，默认24小时
 *
 * Token包含信息：
 * - subject: 用户名
 * - role: 用户角色（ADMIN/USER）
 * - issuedAt: 签发时间
 * - expiration: 过期时间
 *
 * 使用场景：
 * 1. 用户登录成功后，调用createToken生成Token返回给前端
 * 2. 前端后续请求携带Token，后端通过JwtAuthenticationFilter解析验证
 *
 * @Author: doom
 * @Date: 2025/12/25/00:14
 */
@Component
public class JwtUtils {
    private static final String SECRET = "doom-blog-system-secure-key-2025-project";
    private static final long EXPIRATION = 86400000;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * 生成Token
     */
    public String createToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * 解析Token
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
