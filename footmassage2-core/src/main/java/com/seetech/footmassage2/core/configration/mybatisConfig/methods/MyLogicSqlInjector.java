package com.seetech.footmassage2.core.configration.mybatisConfig.methods;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;

import java.util.List;

public class MyLogicSqlInjector extends DefaultSqlInjector {

    /**
     * 如果只需增加方法，保留MP自带方法 只在方法库中扩展方法
     * 可以super.getMethodList() 再add
     *
     * @return
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        //保留原有的方法
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new deleteAll());
        methodList.add(new insertAllBatch());
        return methodList;
    }
}
