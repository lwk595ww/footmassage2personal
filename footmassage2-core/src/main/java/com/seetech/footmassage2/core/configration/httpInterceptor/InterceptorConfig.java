package com.seetech.footmassage2.core.configration.httpInterceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @Description 拦截器配置类 将自定义拦截器加载到拦截器配置中
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private TenantIdInjectInterceptor tenantIdInjectInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tenantIdInjectInterceptor)
                .addPathPatterns("/**")//指定该类拦截的url
                .excludePathPatterns("/static/**", "/swagger-ui/**", "/swagger/**"
                        , "/swagger-resources/**", "/v2/**", "/v3/**", "/webjars/**",
                        "/swagger-ui/index.html");//过滤静态资源
    }

}
