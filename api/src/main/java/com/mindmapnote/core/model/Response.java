package com.mindmapnote.core.model;

/**
 * 接口响应信息类
 *
 * @author Leon
 * @date 2021/06/18 15:12
 */
public class Response<T> {

    private Integer code;
    private String errorMsg;
    private String message;
    private T result;

    public Response() {
    }

    private Response(T result) {
        this.code = 0;
        this.result = result;
    }

    private Response(Integer code, String errorMsg) {

        this.code = code;
        this.errorMsg = errorMsg;
    }

    private Response(Integer code, String errorMsg, T result) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.result = result;
    }

    private Response(Integer code, String message, String errorMsg, T result) {
        this.code = code;
        this.message = message;
        this.errorMsg = errorMsg;
        this.result = result;
    }

    public static <T> Response<T> success() {
        return (Response<T>) success("");
    }

    public static <T> Response<T> success(String message) {
        return new Response<T>(0, message, null, null);
    }

    public static <T> Response<T> success(T result) {
        return new Response<T>(result);
    }

    public static <T> Response<T> successStrResult(T result) {
        return new Response<T>(0, null, null, result);
    }

    public static <T> Response<T> error(ReturnCode returnCode) {
        return new Response<T>(returnCode.getCode(), returnCode.getMsg());
    }

    public static <T> Response<T> error(ReturnCode returnCode, String errorMsg) {
        return new Response<T>(returnCode.getCode(), returnCode.getMsg()+"-"+errorMsg);
    }

    public static <T> Response<T> error(ReturnCode returnCode, T result) {
        return new Response<T>(returnCode.getCode(), returnCode.getMsg(), result);
    }

    public static <T> Response<T> error(ReturnCode returnCode, String errorMsg, T result) {
        return new Response<T>(returnCode.getCode(), returnCode.getMsg() + errorMsg, result);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
