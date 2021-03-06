package com.sergeykotov.op.dao;

public enum ResultCode {
    SQLITE_CONSTRAINT(19, "Abort due to constraint violation");

    private final int code;
    private final String name;

    ResultCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return code + ": " + name;
    }
}