package com.seetech.footmassage2.core.configration.redisConfig;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

public class MyRedisJacksonConfig extends ObjectMapper {
    private static final long serialVersionUID = 1L;

    /*获取实例*/
    public static ObjectMapper getInstance() {
        return StaticSingletonHolder.instance;
    }

    /* 一个私有的静态内部类，用于初始化一个静态final实例*/
    private static class StaticSingletonHolder {
        private static final ObjectMapper instance = new MyRedisJacksonConfig();
    }

    private MyRedisJacksonConfig() {
        super();
        // 使用@JsonSerialize注解的解析
        this.configure(MapperFeature.USE_ANNOTATIONS, true)
                //在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                //缺少id或class类转换失败不报错
                .configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false)
                // 将类型序列化到属性json字符串中
                // this.enableDefaultTyping(DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
                .activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY)
                // 不包含任何属性的bean也不报错
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true)
                // 只针对非空的值进行序列化
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .findAndRegisterModules();
    }

}
