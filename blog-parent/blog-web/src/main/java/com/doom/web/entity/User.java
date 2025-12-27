package com.doom.web.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
/**
 * 用户实体类
 * 对应数据库表：sys_user
 *
 * 字段说明：
 * - id: 用户ID，主键，自增
 * - username: 用户名，唯一标识
 * - password: 密码，BCrypt加密存储
 * - nickname: 用户昵称，用于展示
 * - avatar: 头像URL
 * - role: 用户角色（ADMIN:管理员, USER:普通用户）
 * - createTime: 注册时间
 *
 * 角色权限：
 * - ADMIN: 管理员，拥有所有权限，可审核文章、管理分类、删除任意文章
 * - USER: 普通用户，只能发布/修改/删除自己的文章和评论
 *
 * @Author: doom
 * @Date: 2025/12/25/00:08
 */
@Data
@TableName("sys_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private String role;
    private LocalDateTime createTime;
}
