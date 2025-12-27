package com.doom.web.handler;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

/**
 * MyBatis-Plus元数据自动填充处理器
 * 实现MetaObjectHandler接口，自动填充公共字段
 *
 * 功能说明：
 * - 插入数据时自动填充createTime和updateTime
 * - 更新数据时自动填充updateTime
 *
 * 使用场景：
 * - Article实体：插入时自动设置创建时间和更新时间
 * - Category实体：插入时自动设置创建时间
 * - User实体：插入时自动设置创建时间
 *
 * 配置方式：
 * 在实体类字段上使用@TableField注解指定填充策略
 * - @TableField(fill = FieldFill.INSERT): 插入时填充
 * - @TableField(fill = FieldFill.INSERT_UPDATE): 插入和更新时都填充
 *
 * @Author: doom
 * @Date: 2025/12/25/01:19
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
