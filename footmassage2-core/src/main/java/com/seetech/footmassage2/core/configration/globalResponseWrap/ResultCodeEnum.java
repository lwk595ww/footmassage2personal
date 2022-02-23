package com.seetech.footmassage2.core.configration.globalResponseWrap;

public enum ResultCodeEnum {
    /*成功状态码*/
    SUCCESS(200, "成功"),

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

    /*第三方网络请求错误4001-4009*/
    OTHER_NET_ERROR(4001, "第三方网络访问异常"),
    NET_BLOCK_ERROR(4002, "当前人数过多，系统繁忙，请稍后重试！！(降级触发)"),

    /*第三方网络请求错误5001-5009*/
    Net_ERROR(5001, "第三方网络访问异常"),
    Net2_ERROR(5002, "当前人数过多，系统繁忙，请稍后重试！！(降级触发)"),

    /*其他错误*/
    UNKOWN_ERROR(9001, "自定义错误");


    private int code;
    private String message;

    ResultCodeEnum(Integer code, String message) {
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