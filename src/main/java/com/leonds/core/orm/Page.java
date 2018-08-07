package com.leonds.core.orm;

import java.util.List;

/**
 * 分页
 *
 * @author Leon
 */
public interface Page<T> {
    /**
     * 获取记录集合
     *
     * @return 集合
     */
    List<T> getRows();

    /**
     * 获取当前第几页
     *
     * @return 页码
     */
    int getPage();

    /**
     * 获取分页条数
     *
     * @return 条数
     */
    int getSize();

    /**
     * 获取总记录条数
     *
     * @return 总记录条数
     */
    long getTotal();

    /**
     * 获取总页数
     *
     * @return 总页数
     */
    int getTotalPages();
}
