package com.getindata.flink.catalog.httpclient;

import java.util.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.flink.configuration.Configuration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class JavaNetCatalogHttpClientTest {

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void shouldInvokeHttpService() throws Exception {
        String contentTypeHeader = "application/json";
        String endpoint = "/base/path:listDatabases";
        String body = "{\"databases\": []}";

        wireMockServer.stubFor(any(urlPathEqualTo(endpoint))
                .withHeader("Content-Type", equalTo(contentTypeHeader))
                .willReturn(
                        aResponse().withHeader("Content-Type", contentTypeHeader)
                                .withStatus(200)
                                .withBody(body)));

        Map<String, String> vvpProperties = new HashMap<>();
        vvpProperties.put("gid.vvp.proxy.headers", "Content-Type," + contentTypeHeader);
        JavaNetCatalogHttpClient client = JavaNetHttpClientFactory.createClient(Configuration.fromMap(vvpProperties));

        var result = client.send(wireMockServer.baseUrl() + "/base/path", "listDatabases", Collections.emptyMap());

        assertEquals(new CatalogHttpClientResponse(200, body), result);

        var responses = wireMockServer.getAllServeEvents();
        assertTrue(responses.stream()
                .allMatch(response -> Objects.equals(response.getRequest().getUrl(), endpoint)));
        assertTrue(
                responses.stream().allMatch(response -> response.getResponse().getStatus() == 200));

    }

}
