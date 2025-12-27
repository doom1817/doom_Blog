package com.doom.web.controller;

import com.doom.web.entity.Category;
import com.doom.web.mapper.ArticleMapper;
import com.doom.web.mapper.CategoryMapper;
import com.doom.web.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 分类管理控制器（管理员专用）
 * 提供文章分类的增删改查功能
 *
 * 功能说明：
 * 1. 新增分类 - 创建新的文章分类
 * 2. 删除分类 - 移除指定分类
 * 3. 修改分类 - 更新分类信息
 * 4. 查询分类 - 获取所有分类列表
 *
 * 访问权限：仅限管理员（ADMIN角色）访问
 *
 * @Author: doom
 * @Date: 2025/12/25/01:54
 */
@RestController
@RequestMapping("/api/admin/category")
public class CategoryAdminController {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 新增分类
     * 功能：创建一个新的文章分类
     * 效果：在数据库中插入新的分类记录
     * 用途：管理员创建新的文章分类，用于文章归类
     *
     * @param c 包含分类名称、描述等信息的分类对象
     * @return Result包装的操作结果
     */
    @PostMapping("/add")
    public Result<?> add(@RequestBody Category c) {
        categoryMapper.insert(c);
        return Result.success("新增成功");
    }

    /**
     * 删除分类
     * 功能：根据ID删除指定的分类
     * 效果：从数据库中移除该分类记录
     * 注意：删除分类后，关联该分类的文章可能需要处理
     *
     * @param id 分类ID，路径参数
     * @return Result包装的操作结果
     */
    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        categoryMapper.deleteById(id);
        return Result.success("删除成功");
    }

    /**
     * 修改分类
     * 功能：更新指定分类的信息
     * 效果：更新数据库中的分类记录
     * 用途：管理员修改分类名称、描述等信息
     *
     * @param c 包含分类ID和更新信息的分类对象
     * @return Result包装的操作结果
     */
    @PutMapping("/update")
    public Result<?> update(@RequestBody Category c) {
        categoryMapper.updateById(c);
        return Result.success("修改成功");
    }

    /**
     * 获取分类列表
     * 功能：查询系统中所有分类
     * 效果：返回所有分类的列表
     * 用途：在前端展示分类选择器、分类导航等
     *
     * @return Result包装的分类列表
     */
    @GetMapping("/list")
    public Result<?> list() {
        return Result.success(categoryMapper.selectList(null));
    }
}
