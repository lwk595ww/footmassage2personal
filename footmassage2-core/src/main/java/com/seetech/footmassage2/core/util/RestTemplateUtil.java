package com.seetech.footmassage2.core.util;


import com.seetech.footmassage2.core.dto.res.BaseRes;
import com.seetech.util.EmptyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  主要适用于调用本系统的微服务模块(内部体系) (其他第三方调用请自行用官方API)
 */
public class RestTemplateUtil {

    private static RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        RestTemplateUtil.restTemplate = restTemplate;
    }


    /**
     * @param url       请求路径
     * @param method    请求方式
     * @param parmarMap 当 HttpMethod.GET 时？后面的参数用 Map 来传递，非Get请求则为null
     * @param tClass    返回什么类型数据
     * @param headerMap 请求头数据
     * @param objReq    请求参数
     * @param <T>
     * @return
     */
    public static <T> BaseRes exchangeXr(String url, HttpMethod method, Class<T> tClass, Map<String, String> headerMap, Map<String, Object> parmarMap, Object objReq) throws Exception {
        if (StringUtils.isBlank(url)) {
            throw new Exception("请求路径不能为空！！(必填)");
        }
        if (method.equals(HttpMethod.GET)) {
            if (EmptyUtil.isNotEmpty(parmarMap)) {
                //请求路径
                url = url + parmarSplit(parmarMap);
            }
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json"));
        if (EmptyUtil.isNotEmpty(headerMap)) {
            for (String s : headerMap.keySet()) {
                headers.set(s, headerMap.get(s));
            }
        }

        //HttpEntity 参数 第一个参数：请求参数 第二个参数：请求头
        ResponseEntity<BaseRes> responseEntity = restTemplate
                .exchange(url, method, new HttpEntity<>(objReq, headers), new ParameterizedTypeReference<BaseRes>() {
                });


        return responseEntity.getBody();
    }


    /**
     * 拼接参数
     *
     * @param map 参数合集
     * @return
     */
    private static String parmarSplit(Map<String, Object> map) {
        if (EmptyUtil.isNotEmpty(map)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("?");

            List<String> list = new ArrayList<>();
            for (Object s : map.keySet()) {
                String str = s + "=" + map.get(s);
                list.add(str);
            }
            stringBuilder.append(String.join("&", list));

            return stringBuilder.toString();
        }
        return null;
    }

}
