package com.codenotfound.ws.client;

import javax.net.ssl.HostnameVerifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.security.support.KeyStoreFactoryBean;
import org.springframework.ws.soap.security.support.TrustManagersFactoryBean;
import org.springframework.ws.transport.http.HttpsUrlConnectionMessageSender;

@Configuration
public class ClientConfig {

  @Value("${client.default-uri}")
  private String defaultUri;

  @Value("${client.ssl.trust-store}")
  private Resource trustStore;

  @Value("${client.ssl.trust-store-password}")
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
    // set a httpsUrlConnectionMessageSender to handle the HTTPS session
    webServiceTemplate.setMessageSender(httpsUrlConnectionMessageSender());

    return webServiceTemplate;
  }

  @Bean
  public HttpsUrlConnectionMessageSender httpsUrlConnectionMessageSender() throws Exception {
    HttpsUrlConnectionMessageSender httpsUrlConnectionMessageSender =
        new HttpsUrlConnectionMessageSender();
    httpsUrlConnectionMessageSender.setTrustManagers(trustManagersFactoryBean().getObject());
    // allows the client to skip host name verification as otherwise following error is thrown:
    // java.security.cert.CertificateException: No name matching localhost found
    httpsUrlConnectionMessageSender.setHostnameVerifier(new HostnameVerifier() {
      @Override
      public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
        if ("localhost".equals(hostname)) {
          return true;
        }
        return false;
      }
    });

    return httpsUrlConnectionMessageSender;
  }

  @Bean
  public KeyStoreFactoryBean trustStore() {
    KeyStoreFactoryBean keyStoreFactoryBean = new KeyStoreFactoryBean();
    keyStoreFactoryBean.setLocation(trustStore);
    keyStoreFactoryBean.setPassword(trustStorePassword);

    return keyStoreFactoryBean;
  }

  @Bean
  public TrustManagersFactoryBean trustManagersFactoryBean() {
    TrustManagersFactoryBean trustManagersFactoryBean = new TrustManagersFactoryBean();
    trustManagersFactoryBean.setKeyStore(trustStore().getObject());

    return trustManagersFactoryBean;
  }
}
