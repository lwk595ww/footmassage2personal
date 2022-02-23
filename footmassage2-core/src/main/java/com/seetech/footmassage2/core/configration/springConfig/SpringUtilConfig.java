package com.seetech.footmassage2.core.configration.springConfig;

import com.seetech.footmassage2.core.util.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;


@Component
public class SpringUtilConfig {

    @Resource
    private ApplicationContext applicationContext;

    @PostConstruct
    public void setApplicationContext() {
        SpringUtils.applicationContext = applicationContext;
    }
}
