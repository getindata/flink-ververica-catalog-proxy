package com.getindata.flink.catalog;

import org.apache.flink.configuration.ConfigOption;
import org.apache.flink.configuration.ConfigOptions;

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
}
