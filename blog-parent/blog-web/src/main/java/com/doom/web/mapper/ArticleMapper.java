package com.doom.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doom.web.entity.Article;
import com.doom.web.vo.ArticleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

/**
 * 文章数据访问接口
 * 继承BaseMapper，提供文章的数据库操作
 *
 * 功能说明：
 * - 查询文章列表（支持多条件筛选）
 * - 查询文章详情（含分类和作者信息）
 * - 统计分类文章数量
 *
 * @Author: doom
 * @Date: 2025/12/25/00:09
 */
@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    @Select("<script>" +
            "SELECT a.*, c.name as category_name, u.nickname as nickname, u.username as username " +
            "FROM blog_article a " +
            "LEFT JOIN sys_category c ON a.category_id = c.id " +
            "LEFT JOIN sys_user u ON a.user_id = u.id " +
            "WHERE 1=1 " +
            "<if test='userId != null'> AND a.user_id = #{userId} </if> " +
            "<if test='status != null'> AND a.status = #{status} </if> " +
            "<if test='categoryId != null'> AND a.category_id = #{categoryId} </if> " +
            "ORDER BY a.create_time DESC" +
            "</script>")
    List<ArticleVO> selectArticleVO(@Param("status") Integer status,
                                    @Param("userId") Long userId,
                                    @Param("categoryId") Long categoryId);

    @Select("SELECT a.*, c.name as category_name, u.nickname as nickname, u.username as username " +
            "FROM blog_article a " +
            "LEFT JOIN sys_category c ON a.category_id = c.id " +
            "LEFT JOIN sys_user u ON a.user_id = u.id " +
            "WHERE a.id = #{id}")
    ArticleVO getArticleWithCategory(Long id);

    @Select("SELECT c.name as name, COUNT(a.id) as value " +
            "FROM blog_article a " +
            "RIGHT JOIN sys_category c ON a.category_id = c.id " +
            "GROUP BY c.id, c.name")
    List<Map<String, Object>> getCategoryCountStats();
}
