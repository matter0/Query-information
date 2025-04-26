package com.example.viewinformation.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 统一响应结果
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private String code; // 业务状态码
    private String message; // 提示信息
    private T data; // 响应数据

    // 快速返回操作成功响应结果(不带响应数据)
    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.code = "200";
        return result;
    }


    // 快速返回操作成功响应结果(带响应数据)
    public static <T> Result<T> success(T data) {
        return new Result<>("200", "成功", data);
    }

    // 快速返回操作失败响应结果
    public static Result<?> error(String code, String message) {
        return new Result<>(code, message, null);
    }
}