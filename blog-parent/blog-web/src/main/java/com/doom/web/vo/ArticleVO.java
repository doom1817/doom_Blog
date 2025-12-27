package com.doom.web.vo;
import com.doom.web.entity.Article;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 文章视图对象
 * 继承自Article实体类，用于前端展示文章信息
 *
 * 继承字段（来自Article）：
 * - id, title, summary, content, categoryId, userId, status, viewCount, createTime, updateTime
 *
 * 扩展字段：
 * - categoryName: 分类名称，用于前端展示分类信息
 * - nickname: 作者昵称，用于前端展示作者信息
 * - username: 作者用户名，用于前端展示作者信息
 *
 * 用途：
 * - 文章列表：展示文章标题、摘要、分类名、作者名、发布时间
 * - 文章详情：展示文章完整内容及作者、分类信息
 *
 * @Author: doom
 * @Date: 2025/12/25/02:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleVO extends Article{
    private String categoryName;
    private String nickname;
    private String username;
}
