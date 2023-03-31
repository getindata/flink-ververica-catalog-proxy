package com.getindata.flink.catalog.httpclient;

import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public static final String PROP_DELIM = ",";

    /**
     * Creates Java's {@link JavaNetCatalogHttpClient} instance that will be using default, JVM shared {@link
     * java.util.concurrent.ForkJoinPool} for async calls.
     *
     * @param options properties used to build {@link SSLContext}
     * @param options
     * @return new {@link HttpClient} instance.
     */
    public static JavaNetCatalogHttpClient createClient(ReadableConfig options) {

        HttpClient httpClient = buildHttpClient(options);

        String[] headers = options
                .getOptional(VervericaCatalogOptions.HTTP_PROXY_HEADERS)
                .map(headerString -> headerString.split(PROP_DELIM))
                .orElse(new String[]{});

        return new JavaNetCatalogHttpClient(httpClient, headers);
    }

    private static HttpClient buildHttpClient(ReadableConfig options) {
        HttpClient.Builder builder = HttpClient.newBuilder()
                .followRedirects(Redirect.NORMAL);

        getSslContext(options).ifPresent(builder::sslContext);

        return builder.build();
    }

    /**
     * Creates an {@link SSLContext} based on provided properties.
     * <ul>
     * <li>{@link VervericaCatalogOptions#HTTP_ALLOW_SELF_SIGNED}</li>
     * <li>{@link VervericaCatalogOptions#HTTP_SERVER_TRUSTED_CERT}</li>
     * <li>{@link #PROP_DELIM}</li>
     * </ul>
     *
     * @param options properties used to build {@link SSLContext}
     * @return new {@link SSLContext} instance.
     */
    private static Optional<SSLContext> getSslContext(ReadableConfig options) {
        String[] serverTrustedCerts = options
                .getOptional(VervericaCatalogOptions.HTTP_SERVER_TRUSTED_CERT)
                .map(headerString -> headerString.split(PROP_DELIM))
                .orElse(new String[]{});

        if (serverTrustedCerts.length <= 0) {
            return Optional.empty();
        }

        SecurityContext securityContext = SecurityContext.create();

        for (String cert : serverTrustedCerts) {
            if (!StringUtils.isNullOrWhitespaceOnly(cert)) {
                securityContext.addCertToTrustStore(cert);
            }
        }

        boolean selfSignedCert =
                options.getOptional(VervericaCatalogOptions.HTTP_ALLOW_SELF_SIGNED).orElse(false);


        // NOTE TrustManagers must be created AFTER adding all certificates to KeyStore.
        TrustManager[] trustManagers = getTrustedManagers(securityContext, selfSignedCert);
        return Optional.of(securityContext.getSslContext(trustManagers));
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

}
