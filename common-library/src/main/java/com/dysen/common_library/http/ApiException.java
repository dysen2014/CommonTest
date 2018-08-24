package com.dysen.common_library.http;

/**
 * dysen
 * 2018-08-18 11:20
 * dy.sen@qq.com
 */
public class ApiException extends RuntimeException {
    public ApiException(String detailMessage) {
        super(detailMessage);

    }
}

