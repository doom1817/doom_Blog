package com.doom.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doom.web.entity.Comment;
import com.doom.web.vo.CommentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 评论数据访问接口
 * 继承BaseMapper，提供评论的数据库操作
 *
 * 功能说明：
 * - 提供评论CRUD操作（继承自BaseMapper）
 * - 查询文章评论列表（含评论者信息）
 *
 * @Author: doom
 * @Date: 2025/12/25/00:10
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT c.*, u.nickname, u.avatar FROM blog_comment c " +
            "LEFT JOIN sys_user u ON c.user_id = u.id " +
            "WHERE c.article_id = #{articleId} ORDER BY c.create_time DESC")
    List<CommentVO> selectListWithUser(Long articleId);
}
