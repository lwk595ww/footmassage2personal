package com.seetech.footmassage2.core.util;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;

/**
 *  拼接排序字段 (通用于分页查询)
 */
public class IsOrderBy {

    /**
     * @param orderBy 当前前端传递的排序字段
     */
    public static <T> void isOrder(String orderBy, Page<T> page) {

        OrderItem orderItem = new OrderItem();

        if (!StringUtils.isBlank(orderBy)) {
            boolean flag = true;
            //按照空格分隔数据
            String[] s = orderBy.split(" ");
            if ("desc".equals(s[s.length - 1])) {
                flag = false;
            }
            //看是正序还是倒序
            orderItem.setAsc(flag);
            //需要排序的字符
            orderItem.setColumn(s[0]);
            page.addOrder(orderItem);
        }
    }

}
