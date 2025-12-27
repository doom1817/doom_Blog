package com.doom.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doom.web.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类数据访问接口
 * 继承BaseMapper，提供分类的数据库操作
 *
 * 功能说明：
 * - 提供分类CRUD操作（继承自BaseMapper）
 * - 支持分类列表查询和统计
 *
 * @Author: doom
 * @Date: 2025/12/25/00:10
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
