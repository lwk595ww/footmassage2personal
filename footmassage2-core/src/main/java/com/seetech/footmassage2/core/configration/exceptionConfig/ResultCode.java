package com.seetech.footmassage2.core.configration.exceptionConfig;

public enum ResultCode {
    /*成功状态码*/
    SUCCESS(1, "成功"),

    /*参数错误 1001-1099*/
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, " 参数为空"),
    PARAMTYPE_BIND_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /*用戸錯渓: 2001-2999*/
    USER_NOTLOGGED_IN(2001, "用户未登陆,访问的路径需要认证,请登陆"),
    USER_LOGIN_ERROR(2002, "账号不存在或密码错误"),
    USER_ACCOUNT_FORBIDDEN(2003, "账号已被禁用"),
    USER_NOT_EXIST(2004, "用户不存在"),
    USER_HAS_EXISTED(2005, "用户已存在"),
    USER_CAPTCHA_ERROR(2006, "验证码错误"),

    /*数据库错误3001-3009 */
    Table_Not_EXIST(3001, "表不存在"),
    SQL_SYNTAX_EORROR(3002, "动态SQL拼接错误"),

    /*其他错误*/
    UNKOWN_ERROR(9001, "");


    private int code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }


}