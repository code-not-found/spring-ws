package com.codenotfound.ws.client;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;

@Configuration
public class ClientConfig {

  @Value("${client.default-uri}")
  private String defaultUri;

  @Value("${client.signature.key-store}")
  private Resource keyStore;

  @Value("${client.signature.key-store-password}")
  private String keyStorePassword;

  @Value("${client.signature.key-alias}")
  private String keyAlias;

  @Value("${client.signature.key-password}")
  private String keyPassword;

  @Value("${client.signature.trust-store}")
  private Resource trustStore;

  @Value("${client.signature.trust-store-password}")
  private String trustStorePassword;

  @Bean
  Jaxb2Marshaller jaxb2Marshaller() {
    Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
    jaxb2Marshaller.setContextPath("org.example.ticketagent");

    return jaxb2Marshaller;
  }

  @Bean
  public WebServiceTemplate webServiceTemplate() throws Exception {
    WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMarshaller(jaxb2Marshaller());
    webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
    webServiceTemplate.setDefaultUri(defaultUri);

    // register the signatureSecurityInterceptor
    ClientInterceptor[] interceptors = new ClientInterceptor[] {clientSecurityInterceptor()};
    webServiceTemplate.setInterceptors(interceptors);

    return webServiceTemplate;
  }

  @Bean
  public Wss4jSecurityInterceptor clientSecurityInterceptor() throws Exception {
    Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
    // add a time stamp and sign the request
    securityInterceptor.setSecurementActions("Signature Timestamp");
    // alias of the private key
    securityInterceptor.setSecurementUsername(keyAlias);
    // password of the private key
    securityInterceptor.setSecurementPassword(keyPassword);
    // key store that contains the private key
    securityInterceptor.setSecurementSignatureCrypto(clientKeyStoreCryptoFactoryBean().getObject());

    // check the time stamp and signature of the request
    securityInterceptor.setValidationActions("Signature Timestamp");
    // trust store that contains the trusted certificate
    securityInterceptor
        .setValidationSignatureCrypto(clientTrustStoreCryptoFactoryBean().getObject());

    return securityInterceptor;
  }

  @Bean
  public CryptoFactoryBean clientKeyStoreCryptoFactoryBean() throws IOException {
    CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
    cryptoFactoryBean.setKeyStoreLocation(keyStore);
    cryptoFactoryBean.setKeyStorePassword(keyStorePassword);

    return cryptoFactoryBean;
  }

  @Bean
  public CryptoFactoryBean clientTrustStoreCryptoFactoryBean() throws IOException {
    CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
    cryptoFactoryBean.setKeyStoreLocation(trustStore);
    cryptoFactoryBean.setKeyStorePassword(trustStorePassword);

    return cryptoFactoryBean;
  }
}
