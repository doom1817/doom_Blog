package com.doom.web.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doom.web.entity.User;
import com.doom.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户详情服务实现类
 * 实现Spring Security的UserDetailsService接口
 *
 * 功能说明：
 * - 根据用户名加载用户详情
 * - 将自定义User实体转换为Spring Security的UserDetails
 * - 为用户设置角色权限
 *
 * 使用场景：
 * - 用户登录时，Spring Security调用此方法加载用户信息
 * - JWT过滤器解析Token后，也需要用户信息进行认证
 *
 * 注意事项：
 * - 角色必须加"ROLE_"前缀，否则hasRole()会匹配失败
 * - 返回的UserDetails包含用户名、密码和权限列表
 *
 * @Author: doom
 * @Date: 2025/12/25/00:15
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) throw new UsernameNotFoundException("用户不存在");

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}
