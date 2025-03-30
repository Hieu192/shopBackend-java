package com.hieu.shopBackend.exceptions.payload;

public class PermissionDenyException extends RuntimeException {
    public PermissionDenyException(String message) {
        super(message);
    }
}