package com.ivoyant.GlobalScheduler.Model;
public enum ExceptionMapper {
    ResourceAccessException(400);

    private int statusCode;

    ExceptionMapper(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}