package com.leonds.blog.domain.enums;

/**
 * @author Leon
 */
public enum PostsStatus {
    DRAFT(1), PUBLISH(2);

    private int code;

    PostsStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
