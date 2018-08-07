package com.leonds.blog.domain.enums;

/**
 * @author Leon
 */
public enum SysUserStatus {
    NORMAL(1), DISABLE(2);

    private int code;

    SysUserStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
