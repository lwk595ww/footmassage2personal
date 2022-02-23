package com.seetech.footmassage2.core.configration.acessControl;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface AccessControl {
    /**
     * 访问是否需要指定tenantId
     */
    boolean needTenantId() default true;

    /**
     * 是否需要返回信息包装
     */
    boolean responseWrap() default true;
}
