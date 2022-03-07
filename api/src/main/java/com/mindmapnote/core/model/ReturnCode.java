package com.mindmapnote.core.model;

/**
 * 错误类型枚举
 *
 * @author Leon
 * @date 2021/06/16 14:45
 */
public enum ReturnCode {

    // 返回的错误信息

    SUCCESS(0, "success"),
    FAILED(-1, "failed");

    private int code;
    private String msg;


    ReturnCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}