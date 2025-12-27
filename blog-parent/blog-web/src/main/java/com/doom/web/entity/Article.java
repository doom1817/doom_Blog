package com.doom.web.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 文章实体类
 * 对应数据库表：blog_article
 *
 * 字段说明：
 * - id: 文章ID，主键，自增
 * - title: 文章标题
 * - summary: 文章摘要
 * - content: 文章内容
 * - categoryId: 分类ID，关联sys_category表
 * - userId: 作者ID，关联sys_user表
 * - status: 文章状态（0:待审核, 1:已发布, 2:已拒绝）
 * - viewCount: 点击量
 * - createTime: 创建时间，自动填充
 * - updateTime: 更新时间，自动填充
 *
 * 审核流程：
 * 1. 用户发布文章时，status默认为0（待审核）
 * 2. 管理员审核通过后，status更新为1（已发布），文章公开可见
 * 3. 管理员拒绝后，status更新为2（已拒绝），文章不可见
 *
 * @Author: doom
 * @Date: 2025/12/25/00:05
 */
@Data
@TableName("blog_article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String summary;
    private String content;
    private Long categoryId;
    private Long userId;
    private Integer status;
    private Integer viewCount;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
