package com.seetech.footmassage2.core.configration.httpInterceptor;



import com.seetech.footmassage2.core.configration.acessControl.AccessControl;
import com.seetech.footmassage2.core.configration.consts.TenantConst;
import com.seetech.util.EmptyUtil;
import com.seetech.util.GlobalThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Description http 全局拦截器 当前只是处理 ThreadLoca 用来处理租户
 */
@Slf4j
@Component
public class TenantIdInjectInterceptor implements HandlerInterceptor {

    /**
     * 请求处理之前调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //String tenantId = request.getHeader("tenantId");		// 点钟宝账号
        String tenantId = request.getParameter("tenantId");

        if (EmptyUtil.isNotEmpty(tenantId)) {
            GlobalThreadLocalUtil.add("tenantId", tenantId);
        }

//        throw new BussinessException("222");

        log.info("进入拦截器，拦截http请求！！当前租户：" + tenantId);


        if (handler instanceof HandlerMethod) {
            final HandlerMethod handlerMethod = (HandlerMethod) handler;

            AccessControl methodAnnotation = handlerMethod.getMethodAnnotation(AccessControl.class);
            if (EmptyUtil.isNotEmpty(methodAnnotation)) {
                GlobalThreadLocalUtil.add(TenantConst.tenantIdFlag, methodAnnotation.needTenantId());
            } else {
                GlobalThreadLocalUtil.add(TenantConst.tenantIdFlag, true);
            }
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("删除ThreadLocal");
        GlobalThreadLocalUtil.remove();

    }

    /**
     * 请求处理之后调用
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }


}
