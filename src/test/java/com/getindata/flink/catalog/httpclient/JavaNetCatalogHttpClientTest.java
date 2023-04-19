package com.getindata.flink.catalog.httpclient;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.flink.configuration.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.getindata.flink.catalog.VervericaCatalogOptions;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class JavaNetCatalogHttpClientTest {

    private static final File SERVER_CRT = new File("src/test/resources/security/server.crt");
    private static File KEYSTORE_FILE = new File("src/test/resources/security/serverKeyStore.jks");

    private WireMockServer wireMockServer;

    @AfterEach
    public void tearDown() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    public void shouldInvokeHttpService() throws Exception {
        String contentTypeHeader = "application/json";
        String endpoint = "/base/path:listDatabases";
        String body = "{\"databases\": []}";

        wireMockServer = new WireMockServer();
        wireMockServer.stubFor(any(urlPathEqualTo(endpoint))
                .withHeader("Content-Type", equalTo(contentTypeHeader))
                .willReturn(
                        aResponse().withHeader("Content-Type", contentTypeHeader)
                                .withStatus(200)
                                .withBody(body)));
        wireMockServer.start();

        Map<String, String> vvpProperties = new HashMap<>();
        vvpProperties.put(VervericaCatalogOptions.HEADERS, "Content-Type," + contentTypeHeader);
        vvpProperties.put(VervericaCatalogOptions.FOLLOW_REDIRECTS, "ALWAYS");

        invokeAndAssertResult(endpoint, body, vvpProperties);
    }

    @Test
    public void shouldInvokeHttpServiceWithSSL() throws Exception {
        String contentTypeHeader = "application/json";
        String endpoint = "/base/path:listDatabases";
        String body = "{\"databases\": []}";

        wireMockServer = new WireMockServer(options()
                .dynamicHttpsPort()
                .httpDisabled(true)
                .keystorePath(KEYSTORE_FILE.getAbsolutePath())
                .keystorePassword("password")
                .keyManagerPassword("password")
        );
        wireMockServer.stubFor(any(urlPathEqualTo(endpoint))
                .withHeader("Content-Type", equalTo(contentTypeHeader))
                .willReturn(
                        aResponse().withHeader("Content-Type", contentTypeHeader)
                                .withStatus(200)
                                .withBody(body)));
        wireMockServer.start();

        Map<String, String> vvpProperties = new HashMap<>();
        vvpProperties.put(VervericaCatalogOptions.SERVER_TRUSTED_CERT, SERVER_CRT.getAbsolutePath());
        vvpProperties.put(VervericaCatalogOptions.HEADERS, String.format("Content-Type,%s", contentTypeHeader));

        invokeAndAssertResult(endpoint, body, vvpProperties);
    }

    @Test
    public void shouldInvokeHttpServiceWithAuthorizationHeader() {
        String contentTypeHeader = "application/json";
        String endpoint = "/base/path:listDatabases";
        String body = "{\"databases\": []}";

        wireMockServer = new WireMockServer();
        wireMockServer.stubFor(any(urlPathEqualTo(endpoint))
                .withBasicAuth("user", "password")
                .withHeader("Content-Type", equalTo(contentTypeHeader))
                .willReturn(
                        aResponse().withHeader("Content-Type", contentTypeHeader)
                                .withStatus(200)
                                .withBody(body)));
        wireMockServer.start();

        Map<String, String> vvpProperties = new HashMap<>();
        vvpProperties.put(VervericaCatalogOptions.HEADERS,
                String.format("Content-Type,%s,Authorization,%s", contentTypeHeader, "Basic dXNlcjpwYXNzd29yZA=="));

        invokeAndAssertResult(endpoint, body, vvpProperties);
    }

    private void invokeAndAssertResult(String endpoint, String body, Map<String, String> vvpProperties) {
        JavaNetCatalogHttpClient client = JavaNetHttpClientFactory.createClient(Configuration.fromMap(vvpProperties));

        var result = client.send(wireMockServer.baseUrl() + "/base/path", "listDatabases",
                Collections.emptyMap());

        assertEquals(new CatalogHttpClientResponse(200, body), result);

        var responses = wireMockServer.getAllServeEvents();
        assertTrue(responses.stream()
                .allMatch(response -> Objects.equals(response.getRequest().getUrl(), endpoint)));
        assertTrue(
                responses.stream().allMatch(response -> response.getResponse().getStatus() == 200));
    }

}
