package com.doom.web.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.doom.web.entity.Article;
import com.doom.web.mapper.ArticleMapper;
import com.doom.web.mapper.CategoryMapper;
import com.doom.web.mapper.UserMapper;
import com.doom.web.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 文章管理控制器（管理员专用）
 * 提供文章审核、数据统计等管理功能
 *
 * 功能说明：
 * 1. 文章审核 - 查看待审核文章列表，执行通过/拒绝操作
 * 2. 数据统计 - 点击量排行、分类统计、平台概览数据
 *
 * 访问权限：仅限管理员（ADMIN角色）访问
 *
 * @Author: doom
 * @Date: 2025/12/25/02:05
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/article")
public class ArticleAdminController {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 获取待审核文章列表
     * 功能：查询所有状态为待审核（status=0）的文章
     * 效果：返回待审核文章列表，包含文章标题、摘要、作者、分类等信息
     * 用途：管理员在后台查看并处理待审核的文章
     *
     * @return Result包装的待审核文章VO列表
     */
    @GetMapping("/audit/list")
    public Result<?> auditList() {
        return Result.success(articleMapper.selectArticleVO(0, null, null));
    }

    /**
     * 审核文章
     * 功能：对指定文章执行审核操作，更新文章状态
     * 效果：将文章状态更新为通过（status=1）或拒绝（status=2）
     * 状态说明：
     *   - 0: 待审核
     *   - 1: 已通过（公开可见）
     *   - 2: 已拒绝（不可见）
     * 为文章增加驳回理由
     * @param article 包含文章ID，目标状态,驳回理由的对象
     * @return Result包装的操作结果
     */
    @PostMapping("/audit")
    public Result<?> audit(@RequestBody Article  article) {
            LambdaUpdateWrapper<Article> updateWrapper = Wrappers.<Article>lambdaUpdate()
                    .set(Article::getStatus, article.getStatus())
                    .eq(Article::getId, article.getId());
            //当状态为2，保存驳回理由
        if (article.getStatus() == 2){
            updateWrapper.set(Article::getRejectReason, article.getRejectReason());
        }
        articleMapper.update(article, updateWrapper);
        return Result.success("审核操作已完成");
    }

    /**
     * 获取点击量排行榜
     * 功能：查询点击量最高的前10篇文章
     * 效果：返回按点击量降序排列的文章列表，用于Echarts图表展示
     * 用途：在管理后台展示热门文章排行，帮助管理员了解内容热度
     *
     * @return Result包装的点击量Top10文章列表
     */
    @GetMapping("/stats/clicks")
    public Result<?> getClickStats() {
        return Result.success(articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .orderByDesc(Article::getViewCount).last("limit 10")));
    }

    /**
     * 获取平台概览数据
     * 功能：统计博客平台的核心运营数据
     * 效果：返回包含以下指标的统计数据：
     *   - articleCount: 总文章数
     *   - userCount: 总用户数
     *   - categoryCount: 总分类数
     *   - viewCount: 总访问量（所有文章点击量之和）
     * 用途：在管理后台首页展示平台运营概况
     *
     * @return Result包装的统计数据Map
     */
    @GetMapping("/summary")
    public Result<?> getSummary() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("articleCount", articleMapper.selectCount(null));
        stats.put("userCount", userMapper.selectCount(null));
        stats.put("categoryCount", categoryMapper.selectCount(null));
        Integer totalViews = articleMapper.selectList(null)
                .stream().mapToInt(a -> a.getViewCount() == null ? 0 : a.getViewCount()).sum();
        stats.put("viewCount", totalViews);
        return Result.success(stats);
    }

    /**
     * 获取分类统计数据
     * 功能：统计各分类下的文章数量
     * 效果：返回分类名称及其对应文章数量的统计结果
     * 用途：在管理后台展示分类分布饼图或柱状图
     *
     * @return Result包装的分类统计数据
     */
    @GetMapping("/stats/categories")
    public Result<?> getCategoryStats() {
        return Result.success(articleMapper.getCategoryCountStats());
    }
}
