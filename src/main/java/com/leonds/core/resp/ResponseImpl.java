package com.leonds.core.resp;

import java.io.Serializable;

/**
 * @author Leon
 */
public class ResponseImpl extends Response implements Serializable {

    private static final long serialVersionUID = -5558577507694126573L;
    private final int status;
    private final Object data;
    private final String message;

    public ResponseImpl(int status, Object data, String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public Object getData() {
        return this.data;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
