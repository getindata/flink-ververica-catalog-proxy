package com.getindata.flink.catalog.httpclient;

import java.util.Map;

import com.getindata.flink.catalog.VervericaCatalog;


/**
 * An HTTP client that is used by {@link VervericaCatalog} to
 * communicate with Ververica API using HTTP.
 */
public interface CatalogHttpClient {

    /**
     * @param url    Base URL (Ververica URL + path)
     * @param action Ververica API action
     * @param params A map that represents query parameters. The map key represents a parameter's name and map value represents parameter's value.
     * @return Response with status code & body
     */
    CatalogHttpClientResponse send(String url, String action, Map<String, String> params);
}
