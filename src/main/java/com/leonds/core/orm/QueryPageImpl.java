package com.leonds.core.orm;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页对象
 *
 * @author Leon
 */
public class QueryPageImpl<T> implements Page<T>, Serializable {

    private static final long serialVersionUID = -1343296556002553510L;
    private final List<T> rows;

    /**
     * 当前页
     */
    private final int page;

    /**
     * 每页显示条数
     */
    private final int size;

    /**
     * 总条数
     */
    private final long total;

    public QueryPageImpl(int page, int size, long total, List<T> rows) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.rows = rows;
    }

    public QueryPageImpl(int page, int size) {
        this(page, size, 0, null);
    }

    public List<T> getRows() {
        return rows == null ? Collections.emptyList() : rows;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotal() {
        return this.total;
    }

    public int getTotalPages() {
        return (int) (total / (long) getSize()) + (total % (long) getSize() > 0L ? 1 : 0);
    }

}
