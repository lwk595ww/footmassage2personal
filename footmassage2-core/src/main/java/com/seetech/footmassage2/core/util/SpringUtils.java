package com.seetech.footmassage2.core.util;///*

import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Optional;


/**
 * Utils - Spring
 */
//@Component("springUtils")
//@Lazy(false)
public final class SpringUtils implements ApplicationContextAware, DisposableBean {

    /**
     * applicationContext
     */
    public static ApplicationContext applicationContext;

    /**
     * 不可实例化
     */
    private SpringUtils() {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringUtils.applicationContext = applicationContext;
    }

    @Override
    public void destroy() throws Exception {
        applicationContext = null;
    }

    /**
     * 获取applicationContext
     *
     * @return applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取实例
     *
     * @param name Bean名称
     * @return 实例
     */
    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 获取bean名称
     *
     * @param beanClass
     * @return
     */
    public static String[] getBeanNames(Class<?> beanClass) {
        return applicationContext.getBeanNamesForType(beanClass);
    }

    /**
     * 获取实例
     *
     * @param name Bean名称
     * @param type Bean类型
     * @return 实例
     */
    public static <T> T getBean(String name, Class<T> type) {
        return applicationContext.getBean(name, type);
    }

//    /**
//     * 获取国际化消息
//     *
//     * @param code 代码
//     * @param args 参数
//     * @return 国际化消息
//     */
//    public static String getMessage(String code, Object... args) {
//        LocaleResolver localeResolver = getBean("localeResolver", LocaleResolver.class);
//        Locale locale = localeResolver.resolveLocale(null);
//        return applicationContext.getMessage(code, args, locale);
//    }

    public static <T> T getBean(Class<T> type) {
        return applicationContext.getBean(type);
    }

    public static Optional<ApplicationContext> tryGetApplicationContext() {
        return Optional.ofNullable(applicationContext);
    }


}