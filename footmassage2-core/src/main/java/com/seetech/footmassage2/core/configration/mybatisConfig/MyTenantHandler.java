package com.seetech.footmassage2.core.configration.mybatisConfig;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.seetech.footmassage2.core.configration.consts.TenantConst;
import com.seetech.footmassage2.core.configration.exceptionConfig.MyException;
import com.seetech.util.EmptyUtil;
import com.seetech.util.GlobalThreadLocalUtil;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import org.springframework.stereotype.Component;


/**
 * 配置TenantId,表过滤,IdColumn
 */
@Component
public class MyTenantHandler implements TenantLineHandler {


    @Override
    public Expression getTenantId() {
        if (EmptyUtil.isEmpty(GlobalThreadLocalUtil.get(TenantConst.tenantIdSign))) {
            throw new MyException("当前没有传入租户！！");
        }
        return new StringValue((String) GlobalThreadLocalUtil.get(TenantConst.tenantIdSign));
    }

    //这个表示需要拼接的字段
    @Override
    public String getTenantIdColumn() {
        return "tenantId";
    }

    //这里可以过滤掉不需要拼接的表名
    @Override
    public boolean ignoreTable(String tableName) {
        return !(boolean) GlobalThreadLocalUtil.get(TenantConst.tenantIdFlag);
    }


}
