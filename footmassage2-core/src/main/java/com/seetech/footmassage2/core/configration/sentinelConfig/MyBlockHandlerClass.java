package com.seetech.footmassage2.core.configration.sentinelConfig;

import com.alibaba.cloud.sentinel.rest.SentinelClientHttpResponse;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.seetech.footmassage2.core.configration.globalResponseWrap.ResultCodeEnum;
import com.seetech.footmassage2.core.dto.res.BaseRes;
import com.seetech.footmassage2.core.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;

/**
 *  restTemplate Sentinel限制处理
 */
@Slf4j
public class MyBlockHandlerClass {

    /**
     * 流控处理
     *
     * @param request
     * @param body
     * @param execution
     * @param ex
     * @return
     */
    public static SentinelClientHttpResponse blockException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException ex) {
        System.err.println("Oops: " + ex.getClass().getCanonicalName());
        BaseRes baseRes = BaseRes.builder().status(ResultCodeEnum.NET_BLOCK_ERROR.code()).message(ResultCodeEnum.NET_BLOCK_ERROR.message()).build();
        return new SentinelClientHttpResponse(JsonUtils.toJson(baseRes));
    }

    /**
     * 降级处理
     *
     * @param request
     * @param body
     * @param execution
     * @param ex
     * @return
     */
    public static SentinelClientHttpResponse fallException(HttpRequest request, byte[] body, ClientHttpRequestExecution execution, BlockException ex) {
        System.err.println("Oops: " + ex.getClass().getCanonicalName());
        BaseRes baseRes = BaseRes.builder().status(ResultCodeEnum.OTHER_NET_ERROR.code()).message(ResultCodeEnum.OTHER_NET_ERROR.message()).build();
        return new SentinelClientHttpResponse(JsonUtils.toJson(baseRes));
    }


}
