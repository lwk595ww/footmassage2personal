package com.seetech.footmassage2.core.configration.sentinelConfig;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.seetech.footmassage2.core.dto.res.BaseRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义返回限流后响应结果
 */
@Slf4j
@Component
public class MyBlockExceptionHandler implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, BlockException e) throws Exception {
        BaseRes baseResFail = null;
//        //不同的异常返回不同的提示语
//        if (e instanceof FlowException){
//            baseResponseFail = DtoConvertUtil.getBaseResponseFail(401, "接口限流了！");
//        } else if (e instanceof DegradeException){
//            baseResponseFail = DtoConvertUtil.getBaseResponseFail(401,"接口降级了！");
//        }else if (e instanceof ParamFlowException){
//            baseResponseFail = DtoConvertUtil.getBaseResponseFail(401,"热点参数限流了！");
//        }else if (e instanceof SystemBlockException){
//            baseResponseFail = DtoConvertUtil.getBaseResponseFail(401,"触发系统保护规则！");
//        }else if (e instanceof AuthorityException){
//            baseResponseFail = DtoConvertUtil.getBaseResponseFail(401,"授权规则不通过");
//        }

        response.setStatus(401);
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().print(mapper.writeValueAsString(baseResFail));
    }
}
