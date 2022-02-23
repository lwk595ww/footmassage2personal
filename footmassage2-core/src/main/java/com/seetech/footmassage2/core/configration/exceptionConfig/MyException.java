package com.seetech.footmassage2.core.configration.exceptionConfig;


import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyException extends RuntimeException {

    private static final long serialVersionUID = 4012520104292499678L;
    private int code;
    private String message;

    public MyException(String message) {
        this(ResultCode.UNKOWN_ERROR.code(), message);
    }

    public MyException(int code, String message) {
        this.code = code;
        this.message = message;
    }


}