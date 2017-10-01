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
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;

@Configuration
public class ClientConfig {

  @Value("${client.default-uri}")
  private String defaultUri;

  @Value("${client.encrypt.trust-store}")
  private Resource trustStore;

  @Value("${client.encrypt.trust-store-password}")
  private String trustStorePassword;

  @Value("${client.encrypt.certificate-alias}")
  private String certificateAlias;

  @Value("${client.encrypt.key-store}")
  private Resource keyStore;

  @Value("${client.encrypt.key-store-password}")
  private String keyStorePassword;

  @Value("${client.encrypt.key-password}")
  private String keyPassword;

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
    // encrypt the outgoing messages
    securityInterceptor.setSecurementActions("Encrypt");
    // set the part of the content that needs to be encrypted
    securityInterceptor.setSecurementEncryptionParts(
        "{Content}{http://example.org/TicketAgent.xsd}listFlightsRequest");
    // alias of the public certificate used to encrypt
    securityInterceptor.setSecurementEncryptionUser(certificateAlias);
    // trust store that contains the public certificate used to encrypt
    securityInterceptor
        .setSecurementEncryptionCrypto(clientTrustStoreCryptoFactoryBean().getObject());

    // decrypt the incoming messages
    securityInterceptor.setValidationActions("Encrypt");
    // validationCallbackHandler specifying the password of the private key
    securityInterceptor.setValidationCallbackHandler(clientValidationCallbackHandler());
    // key store that contains the decryption private key used to decrypt
    securityInterceptor
        .setValidationDecryptionCrypto(clientKeyStoreCryptoFactoryBean().getObject());

    return securityInterceptor;
  }

  @Bean
  public CryptoFactoryBean clientTrustStoreCryptoFactoryBean() throws IOException {
    CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
    cryptoFactoryBean.setKeyStoreLocation(trustStore);
    cryptoFactoryBean.setKeyStorePassword(trustStorePassword);

    return cryptoFactoryBean;
  }

  @Bean
  public CryptoFactoryBean clientKeyStoreCryptoFactoryBean() throws IOException {
    CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
    cryptoFactoryBean.setKeyStoreLocation(keyStore);
    cryptoFactoryBean.setKeyStorePassword(keyStorePassword);

    return cryptoFactoryBean;
  }

  @Bean
  public KeyStoreCallbackHandler clientValidationCallbackHandler() {
    KeyStoreCallbackHandler keyStoreCallbackHandler = new KeyStoreCallbackHandler();
    keyStoreCallbackHandler.setPrivateKeyPassword(keyPassword);

    return keyStoreCallbackHandler;
  }
}
