package com.getindata.flink.catalog.httpclient;

import lombok.Value;

@Value
public class CatalogHttpClientResponse {

    int statusCode;

    String body;

}
