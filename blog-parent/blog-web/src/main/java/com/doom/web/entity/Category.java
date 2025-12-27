package com.doom.web.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
/**
 * 文章分类实体类
 * 对应数据库表：sys_category
 *
 * 字段说明：
 * - id: 分类ID，主键，自增
 * - name: 分类名称，如"技术分享"、"生活随笔"等
 * - sort: 排序序号，数值越小越靠前
 * - createTime: 创建时间
 *
 * 用途：
 * - 文章归类：每篇文章关联一个分类，便于用户按主题浏览
 * - 分类统计：管理员可查看各分类下的文章数量
 * - 分类筛选：用户可按分类筛选文章
 *
 * @Author: doom
 * @Date: 2025/12/25/00:08
 */
@Data
@TableName("sys_category")
public class Category {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer sort;
    private LocalDateTime createTime;
}
