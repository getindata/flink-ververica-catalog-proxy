package com.getindata.flink.catalog.httpclient.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.UUID;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * This class represents a security context for given proxy instance. The Security context
 * is backed by in memory instance of Java's {@link KeyStore}. All keys and certificates managed by
 * instance of this class are only in scope of this object and not entire JVM.
 */
@Slf4j
public class SecurityContext {

    private static final String JKS_STORE_TYPE = "JKS";

    private static final String X_509_CERTIFICATE_TYPE = "X.509";

    private final char[] storePassword;

    private final KeyStore keystore;

    /**
     * Creates instance of {@link SecurityContext} and initialize {@link KeyStore} instance.
     */
    private SecurityContext(KeyStore keystore, char[] storePassword) {
        this.keystore = keystore;
        this.storePassword = storePassword;
    }

    /**
     * Creates a {@link SecurityContext} with empty {@link KeyStore}
     *
     * @return new instance of {@link SecurityContext}
     */
    public static SecurityContext create() {

        char[] storePasswordCharArr = UUID.randomUUID().toString().toCharArray();

        try {
            KeyStore keystore = KeyStore.getInstance(JKS_STORE_TYPE);
            keystore.load(null, storePasswordCharArr);
            log.info("Created KeyStore for proxy security context.");
            return new SecurityContext(keystore, storePasswordCharArr);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to create KeyStore for proxy Security Context.",
                    e
            );
        }
    }

    /**
     * Creates a {@link SecurityContext} with {@link KeyStore} loaded from provided path.
     *
     * @param keyStorePath  Path to keystore.
     * @param storePassword password for keystore.
     * @return new instance of {@link SecurityContext}
     */
    public static SecurityContext createFromKeyStore(String keyStorePath, char[] storePassword) {
        try {
            log.info("Creating Security Context from keystore " + keyStorePath);
            File file = new File(keyStorePath);
            InputStream stream = new FileInputStream(file);
            KeyStore keystore = KeyStore.getInstance(JKS_STORE_TYPE);
            keystore.load(stream, storePassword);
            return new SecurityContext(keystore, storePassword);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to create KeyStore for proxy Security Context.",
                    e
            );
        }
    }

    /**
     * Creates an instance of {@link SSLContext} backed by {@link KeyStore} from this {@link
     * SSLContext} instance.
     *
     * @param trustManagers {@link TrustManager} that should be used to create {@link SSLContext}
     * @return new sslContext instance.
     */
    public SSLContext getSslContext(TrustManager[] trustManagers) {
        try {

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(this.keystore, this.storePassword);

            // populate SSLContext with key manager
            SSLContext sslCtx = SSLContext.getInstance("TLSv1.2");
            sslCtx.init(keyManagerFactory.getKeyManagers(), trustManagers, null);
            return sslCtx;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates TrustManagers for given {@link KeyStore} managed by this instance of
     * {@link SSLContext}. It is important that all keys and certificates should be added
     * before calling this method. Any key/certificate added after calling this method
     * will not be visible by previously created TrustManager objects.
     *
     * @return an array of {@link TrustManager}
     */
    public TrustManager[] getTrustManagers() {
        try {
            String alg = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(alg);

            trustManagerFactory.init(this.keystore);
            log.info("Created security Trust Managers for proxy security context.");
            return trustManagerFactory.getTrustManagers();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(
                    "Unable to created Trust Managers for proxy security context.",
                    e
            );
        }
    }

    /**
     * Adds certificate to as trusted. Certificate is added only to this Context's {@link KeyStore}
     * and not for entire JVM.
     *
     * @param certPath path to certificate that should be added as trusted.
     */
    public void addCertToTrustStore(String certPath) {

        log.info("Trying to add certificate to Security Context - " + certPath);
        try (FileInputStream certInputStream = new FileInputStream(certPath)) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance(
                    X_509_CERTIFICATE_TYPE);
            Certificate certificate = certificateFactory.generateCertificate(certInputStream);
            this.keystore.setCertificateEntry(UUID.randomUUID().toString(), certificate);
            log.info("Certificated added to keyStore ass trusted - " + certPath);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to add certificate as trusted to proxy security context - "
                            + certPath,
                    e
            );
        }
    }

}
