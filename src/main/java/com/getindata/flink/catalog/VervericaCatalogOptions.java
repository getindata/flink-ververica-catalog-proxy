package com.getindata.flink.catalog;

import java.net.http.HttpClient;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

import com.getindata.flink.catalog.httpclient.JavaNetHttpClientFactory;

public class VervericaCatalogOptions {

    public static final String IDENTIFIER = "ververica";


    /**
     * A property prefix for proxy.
     */
    public static final String GID_PROXY = "gid.vvp.proxy.";

    public static final String URL = GID_PROXY + "url";

    public static final String NAMESPACE = GID_PROXY + "namespace";

    public static final String CATALOG = GID_PROXY + "catalog";

    public static final String HEADERS = GID_PROXY + "headers";

    public static final String FOLLOW_REDIRECTS = GID_PROXY + "followRedirects";

    /* -------------- HTTPS security settings -------------- */

    public static final String ALLOW_SELF_SIGNED = GID_PROXY + "security.cert.server.allowSelfSigned";

    public static final String SERVER_TRUSTED_CERT = GID_PROXY + "security.cert.server";

    /**
     * Ververica URL
     */
    public static final ConfigOption<String> VERVERICA_URL =
            ConfigOptions.key(URL)
                    .stringType()
                    .noDefaultValue()
                    .withDescription("Ververica URL");

    /**
     * Ververica namespace
     */
    public static final ConfigOption<String> VERVERICA_NAMESPACE =
            ConfigOptions.key(NAMESPACE)
                    .stringType()
                    .defaultValue("default")
                    .withDescription("Ververica namespace");

    /**
     * Catalog name in ververica
     */
    public static final ConfigOption<String> VERVERICA_CATALOG =
            ConfigOptions.key(CATALOG)
                    .stringType()
                    .defaultValue("vvp")
                    .withDescription("Catalog name in ververica");


    public static final ConfigOption<String> HTTP_PROXY_HEADERS =
            ConfigOptions.key(HEADERS)
                    .stringType()
                    .noDefaultValue()
                    .withDescription("Headers for http(s) call to ververica. Arrays of key and values using '"
                            + JavaNetHttpClientFactory.PROP_DELIM + "' as delimiter");

    public static final ConfigOption<Boolean> HTTP_ALLOW_SELF_SIGNED =
            ConfigOptions.key(ALLOW_SELF_SIGNED)
                    .booleanType()
                    .noDefaultValue()
                    .withDescription("Allow self signed certificate");

    public static final ConfigOption<String> HTTP_SERVER_TRUSTED_CERT =
            ConfigOptions.key(SERVER_TRUSTED_CERT)
                    .stringType()
                    .noDefaultValue()
                    .withDescription("Path for server .crt file.");

    public static final ConfigOption<HttpClient.Redirect> HTTP_FOLLOW_REDIRECTS =
            ConfigOptions.key(FOLLOW_REDIRECTS)
                    .enumType(HttpClient.Redirect.class)
                    .defaultValue(HttpClient.Redirect.NORMAL)
                    .withDescription("Follow redirects option for http client.");
}
