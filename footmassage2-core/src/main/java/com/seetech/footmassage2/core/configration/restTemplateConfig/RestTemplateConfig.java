package com.seetech.footmassage2.core.configration.restTemplateConfig;

import com.alibaba.cloud.sentinel.annotation.SentinelRestTemplate;
import com.seetech.footmassage2.core.configration.sentinelConfig.MyBlockHandlerClass;
import com.seetech.footmassage2.core.util.RestTemplateHttpUtil;
import com.seetech.footmassage2.core.util.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 *  restTemplate 设置埋点监控
 */
@Configuration
public class RestTemplateConfig {


    @Bean(name = "restTemplateHttp")
    public RestTemplate restTemplateHttp() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //设置连接超时时间 // in milliseconds
        factory.setConnectTimeout(600000);
        //设置读取超时时间
        factory.setReadTimeout(20000);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        /**
         * 此配置是 微信登录返回的请求头并不是 json格式 而是 TEXT_PLAIN 配置让RestTemplate来兼容此类型
         */
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.TEXT_PLAIN);
        mediaTypes.add(MediaType.TEXT_HTML);
        mediaTypes.add(MediaType.ALL);
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(mediaTypes);

        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);

        return restTemplate;
    }

    @Bean
    public RestTemplateUtil restTemplateUtil(@Qualifier("restTemplateHttp") RestTemplate restTemplate) {
        //将当前 RestTemplate 实例注入到 RestTemplateUtil中
        RestTemplateUtil restTemplateUtil = new RestTemplateUtil();
        restTemplateUtil.setRestTemplate(restTemplate);
        return restTemplateUtil;
    }

    @Bean
    public RestTemplateHttpUtil restTemplateHttpUtil(@Qualifier("restTemplateHttp") RestTemplate restTemplate) {
        RestTemplateHttpUtil restTemplateHttpUtil = new RestTemplateHttpUtil();
        restTemplateHttpUtil.setRestTemplate(restTemplate);
        return restTemplateHttpUtil;
    }

    //    //增加对restTemplate的埋点
    @SentinelRestTemplate(blockHandlerClass = MyBlockHandlerClass.class, blockHandler = "blockException",
            fallbackClass = MyBlockHandlerClass.class, fallback = "fallException")
    @LoadBalanced
    @Bean(name = "restTemplatecloud")
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //设置连接超时时间
        factory.setConnectTimeout(600000);
        //设置读取超时时间
        factory.setReadTimeout(20000);

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }
}
