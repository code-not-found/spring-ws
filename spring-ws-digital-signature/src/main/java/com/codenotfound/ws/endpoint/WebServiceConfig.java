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
import org.springframework.ws.soap.security.wss4j2.support.CryptoFactoryBean;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceConfig.class);

  @Value("${server.signature.key-store}")
  private Resource keyStore;

  @Value("${server.signature.key-store-password}")
  private String keyStorePassword;

  @Value("${server.signature.key-alias}")
  private String keyAlias;

  @Value("${server.signature.key-password}")
  private String keyPassword;

  @Value("${server.signature.trust-store}")
  private Resource trustStore;

  @Value("${server.signature.trust-store-password}")
  private String trustStorePassword;

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
    // check the time stamp and signature of the request
    securityInterceptor.setValidationActions("Signature Timestamp");
    // trust store that contains the trusted certificate
    try {
      securityInterceptor
          .setValidationSignatureCrypto(serverTrustStoreCryptoFactoryBean().getObject());
    } catch (Exception e) {
      LOGGER.error("unable to get the server trust store", e);
    }

    // add a time stamp and sign the request
    securityInterceptor.setSecurementActions("Signature Timestamp");
    // alias of the private key
    securityInterceptor.setSecurementUsername(keyAlias);
    // password of the private key
    securityInterceptor.setSecurementPassword(keyPassword);
    // key store that contains the private key
    try {
      securityInterceptor
          .setSecurementSignatureCrypto(serverKeyStoreCryptoFactoryBean().getObject());
    } catch (Exception e) {
      LOGGER.error("unable to get the server key store", e);
    }

    return securityInterceptor;
  }

  @Bean
  public CryptoFactoryBean serverTrustStoreCryptoFactoryBean() throws IOException {
    CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
    cryptoFactoryBean.setKeyStoreLocation(trustStore);
    cryptoFactoryBean.setKeyStorePassword(trustStorePassword);

    return cryptoFactoryBean;
  }

  @Bean
  public CryptoFactoryBean serverKeyStoreCryptoFactoryBean() throws IOException {
    CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
    cryptoFactoryBean.setKeyStoreLocation(keyStore);
    cryptoFactoryBean.setKeyStorePassword(keyStorePassword);

    return cryptoFactoryBean;
  }
}
