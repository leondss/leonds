package com.leonds.core.orm;

/**
 * @author Leon
 */
public class PageRequest {
    private final int page;
    private final int size;
    private final String orderBy;

    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }

    public static PageRequest of(int page, int size, String orderBy) {
        return new PageRequest(page, size, orderBy);
    }

    private PageRequest(int page, int size) {
        this(page, size, null);
    }

    private PageRequest(int page, int size, String orderBy) {
        this.page = page;
        this.size = size;
        this.orderBy = orderBy;
    }

    public int getOffset() {
        return page * size;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
