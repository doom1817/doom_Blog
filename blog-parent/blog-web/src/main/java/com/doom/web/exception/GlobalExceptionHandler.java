package com.doom.web.exception;
import com.doom.web.utils.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * 全局异常处理器
 * 统一处理Controller层抛出的异常
 *
 * 功能说明：
 * - 捕获所有未处理的异常，避免暴露堆栈信息给前端
 * - 统一异常响应格式，提升用户体验
 * - 记录异常日志，便于问题排查
 *
 * 处理流程：
 * 1. Controller抛出异常
 * 2. GlobalExceptionHandler捕获异常
 * 3. 打印异常堆栈到控制台
 * 4. 返回统一的错误响应格式
 *
 * 响应格式：
 * {
 *   "code": 500,
 *   "msg": "系统运行出错：xxx",
 *   "data": null
 * }
 *
 * @Author: doom
 * @Date: 2025/12/25/01:22
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理所有异常
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        e.printStackTrace();
        return Result.error("系统运行出错：" + e.getMessage());
    }
}
