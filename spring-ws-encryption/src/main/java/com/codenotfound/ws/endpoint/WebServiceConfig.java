package com.codenotfound.ws.endpoint;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceConfig.class);

  @Value("${server.encrypt.key-store}")
  private Resource keyStore;

  @Value("${server.encrypt.key-store-password}")
  private String keyStorePassword;

  @Value("${server.encrypt.key-password}")
  private String keyPassword;

  @Value("${server.encrypt.trust-store}")
  private Resource trustStore;

  @Value("${server.encrypt.trust-store-password}")
  private String trustStorePassword;

  @Value("${server.encrypt.certificate-alias}")
  private String certificateAlias;

  @Bean
  public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {

    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);

    return new ServletRegistrationBean(servlet, "/codenotfound/ws/*");
  }

  @Bean(name = "ticketagent")
  public Wsdl11Definition defaultWsdl11Definition() {
    SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
    wsdl11Definition.setWsdl(new ClassPathResource("/wsdl/ticketagent.wsdl"));

    return wsdl11Definition;
  }

  @Override
  public void addInterceptors(List<EndpointInterceptor> interceptors) {
    interceptors.add(serverSecurityInterceptor());
  }

  @Bean
  public Wss4jSecurityInterceptor serverSecurityInterceptor() {
    Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
    // decrypt the incoming messages
    securityInterceptor.setValidationActions("Encrypt");
    // validationCallbackHandler specifying the password of the private key
    securityInterceptor.setValidationCallbackHandler(serverValidationCallbackHandler());
    // key store that contains the private key used to decrypt
    try {
      securityInterceptor
          .setValidationDecryptionCrypto(serverKeyStoreCryptoFactoryBean().getObject());
    } catch (Exception e) {
      LOGGER.error("unable to get the server key store", e);
    }

    // encrypt the outgoing messages
    securityInterceptor.setSecurementActions("Encrypt");
    // set the part of the content that needs to be encrypted
    securityInterceptor.setSecurementEncryptionParts(
        "{Content}{http://example.org/TicketAgent.xsd}listFlightsResponse");
    // alias of the public certificate used to encrypt
    securityInterceptor.setSecurementEncryptionUser(certificateAlias);
    // trust store that contains the public certificate used to encrypt
    try {
      securityInterceptor
          .setSecurementEncryptionCrypto(serverTrustStoreCryptoFactoryBean().getObject());
    } catch (Exception e) {
      LOGGER.error("unable to get the server trust store", e);
    }

    return securityInterceptor;
  }

  @Bean
  public CryptoFactoryBean serverKeyStoreCryptoFactoryBean() throws IOException {
    CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
    cryptoFactoryBean.setKeyStoreLocation(keyStore);
    cryptoFactoryBean.setKeyStorePassword(keyStorePassword);

    return cryptoFactoryBean;
  }

  @Bean
  public KeyStoreCallbackHandler serverValidationCallbackHandler() {
    KeyStoreCallbackHandler keyStoreCallbackHandler = new KeyStoreCallbackHandler();
    keyStoreCallbackHandler.setPrivateKeyPassword(keyPassword);

    return keyStoreCallbackHandler;
  }

  @Bean
  public CryptoFactoryBean serverTrustStoreCryptoFactoryBean() throws IOException {
    CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
    cryptoFactoryBean.setKeyStoreLocation(trustStore);
    cryptoFactoryBean.setKeyStorePassword(trustStorePassword);

    return cryptoFactoryBean;
  }
}
