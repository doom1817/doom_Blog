package com.doom.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doom.web.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户数据访问接口
 * 继承BaseMapper，提供用户的数据库操作
 *
 * 功能说明：
 * - 提供用户CRUD操作（继承自BaseMapper）
 * - 支持用户名查询（用于登录验证）
 *
 * @Author: doom
 * @Date: 2025/12/25/00:10
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
