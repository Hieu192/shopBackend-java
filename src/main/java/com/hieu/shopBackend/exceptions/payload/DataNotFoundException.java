package com.hieu.shopBackend.exceptions.payload;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }
}
