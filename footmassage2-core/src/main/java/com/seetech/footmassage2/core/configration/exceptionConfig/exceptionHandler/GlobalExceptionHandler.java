package com.seetech.footmassage2.core.configration.exceptionConfig.exceptionHandler;



import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 处理自定义的业务异常
     * 此处理器拦截所有异常并交由全局信息返回拦截器处理{@link GlobalResponseHandler}
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object handleExceptions(Exception e, WebRequest request) {
        return e;
    }

}
