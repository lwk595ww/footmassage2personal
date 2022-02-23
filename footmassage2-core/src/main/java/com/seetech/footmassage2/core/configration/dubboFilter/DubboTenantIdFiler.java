package com.seetech.footmassage2.core.configration.dubboFilter;



import com.seetech.footmassage2.core.configration.acessControl.AccessControl;
import com.seetech.footmassage2.core.configration.consts.TenantConst;
import com.seetech.util.EmptyUtil;
import com.seetech.util.GlobalThreadLocalUtil;
import lombok.SneakyThrows;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.protocol.dubbo.DecodeableRpcInvocation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;


@Activate(group = {CommonConstants.PROVIDER})
public class DubboTenantIdFiler implements Filter {


    @SneakyThrows
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) {

        if (invocation instanceof DecodeableRpcInvocation) {
            DecodeableRpcInvocation rpcInvocation = (DecodeableRpcInvocation) invocation;
            String targetServiceUniqueName = rpcInvocation.getTargetServiceUniqueName();
            String methodName = rpcInvocation.getMethodName();
            Class<?>[] parameterTypes = rpcInvocation.getParameterTypes();

            //e.g:com.seetech.coreInterface.dubbo2.UserService:0.0.0
            String[] className = targetServiceUniqueName.split(":");
            Class<?> targetClass = Class.forName(className[0]);
            Method method = targetClass.getMethod(methodName, parameterTypes);

            AccessControl methodAnnotation = method.getAnnotation(AccessControl.class);
            if (EmptyUtil.isNotEmpty(methodAnnotation) && methodAnnotation.needTenantId() == false) {
                GlobalThreadLocalUtil.add(TenantConst.tenantIdFlag, false);
            } else {

                GlobalThreadLocalUtil.add(TenantConst.tenantIdFlag, true);
                Parameter[] parameters = method.getParameters();

                for (Parameter parameter : parameters) {
                    if (parameter.getName().equals(TenantConst.tenantIdSign)) {
                        GlobalThreadLocalUtil.add(TenantConst.tenantIdSign, parameter.getName());
                        break;
                    }
                }
            }
        }

        try {
            //执行本体链路
            Result result = invoker.invoke(invocation);
            return result;
        } finally {
            GlobalThreadLocalUtil.remove();
        }

    }
}
