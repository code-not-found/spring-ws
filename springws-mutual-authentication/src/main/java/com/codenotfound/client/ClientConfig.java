package com.codenotfound.client;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

@Configuration
public class ClientConfig {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ClientConfig.class);

    @Value("${stockquote.service.address}")
    private String stockquoteServiceAddress;

    @Value("${client.ssl.trust-store}")
    private Resource clientSslTrustStore;
    @Value("${client.ssl.trust-store-password}")
    private String clientSslTrustStorePassword;

    @Value("${client.ssl.key-store}")
    private Resource clientSslKeyStore;
    @Value("${client.ssl.key-store-password}")
    private String clientSslKeyStorePassword;

    @Bean
    Jaxb2Marshaller jaxb2Marshaller() {

        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setContextPath("com.example.stockquote");
        return jaxb2Marshaller;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate()
            throws KeyStoreException, NoSuchAlgorithmException,
            CertificateException, IOException,
            UnrecoverableKeyException {

        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
        webServiceTemplate.setMarshaller(jaxb2Marshaller());
        webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
        webServiceTemplate.setDefaultUri(stockquoteServiceAddress);

        webServiceTemplate
                .setMessageSender(httpsUrlConnectionMessageSender());

        return webServiceTemplate;
    }

    @Bean
    public HttpsUrlConnectionMessageSender httpsUrlConnectionMessageSender()
            throws KeyStoreException, NoSuchAlgorithmException,
            CertificateException, IOException,
            UnrecoverableKeyException {

        HttpsUrlConnectionMessageSender httpsUrlConnectionMessageSender = new HttpsUrlConnectionMessageSender();

        httpsUrlConnectionMessageSender.setTrustManagers(
                trustManagerFactory().getTrustManagers());
        httpsUrlConnectionMessageSender
                .setKeyManagers(keyManagerFactory().getKeyManagers());

        // allows the client to skip host name verification as otherwise
        // following error is thrown: java.security.cert.CertificateException:
        // No name matching localhost found
        httpsUrlConnectionMessageSender
                .setHostnameVerifier(new HostnameVerifier() {

                    public boolean verify(String hostname,
                            javax.net.ssl.SSLSession sslSession) {
                        if (hostname.equals("localhost")) {
                            return true;
                        }
                        return false;
                    }
                });

        return httpsUrlConnectionMessageSender;
    }

    @Bean
    public TrustManagerFactory trustManagerFactory()
            throws KeyStoreException, NoSuchAlgorithmException,
            CertificateException, IOException {

        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(clientSslTrustStore.getInputStream(),
                clientSslTrustStorePassword.toCharArray());
        LOGGER.info("loaded trust-store: "
                + clientSslTrustStore.getURI().toString());
        clientSslTrustStore.getInputStream().close();

        TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);

        return trustManagerFactory;
    }

    @Bean
    public KeyManagerFactory keyManagerFactory()
            throws KeyStoreException, NoSuchAlgorithmException,
            CertificateException, IOException,
            UnrecoverableKeyException {

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(clientSslKeyStore.getInputStream(),
                clientSslKeyStorePassword.toCharArray());
        LOGGER.info("loaded key-store: "
                + clientSslKeyStore.getURI().toString());
        clientSslKeyStore.getInputStream().close();

        KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore,
                clientSslKeyStorePassword.toCharArray());

        return keyManagerFactory;
    }
}
