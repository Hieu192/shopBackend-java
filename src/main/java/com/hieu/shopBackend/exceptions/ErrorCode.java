package com.hieu.shopBackend.exceptions;


import com.hieu.shopBackend.utils.MessageKeys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, MessageKeys.APP_UNCATEGORIZED_500, HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, MessageKeys.APP_AUTHORIZATION_403, HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, MessageKeys.USER_EXISTED, HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, MessageKeys.USER_ID_REQUIRED, HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, MessageKeys.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, MessageKeys.USER_EXISTED, HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, MessageKeys.APP_AUTHORIZATION_403, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, MessageKeys.APP_AUTHORIZATION_403, HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    PHONE_EXISTED(1009, MessageKeys.PHONE_NUMBER_EXISTED, HttpStatus.BAD_REQUEST),
    ROLE_EXISTED(1010, MessageKeys.ROLE_NAME_EXISTED, HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1011, MessageKeys.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_REQUIRED(1012, MessageKeys.PHONE_NUMBER_REQUIRED, HttpStatus.BAD_REQUEST),
    ROLE_ID_REQUIRED(1013, MessageKeys.ROLE_ID_REQUIRED, HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

}
