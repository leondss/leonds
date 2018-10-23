package com.leonds.core;


import com.leonds.core.orm.PageRequest;

/**
 * @author Leon
 */
public class QueryParams {
    private int page;
    private int size;
    private String orderBy;
    private String q;

    public PageRequest pageRequest() {
        int p = page <= 0 ? 0 : page;
        int s = size <= 0 ? 10 : size;
        return PageRequest.of(p, s, orderBy);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
