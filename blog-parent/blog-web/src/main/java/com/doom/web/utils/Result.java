package com.doom.web.utils;

import lombok.Data;

/**
 * 统一响应结果封装类
 * 用于封装所有Controller接口的返回结果
 *
 * 字段说明：
 * - code: 响应状态码（200:成功, 500:失败）
 * - msg: 响应消息，描述操作结果
 * - data: 响应数据，泛型类型，支持任意类型
 *
 * 使用示例：
 * - 成功返回：Result.success(data)
 * - 失败返回：Result.error("错误信息")
 *
 * @Author: doom
 * @Date: 2025/12/25/00:03
 */
@Data
public class Result<T> {
    private Integer code;
    private String msg;
    private T data;

    /**
     * 成功响应
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}
