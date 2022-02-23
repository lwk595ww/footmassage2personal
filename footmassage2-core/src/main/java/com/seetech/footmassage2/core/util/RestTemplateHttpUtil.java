package com.seetech.footmassage2.core.util;//package com.seetech.util;


import com.seetech.util.EmptyUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


public class RestTemplateHttpUtil {

    private static RestTemplate restTemplate;

    public void setRestTemplate(RestTemplate restTemplate) {
        RestTemplateHttpUtil.restTemplate = restTemplate;
    }

    /**
     * restTemplate  GET 请求 需要带参数
     *
     * @param url    请求地址
     * @param tClass 返回对象
     * @param params 请求参数
     */
    public static <T> T getMethod(String url, Class<T> tClass, Map<String, Object> params) {
        //参数判断
        if (EmptyUtil.isEmpty(url)) {
            throw new RuntimeException("url不能为空！！");
        }
        if (!EmptyUtil.isEmpty(params)) {
            StringBuilder paramsUrl = new StringBuilder();
            int i = 0;
            for (String param : params.keySet()) {
                if (params.keySet().size() - 1 == i) { //最后一个处理结尾字符
                    paramsUrl.append(param);
                    paramsUrl.append("=");
                    paramsUrl.append(params.get(param));
                } else {
                    paramsUrl.append(param);
                    paramsUrl.append("=");
                    paramsUrl.append(params.get(param));
                    paramsUrl.append("&");
                }
                i++;
            }
            url = url + "&" + paramsUrl.toString();
        }

        ResponseEntity<String> mapEntity = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        });
        return JsonUtils.toObject(mapEntity.getBody(), tClass);
    }

    /**
     * restTemplate  GET 请求 不需要带参数
     *
     * @param url    请求地址
     * @param tClass 返回对象
     */
    public static <T> T getMethod(String url, Class<T> tClass) {
        //参数判断
        if (EmptyUtil.isEmpty(url)) {
            throw new RuntimeException("url不能为空！！");
        }

        ResponseEntity<String> mapEntity = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
        });

        return JsonUtils.toObject(mapEntity.getBody(), tClass);
    }


    /**
     * restTemplate POST 请求 请求头不需要带参数的
     *
     * @param url    请求地址
     * @param tClass 返回对象
     * @param params 参数 （最好是采用 LinkedHashMap）
     * @param <T>
     * @return
     */
    public static <T> T postMethod(String url, Class<T> tClass, Map<String, Object> params) {
        //参数判断
        if (EmptyUtil.isEmpty(url)) {
            throw new RuntimeException("url不能为空！！");
        }
        //请求头数据
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        //将参数变成json字符串
        String paramsJson = JsonUtils.toJson(params);
        HttpEntity<String> formEntity = new HttpEntity<>(paramsJson, headers);
        //发起请求
        String result = restTemplate.postForObject(url, formEntity, String.class);

        return JsonUtils.toObject(result, tClass);
    }

    /**
     * restTemplate POST 请求 请求头带参数的
     *
     * @param url        请求地址
     * @param tClass     返回对象
     * @param headParams 请求头参数
     * @param params     参数体
     * @param <T>
     * @return
     */
    public static <T> T postMethod(String url, Class<T> tClass, Map<String, Object> headParams, Map<String, Object> params) {
        //参数判断
        if (EmptyUtil.isEmpty(url)) {
            throw new RuntimeException("url不能为空！！");
        }
        //请求头数据
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        for (String headParam : headParams.keySet()) {
            headers.add(headParam, headParams.get(headParam).toString());
        }
        //将参数变成json字符串
        String paramsJson = JsonUtils.toJson(params);
        HttpEntity<String> formEntity = new HttpEntity<>(paramsJson, headers);
        //发起请求
        String result = restTemplate.postForObject(url, formEntity, String.class);

        return JsonUtils.toObject(result, tClass);
    }


}
