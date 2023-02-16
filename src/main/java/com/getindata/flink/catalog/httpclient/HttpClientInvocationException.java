package com.getindata.flink.catalog.httpclient;

class HttpClientInvocationException extends RuntimeException {

    HttpClientInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
