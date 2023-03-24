package com.getindata.flink.catalog;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

import com.getindata.flink.catalog.httpclient.HttpConfigConstants;

public class VervericaCatalogOptions {

    public static final String IDENTIFIER = "ververica";

    /**
     * Ververica URL
     */
    public static final ConfigOption<String> VERVERICA_URL =
            ConfigOptions.key("vvp-url")
                    .stringType()
                    .noDefaultValue()
                    .withDescription("Ververica URL");

    /**
     * Ververica namespace
     */
    public static final ConfigOption<String> VERVERICA_NAMESPACE =
            ConfigOptions.key("vvp-namespace")
                    .stringType()
                    .defaultValue("default")
                    .withDescription("Ververica namespace");

    /**
     * Catalog name in ververica
     */
    public static final ConfigOption<String> VERVERICA_CATALOG =
            ConfigOptions.key("vvp-catalog")
                    .stringType()
                    .defaultValue("vvp")
                    .withDescription("Catalog name in ververica");


    public static final ConfigOption<String> HTTP_PROXY_HEADERS =
            ConfigOptions.key(HttpConfigConstants.HEADERS)
                    .stringType()
                    .noDefaultValue()
                    .withDescription("Headers for http call to ververica. Using '" + HttpConfigConstants.PROP_DELIM + "' as delimiter");

    public static final ConfigOption<Boolean> HTTP_ALLOW_SELF_SIGNED =
            ConfigOptions.key(HttpConfigConstants.ALLOW_SELF_SIGNED)
                    .booleanType()
                    .noDefaultValue()
                    .withDescription("'");

    public static final ConfigOption<String> HTTP_SERVER_TRUSTED_CERT =
            ConfigOptions.key(HttpConfigConstants.SERVER_TRUSTED_CERT)
                    .stringType()
                    .noDefaultValue()
                    .withDescription("'");

    public static final ConfigOption<String> HTTP_KEY_STORE_PATH =
            ConfigOptions.key(HttpConfigConstants.KEY_STORE_PATH)
                    .stringType()
                    .noDefaultValue()
                    .withDescription("'");

    public static final ConfigOption<String> HTTP_KEY_STORE_PASSWORD =
            ConfigOptions.key(HttpConfigConstants.KEY_STORE_PASSWORD)
                    .stringType()
                    .noDefaultValue()
                    .withDescription("'");

    public static final ConfigOption<String> HTTP_CLIENT_PRIVATE_KEY =
            ConfigOptions.key(HttpConfigConstants.CLIENT_PRIVATE_KEY)
                    .stringType()
                    .noDefaultValue()
                    .withDescription("'");

    public static final ConfigOption<String> HTTP_CLIENT_CERT =
            ConfigOptions.key(HttpConfigConstants.CLIENT_CERT)
                    .stringType()
                    .noDefaultValue()
                    .withDescription("'");
}
