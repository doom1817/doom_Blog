package com.doom.web.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
/**
 * 评论实体类
 * 对应数据库表：blog_comment
 *
 * 字段说明：
 * - id: 评论ID，主键，自增
 * - articleId: 文章ID，关联blog_article表
 * - userId: 评论者ID，关联sys_user表
 * - content: 评论内容
 * - createTime: 评论时间
 *
 * 用途：
 * - 文章互动：用户可对已发布的文章发表评论
 * - 评论展示：在文章详情页展示所有评论及评论者信息
 * - 评论管理：用户可删除自己的评论
 *
 * @Author: doom
 * @Date: 2025/12/25/00:09
 */
@Data
@TableName("blog_comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private LocalDateTime createTime;
}
