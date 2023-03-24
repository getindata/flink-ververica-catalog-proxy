package com.getindata.flink.catalog.httpclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;


/**
 * This implementation uses Java's 11 Http Client and supports only HTTP traffic (not HTTPS).
 */
@Slf4j
@AllArgsConstructor
public final class JavaNetCatalogHttpClient implements CatalogHttpClient {

    private final HttpClient httpClient;

    private final String[] headersAndValues;

    // TODO throw out
    public JavaNetCatalogHttpClient(Properties properties) {
        this.httpClient = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        this.headersAndValues = properties.entrySet().stream()
                .flatMap(property -> Stream.of(property.getKey(), property.getValue()))
                .toArray(String[]::new);
    }

    @Override
    public CatalogHttpClientResponse send(String url, String action, Map<String, String> params) {
        try {
            HttpRequest request = buildHttpRequest(buildUri(url, action, params));
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return new CatalogHttpClientResponse(response.statusCode(), response.body());
        } catch (Exception e) {
            log.error("Error sending request", e);
            throw new HttpClientInvocationException("Error when invoking http service.", e);
        }
    }

    private HttpRequest buildHttpRequest(URI uri) {
        HttpRequest.Builder requestBuilder = HttpRequest
                .newBuilder()
                .GET()
                .uri(uri);

        if (headersAndValues.length != 0) {
            requestBuilder.headers(headersAndValues);
        }

        return requestBuilder.build();
    }

    private URI buildUri(String url, String action, Map<String, String> params) throws URISyntaxException {
        List<NameValuePair> paramList = params
                .entrySet()
                .stream()
                .map(pair -> new BasicNameValuePair(pair.getKey(), pair.getValue()))
                .collect(Collectors.toList());
        return new URIBuilder(String.format("%s:%s", url, action))
                .setParameters(paramList)
                .build();
    }
}
