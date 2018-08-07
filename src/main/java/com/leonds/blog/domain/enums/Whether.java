package com.leonds.blog.domain.enums;

/**
 * @author Leon
 */
public enum Whether {
    YES(1), NO(2);

    private int code;

    Whether(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
