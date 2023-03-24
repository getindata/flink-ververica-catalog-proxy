package com.getindata.flink.catalog.httpclient;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.ReadableConfig;
import org.apache.flink.util.StringUtils;

import com.getindata.flink.catalog.VervericaCatalogOptions;
import com.getindata.flink.catalog.httpclient.security.SecurityContext;
import com.getindata.flink.catalog.httpclient.security.SelfSignedTrustManager;


@Slf4j
@NoArgsConstructor(access = AccessLevel.NONE)
public class JavaNetHttpClientFactory {

    /**
     * Creates Java's {@link JavaNetCatalogHttpClient} instance that will be using default, JVM shared {@link
     * java.util.concurrent.ForkJoinPool} for async calls.
     *
     * @param options properties used to build {@link SSLContext}
     * @param options
     * @return new {@link HttpClient} instance.
     */
    public static JavaNetCatalogHttpClient createClient(ReadableConfig options) {

        SSLContext sslContext = getSslContext(options);

        HttpClient httpClient = HttpClient.newBuilder()
                .followRedirects(Redirect.NORMAL)
                .sslContext(sslContext)
                .build();

        String[] headers = options
                .getOptional(VervericaCatalogOptions.HTTP_PROXY_HEADERS).orElse("")
                .split(HttpConfigConstants.PROP_DELIM);

        return new JavaNetCatalogHttpClient(httpClient, headers);
    }

    /**
     * Creates an {@link SSLContext} based on provided properties.
     * <ul>
     * <li>{@link VervericaCatalogOptions#HTTP_ALLOW_SELF_SIGNED}</li>
     * <li>{@link VervericaCatalogOptions#HTTP_SERVER_TRUSTED_CERT}</li>
     * <li>{@link HttpConfigConstants#PROP_DELIM}</li>
     * </ul>
     *
     * @param options properties used to build {@link SSLContext}
     * @return new {@link SSLContext} instance.
     */
    private static SSLContext getSslContext(ReadableConfig options) {
        SecurityContext securityContext = createSecurityContext(options);

        boolean selfSignedCert =
                options.getOptional(VervericaCatalogOptions.HTTP_ALLOW_SELF_SIGNED).orElse(false);

        String[] serverTrustedCerts = options
                .getOptional(VervericaCatalogOptions.HTTP_SERVER_TRUSTED_CERT).orElse("")
                .split(HttpConfigConstants.PROP_DELIM);

        if (serverTrustedCerts.length > 0) {
            for (String cert : serverTrustedCerts) {
                if (!StringUtils.isNullOrWhitespaceOnly(cert)) {
                    securityContext.addCertToTrustStore(cert);
                }
            }
        }

        // NOTE TrustManagers must be created AFTER adding all certificates to KeyStore.
        TrustManager[] trustManagers = getTrustedManagers(securityContext, selfSignedCert);
        return securityContext.getSslContext(trustManagers);
    }

    private static TrustManager[] getTrustedManagers(
            SecurityContext securityContext,
            boolean selfSignedCert) {

        TrustManager[] trustManagers = securityContext.getTrustManagers();

        if (selfSignedCert) {
            return wrapWithSelfSignedManagers(trustManagers).toArray(new TrustManager[0]);
        } else {
            return trustManagers;
        }
    }

    private static List<TrustManager> wrapWithSelfSignedManagers(TrustManager[] trustManagers) {
        log.warn("Creating Trust Managers for self-signed certificates - not Recommended. "
                + "Use [" + VervericaCatalogOptions.HTTP_SERVER_TRUSTED_CERT + "] "
                + "proxy property to add certificated as trusted.");

        List<TrustManager> selfSignedManagers = new ArrayList<>(trustManagers.length);
        for (TrustManager trustManager : trustManagers) {
            selfSignedManagers.add(new SelfSignedTrustManager((X509TrustManager) trustManager));
        }
        return selfSignedManagers;
    }

    /**
     * Creates a {@link SecurityContext} with empty {@link java.security.KeyStore} or loaded from
     * file.
     *
     * @param options Properties for creating {@link SecurityContext}
     * @return new {@link SecurityContext} instance.
     */
    private static SecurityContext createSecurityContext(ReadableConfig options) {

        String keyStorePath =
                options.getOptional(VervericaCatalogOptions.HTTP_KEY_STORE_PATH).orElse("");

        if (StringUtils.isNullOrWhitespaceOnly(keyStorePath)) {
            return SecurityContext.create();
        } else {
            char[] storePassword =
                    options.getOptional(VervericaCatalogOptions.HTTP_KEY_STORE_PASSWORD).orElse("")
                            .toCharArray();
            if (storePassword.length == 0) {
                throw new RuntimeException("Missing password for provided KeyStore");
            }
            return SecurityContext.createFromKeyStore(keyStorePath, storePassword);
        }
    }

}
