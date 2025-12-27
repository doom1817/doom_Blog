package com.doom.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doom.web.utils.JwtUtils;
import com.doom.web.entity.User;
import com.doom.web.mapper.UserMapper;
import com.doom.web.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供用户注册、登录、退出等认证相关功能
 *
 * 功能说明：
 * 1. 用户注册 - 创建新用户账号，密码加密存储
 * 2. 用户登录 - 验证身份并生成JWT令牌
 * 3. 退出登录 - 清除前端Token（后端无状态）
 *
 * 访问权限：所有接口均公开访问
 *
 * @Author: doom
 * @Date: 2025/12/25/00:21
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    /**
     * 用户注册
     * 功能：创建新用户账号，进行数据校验和密码加密
     * 效果：成功注册后用户可使用该账号登录系统
     * 安全性：
     *   - 用户名唯一性校验，防止重复注册
     *   - 密码使用BCrypt加密存储，不可逆
     *   - 默认角色为USER，普通用户权限
     *
     * @param user 包含用户名、密码、昵称等信息的用户对象
     * @return Result包装的注册结果
     */
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) {
            return Result.error("用户名和密码不能为空");
        }

        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, user.getUsername()));
        if (count > 0) {
            return Result.error("用户名已存在");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null) user.setRole("USER");
        user.setCreateTime(LocalDateTime.now());

        userMapper.insert(user);
        return Result.success("注册成功");
    }

    /**
     * 用户登录
     * 功能：验证用户身份并生成JWT访问令牌
     * 效果：成功登录后返回Token及用户基本信息
     * 返回数据：
     *   - token: JWT令牌，用于后续请求的身份验证
     *   - username: 用户名
     *   - nickname: 用户昵称
     *   - role: 用户角色（USER/ADMIN）
     *   - id: 用户ID
     * 安全性：
   *   - 密码使用BCrypt匹配验证
     *   - JWT令牌包含用户名和角色信息
     *   - 令牌有效期由JwtUtils配置
     *
     * @param loginUser 包含用户名和密码的登录对象
     * @return Result包装的登录结果，包含Token和用户信息
     */
    @PostMapping("/login")
    public Result<?> login(@RequestBody User loginUser) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, loginUser.getUsername()));

        if (user == null) {
            return Result.error("用户不存在");
        }

        if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
            return Result.error("密码错误");
        }

        String token = jwtUtils.createToken(user.getUsername(), user.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", user.getUsername());
        data.put("nickname", user.getNickname());
        data.put("role", user.getRole());
        data.put("id", user.getId());

        return Result.success(data);
    }

    /**
     * 退出登录
     * 功能：用户退出系统
     * 效果：返回成功提示，前端需清除本地存储的Token
     * 说明：JWT为无状态认证，后端无需维护会话，只需前端清除Token即可
     *
     * @return Result包装的退出结果
     */
    @PostMapping("/logout")
    public Result<?> logout() {
        return Result.success("退出成功");
    }
}
