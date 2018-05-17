package com.leonds.core.resp;

/**
 * @author Leon
 */
public final class ResponseBuilderImpl extends Response.ResponseBuilder {

    private int status;
    private Object data;
    private String message;

    public ResponseBuilderImpl() {
    }


    public ResponseBuilderImpl(ResponseBuilderImpl that) {
        this.status = that.status;
        this.data = that.data;
        this.message = that.message;
    }

    @Override
    public Response build() {
        return new ResponseImpl(this.status, this.data, this.message);
    }

    @Override
    public Response.ResponseBuilder clone() {
        return new ResponseBuilderImpl(this);
    }

    @Override
    public Response.ResponseBuilder status(int status) {
        this.status = status;
        return this;
    }

    @Override
    public Response.ResponseBuilder data(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public Response.ResponseBuilder message(String message) {
        this.message = message;
        return this;
    }
}
