package com.leonds.blog.domain.enums;

/**
 * @author Leon
 */
public enum CommentsStatus {
    ADD(1), PASS(2), UN_PASS(3);

    private int code;

    CommentsStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
