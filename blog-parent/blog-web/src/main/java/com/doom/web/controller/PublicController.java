package com.doom.web.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.doom.web.entity.*;
import com.doom.web.mapper.*;
import com.doom.web.utils.Result;
import com.doom.web.vo.ArticleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * 公开访问控制器
 * 提供无需登录即可访问的公开接口，包括文章列表、文章详情和评论查询
 *
 * 功能说明：
 * 1. 文章列表查询 - 支持按分类筛选，仅展示已审核通过的文章
 * 2. 文章详情查看 - 自动增加点击量，仅展示已审核通过的文章
 * 3. 评论列表查询 - 获取指定文章的所有评论及评论者信息
 *
 * 访问权限：所有接口均公开访问，无需身份验证
 *
 * @Author: doom
 * @Date: 2025/12/25/00:31
 */
@RestController
@RequestMapping("/api/public")
public class PublicController {
    @Autowired private ArticleMapper articleMapper;
    @Autowired private CommentMapper commentMapper;

    /**
     * 获取首页文章列表
     * 功能：查询所有已审核通过的文章，支持按分类筛选
     * 效果：返回文章列表，包含文章标题、摘要、分类名、作者名、发布时间等信息
     * 安全性：仅返回status=1（已通过审核）的文章，未审核文章不展示
     *
     * @param categoryId 可选参数，分类ID。不传则查询所有分类的文章
     * @return Result包装的文章VO列表
     */
    @GetMapping("/article/list")
    public Result<?> list(@RequestParam(required = false) Long categoryId) {
        return Result.success(articleMapper.selectArticleVO(1, null, categoryId));
    }

    /**
     * 获取文章详情
     * 功能：查询指定文章的详细信息，并自动增加文章点击量
     * 效果：返回文章完整内容，同时view_count字段自增1，用于统计文章热度
     * 安全性：仅允许查看已审核通过（status=1）的文章，否则返回错误提示
     *
     * @param id 文章ID，路径参数
     * @return Result包装的文章VO对象，包含文章完整信息
     */
    @GetMapping("/article/{id}")
    public Result<?> detail(@PathVariable Long id) {
        ArticleVO article = articleMapper.getArticleWithCategory(id);
        if (article != null && article.getStatus() == 1) {
            articleMapper.update(null, Wrappers.<Article>lambdaUpdate()
                    .setSql("view_count = view_count + 1")
                    .eq(Article::getId, id));
            return Result.success(article);
        }
        return Result.error("文章不存在或未通过审核");
    }

    /**
     * 获取文章评论列表
     * 功能：查询指定文章的所有评论，包含评论者信息
     * 效果：返回评论列表，每条评论包含评论内容、评论时间、评论者昵称、评论者头像等
     * 用途：用于在文章详情页展示评论区，支持公开浏览所有评论
     *
     * @param articleId 文章ID，路径参数
     * @return Result包装的评论VO列表
     */
    @GetMapping("/comments/{articleId}")
    public Result<?> getComments(@PathVariable Long articleId) {
        return Result.success(commentMapper.selectListWithUser(articleId));
    }
}
