package com.seetech.footmassage2.core.configration.mybatisConfig;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;

import com.seetech.footmassage2.core.configration.mybatisConfig.methods.MyLogicSqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * mybatis_plus核心配置
 */
@Configuration
@EnableTransactionManagement//管理事务
@MapperScan({"com.seetech.footmassage2.core.dao"})
public class MybatisPlusConfig {

    /**
     * 新多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(MyTenantHandler myTenantHandler) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(myTenantHandler));
        //加入乐观锁的支持
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        //分页插件 (基于二级缓存 不可用这种形式注入分页插件 会导致异常 主要产生的原因是：select count(*) 会出现0)
        //interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }


    /**
     * 自定义 SqlInjector
     * 里面包含自定义的全局方法
     */
    @Bean
    public MyLogicSqlInjector myLogicSqlInjector() {
        return new MyLogicSqlInjector();
    }


    /**
     * 如果用二级缓存就采用这种形式 注入分页插件
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }


}
