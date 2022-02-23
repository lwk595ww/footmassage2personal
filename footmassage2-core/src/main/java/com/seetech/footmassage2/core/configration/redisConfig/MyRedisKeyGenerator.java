package com.seetech.footmassage2.core.configration.redisConfig;


import lombok.SneakyThrows;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MyRedisKeyGenerator implements KeyGenerator {

    // custom cache key
    public static final int NO_PARAM_KEY = 0;
    public static final int NULL_PARAM_KEY = 53;

    /**
     * @param target
     * @param method 为aop代理method对象
     * @param params
     * @return
     * @Memo 必须带tenantId参数
     */
    @SneakyThrows
    @Override
    public Object generate(Object target, Method method, Object... params) {

        StringBuilder keyStringBuffer = new StringBuilder();

        keyStringBuffer.append(target.getClass().getSimpleName())
                .append(":")
                .append(method.getName());

        if (params.length == 0) {

            keyStringBuffer.append(NO_PARAM_KEY);

        } else {

            if (params.length == 1) {

                Object param = params[0];

                if (param == null) {
                    keyStringBuffer.append(NULL_PARAM_KEY);
                } else if (!param.getClass().isArray()) {
                    keyStringBuffer.append(":").append(param);
                }

            } else {
                //注意这个调用的deepHashCode，可能会有在不同机器产生不同hash的情况
                keyStringBuffer.append(":").append(Arrays.deepHashCode(params));
            }
        }
        return keyStringBuffer.toString();
    }
}
