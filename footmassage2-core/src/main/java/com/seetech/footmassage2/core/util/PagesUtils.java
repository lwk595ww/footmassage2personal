package com.seetech.footmassage2.core.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.seetech.util.EmptyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *  分页工具 (物理分页 )
 */
public class PagesUtils {

    public static <T> Page<T> getPages(Integer currentPage, Integer pageSize, List<T> list) {
        Page<T> page = new Page<T>();

        if (EmptyUtil.isEmpty(list)) {
            page.setCurrent(currentPage);
            page.setSize(pageSize);
            page.setRecords(new ArrayList<>());
            page.setTotal(0);
            return page;
        }

        int size = list.size();

        if (pageSize > size) {
            pageSize = size;
        }

        // 求出最大页数，防止currentPage越界
        int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;

        if (currentPage > maxPage) {
            currentPage = maxPage;
        }

        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;

        List<T> pageList = new ArrayList<>();

        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }

        page.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return page;
    }


    public static void main(String[] args) {
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        integers.add(5);

        Page<Integer> pages = getPages(1, 10, integers);
        System.out.println(pages.getRecords());
    }
}
