package com.doom.web.vo;
import com.doom.web.entity.Comment;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 评论视图对象
 * 继承自Comment实体类，用于前端展示评论信息
 *
 * 继承字段（来自Comment）：
 * - id, articleId, userId, content, createTime
 *
 * 扩展字段：
 * - nickname: 评论者昵称，用于前端展示评论者信息
 * - avatar: 评论者头像URL，用于前端展示评论者头像
 *
 * 用途：
 * - 评论列表：在文章详情页展示所有评论及评论者信息
 * - 评论展示：显示评论内容、评论时间、评论者昵称和头像
 *
 * @Author: doom
 * @Date: 2025/12/25/02:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentVO extends Comment {
    private String nickname;
    private String avatar;
}
