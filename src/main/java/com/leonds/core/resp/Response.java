package com.leonds.core.resp;

import java.io.Serializable;

/**
 * 控制器与前端交互的数据格式定义
 *
 * @author Leon
 */
public abstract class Response implements Serializable {

    private static final long serialVersionUID = -2425760660939074935L;

    public abstract int getStatus();

    public abstract Object getData();

    public abstract String getMessage();


    public static Response.ResponseBuilder status(Response.StatusType status) {
        return Response.ResponseBuilder.newInstance().status(status);
    }

    public static Response.ResponseBuilder status(Response.Status status) {
        return status((Response.StatusType) status);
    }

    public static Response.ResponseBuilder data(Object data) {
        return Response.ResponseBuilder.newInstance().data(data);
    }

    public static Response.ResponseBuilder message(String message) {
        return Response.ResponseBuilder.newInstance().message(message);
    }

    public static Response.ResponseBuilder ok() {
        return status(Response.Status.OK);
    }

    public static Response.ResponseBuilder ok(Object data) {
        return ok().data(data);
    }

    public static Response.ResponseBuilder err() {
        return status(Response.Status.ERR).message(Response.Status.ERR.getReason());
    }

    public static Response.ResponseBuilder err(String message) {
        return status(Response.Status.ERR).message(message);
    }

    public static Response.ResponseBuilder err(Response.Status status) {
        return status(status).message(status.getReason());
    }

    public static Response.ResponseBuilder err(Response.Status status, String message) {
        return status(status).message(message);
    }

    public static Response.ResponseBuilder errBiz(String message) {
        return status(Response.Status.BIZ).message(message);
    }


    public abstract static class ResponseBuilder {
        protected ResponseBuilder() {
        }

        public static Response.ResponseBuilder newInstance() {
            return new ResponseBuilderImpl();
        }

        public abstract Response build();

        public abstract Response.ResponseBuilder clone();

        public abstract Response.ResponseBuilder status(int statusCode);

        public Response.ResponseBuilder status(Response.StatusType statusType) {
            if (statusType == null) {
                throw new IllegalArgumentException();
            } else {
                return this.status(statusType.getCode());
            }
        }

        public Response.ResponseBuilder status(Response.Status status) {
            return this.status(status);
        }

        public abstract Response.ResponseBuilder data(Object data);

        public abstract Response.ResponseBuilder message(String message);

    }

    public enum Status implements StatusType {
        OK(0, "Success"),
        ERR(1, "Error"),
        BIZ(101, "BusinessError");

        private final int code;
        private final String reason;

        Status(int code, String reason) {
            this.code = code;
            this.reason = reason;
        }

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public String getReason() {
            return this.toString();
        }

        @Override
        public String toString() {
            return this.reason;
        }

    }

    public interface StatusType {
        int getCode();

        String getReason();
    }

}
