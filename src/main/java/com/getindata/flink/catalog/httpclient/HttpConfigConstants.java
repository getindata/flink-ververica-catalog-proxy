package com.getindata.flink.catalog.httpclient;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class HttpConfigConstants {


    public static final String PROP_DELIM = ",";

    /**
     * A property prefix for http connector.
     */
    public static final String GID_PROXY_HTTP = "gid.proxy.http.";

    public static final String HEADERS = GID_PROXY_HTTP + "headers";

    /* -------------- HTTPS security settings -------------- */

    public static final String ALLOW_SELF_SIGNED = GID_PROXY_HTTP + "security.cert.server.allowSelfSigned";

    public static final String SERVER_TRUSTED_CERT = GID_PROXY_HTTP + "security.cert.server";

    public static final String CLIENT_CERT = GID_PROXY_HTTP + "security.cert.client";

    public static final String CLIENT_PRIVATE_KEY = GID_PROXY_HTTP + "security.key.client";

    public static final String KEY_STORE_PATH = GID_PROXY_HTTP
            + "security.keystore.path";

    public static final String KEY_STORE_PASSWORD = GID_PROXY_HTTP
            + "security.keystore.password";

    public static final String KEY_STORE_TYPE = GID_PROXY_HTTP
            + "security.keystore.type";


}
