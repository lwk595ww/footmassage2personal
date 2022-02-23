package com.seetech.footmassage2.core.configration.globalResponseWrap;



import com.seetech.footmassage2.core.configration.acessControl.AccessControl;
import com.seetech.footmassage2.core.configration.exceptionConfig.ExceptionConfig;
import com.seetech.footmassage2.core.configration.exceptionConfig.MyException;
import com.seetech.footmassage2.core.dto.res.BaseRes;
import com.seetech.footmassage2.core.util.JsonUtils;
import com.seetech.util.EmptyUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

/**
 * 重写返回体
 */
@Slf4j
@ControllerAdvice
@Priority(1)
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    public static final String[] swaggerUrlList = {"/swagger-resources/**", "/webjars/**", "/v3/**", "/swagger-ui/**", "/v2/**"};
    public static final PathMatcher matcher = new AntPathMatcher();

    @Value("${spring.profiles.active}")
    private String productActive;

    private Function<MethodParameter, Boolean> supportHandle;


    @PostConstruct
    public void initial() {
        if ("dev".equals(productActive)) {
            //测试环境
            supportHandle = (MethodParameter returnType) -> {
                boolean checkResult = this.acessControlCheck(returnType);
                if (checkResult) {

                    checkResult = this.urlWhitelistCheck();
                    return !checkResult;
                }
                return false;
            };
        } else {
            //生产环境
            supportHandle = this::acessControlCheck;
        }
    }

    /**
     * 是否请求 包含了 包装注解标记
     * 没有就直接返回，不需要重写返回体
     *
     * @param returnType    returnType
     * @param converterType converterType
     * @return boolean
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return supportHandle.apply(returnType);
    }


    @SneakyThrows
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

        //未被全局异常捕获的异常会被httpExceptionHandler处理，转化为Map
        if (body instanceof Exception) {
            if (body instanceof MyException) { //发生业务异常
                MyException myException = (MyException) body;
                BaseRes<Object> returnMessage = BaseRes.builder()
                        .message(myException.getMessage())
                        .status(ResultCodeEnum.UNKOWN_ERROR.code())
                        .data(ResultCodeEnum.UNKOWN_ERROR.message())
                        .build();
                //控制台输出错误
                ExceptionConfig.myExceptionLogHandler.info(myException.getMessage(), (Throwable) body);
                return returnMessage;
            }
            //发生非业务异常
            ExceptionConfig.myExceptionLogHandler.error(((Exception) body).getMessage(), (Throwable) body);
            return BaseRes.builder()
                    .message(((Exception) body).getMessage())
                    .status(ResultCodeEnum.UNKOWN_ERROR.code())
                    .data(ResultCodeEnum.UNKOWN_ERROR.message())
                    .build();
        }

        if (body instanceof String) {
            BaseRes<Object> returnMessage = BaseRes.builder()
                    .message(ResultCodeEnum.SUCCESS.message())
                    .status(ResultCodeEnum.SUCCESS.code())
                    .data(String.valueOf(body))
                    .build();
            return JsonUtils.toJson(returnMessage);
        }

        return BaseRes.builder()
                .message(ResultCodeEnum.SUCCESS.message())
                .status(ResultCodeEnum.SUCCESS.code())
                .data(body)
                .build();
    }

    /**
     * 返回true表示结果在白名单中
     *
     * @return
     */
    private boolean urlWhitelistCheck() {
        final ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert sra != null;
        final HttpServletRequest request = sra.getRequest();

        String url = request.getServletPath();
        for (String auth : swaggerUrlList) {

            if (matcher.match(auth, url)) {
                return true;
            }
        }
        return false;
    }

    private Boolean acessControlCheck(MethodParameter returnType) {
        AccessControl[] accessControls = returnType.getMethod().getAnnotationsByType(AccessControl.class);
        if (EmptyUtil.isNotEmpty(accessControls)) {
            return accessControls[0].responseWrap();
        }
        return true;
    }

}