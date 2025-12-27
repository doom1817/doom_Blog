package com.doom.web.controller;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.doom.web.entity.*;
import com.doom.web.mapper.*;
import com.doom.web.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * 用户文章及评论管理控制器
 * 提供已登录用户的文章和评论管理功能
 *
 * 功能说明：
 * 1. 文章管理 - 发布、修改、删除、查询个人文章
 * 2. 评论管理 - 发表评论、删除自己的评论
 * 3. 权限控制 - 普通用户只能操作自己的数据，管理员可操作所有数据
 *
 * 访问权限：需要登录认证（JWT Token）
 *
 * @Author: doom
 * @Date: 2025/12/25/00:32
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserArticleController {
    @Autowired private ArticleMapper articleMapper;
    @Autowired private UserMapper userMapper;
    @Autowired private CommentMapper commentMapper;

    /**
     * 获取当前登录用户对象
     * 功能：从Spring Security上下文中获取当前用户信息
     * 效果：返回完整的User实体对象
     * 安全性：依赖JWT过滤器验证身份
     *
     * @return 当前登录用户的User对象
     */
    private User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    /**
     * 发布文章
     * 功能：用户发布新文章，提交审核
     * 效果：创建新文章记录，初始状态为待审核（status=0）
     * 审核流程：
     *   - 初始状态：status=0（待审核）
     *   - 管理员审核通过后：status=1（公开可见）
     *   - 管理员拒绝后：status=2（不可见）
     *
     * @param a 包含标题、内容、分类ID等信息的文章对象
     * @return Result包装的操作结果
     */
    @PostMapping("/article/add")
    public Result<?> addArticle(@RequestBody Article a) {
        a.setUserId(getCurrentUser().getId());
        a.setStatus(0);
        a.setViewCount(0);
        a.setCreateTime(LocalDateTime.now());
        articleMapper.insert(a);
        return Result.success("已提交审核");
    }

    /**
     * 修改文章
     * 功能：用户修改已发布的文章
     * 效果：更新文章内容，状态重置为待审核
     * 权限控制：只有文章作者可以修改自己的文章
     * 审核重置：修改后文章需重新审核（status=0）
     *
     * @param a 包含文章ID和更新内容的文章对象
     * @return Result包装的操作结果
     */
    @PutMapping("/article/update")
    public Result<?> updateArticle(@RequestBody Article a) {
        Article old = articleMapper.selectById(a.getId());
        if (!old.getUserId().equals(getCurrentUser().getId())) return Result.error("无权修改");
        a.setStatus(0);
        articleMapper.updateById(a);
        return Result.success("更新成功，等待重新审核");
    }

    /**
     * 删除文章
     * 功能：删除指定文章
     * 效果：从数据库中永久移除文章记录
     * 权限控制：
     *   - 文章作者：可删除自己的文章
     *   - 管理员：可删除任何文章（强制删除）
     *   - 其他用户：无权删除
     *
     * @param id 文章ID，路径参数
     * @return Result包装的操作结果，包含删除者身份提示
     */
    @DeleteMapping("/article/delete/{id}")
    public Result<?> deleteArticle(@PathVariable Long id) {
        Article a = articleMapper.selectById(id);
        if (a == null) return Result.error("文章不存在");

        User currentUser = getCurrentUser();

        boolean isAdmin = "ADMIN".equals(currentUser.getRole());
        boolean isOwner = a.getUserId().equals(currentUser.getId());

        if (isAdmin || isOwner) {
            articleMapper.deleteById(id);
            return Result.success(isAdmin ? "管理员已强制删除" : "删除成功");
        }

        return Result.error("权限不足：你只能删除自己的文章");
    }

    /**
     * 发表评论
     * 功能：用户对指定文章发表评论
     * 效果：创建新的评论记录，关联文章和用户
     * 用途：用户在文章详情页发表评论
     *
     * @param c 包含文章ID和评论内容的评论对象
     * @return Result包装的操作结果
     */
    @PostMapping("/comment/add")
    public Result<?> addComment(@RequestBody Comment c) {
        c.setUserId(getCurrentUser().getId());
        c.setCreateTime(LocalDateTime.now());
        commentMapper.insert(c);
        return Result.success("评论成功");
    }

    /**
     * 删除评论
     * 功能：删除指定的评论
     * 效果：从数据库中移除评论记录
     * 权限控制：只有评论作者可以删除自己的评论
     *
     * @param id 评论ID，路径参数
     * @return Result包装的操作结果
     */
    @DeleteMapping("/comment/delete/{id}")
    public Result<?> deleteComment(@PathVariable Long id) {
        Comment c = commentMapper.selectById(id);
        if (c.getUserId().equals(getCurrentUser().getId())) {
            commentMapper.deleteById(id);
            return Result.success("删除成功");
        }
        return Result.error("无权删除");
    }

    /**
     * 获取文章列表（根据用户角色）
     * 功能：根据当前用户角色返回不同的文章列表
     * 效果：
     *   - 普通用户：只返回自己发布的文章
     *   - 管理员：返回全站所有文章（用于管理）
     * 用途：在"我的文章"页面展示用户的文章，管理员可管理全站文章
     *
     * @return Result包装的文章VO列表
     */
    @GetMapping("/article/list")
    public Result<?> listMyArticles() {
        User currentUser = getCurrentUser();
        String role = currentUser.getRole();
        Long userId = currentUser.getId();

        if ("ADMIN".equals(role)) {
            log.info("DEBUG: 管理员 [{}] 正在管理全站文章", currentUser.getUsername());
            return Result.success(articleMapper.selectArticleVO(null, null, null));
        } else {
            log.info("DEBUG: 用户 [{}] 正在查看个人文章", currentUser.getUsername());
            return Result.success(articleMapper.selectArticleVO(null, userId, null));
        }
    }
}
