package com.codenotfound.ws.client;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class ClientConfig {

  @Value("${client.default-uri}")
  private String defaultUri;

  @Value("${client.ssl.trust-store}")
  private Resource trustStore;

  @Value("${client.ssl.trust-store-password}")
  private String trustStorePassword;

  @Value("${client.timeout}")
  private int timeout;

  @Bean
  Jaxb2Marshaller jaxb2Marshaller() {
    Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
    jaxb2Marshaller.setContextPath("org.example.ticketagent");

    return jaxb2Marshaller;
  }

  @Bean
  public WebServiceTemplate webServiceTemplate() throws KeyManagementException,
      NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
    WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMarshaller(jaxb2Marshaller());
    webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
    webServiceTemplate.setDefaultUri(defaultUri);
    webServiceTemplate.setMessageSender(httpComponentsMessageSender());

    return webServiceTemplate;
  }

  @Bean
  public HttpComponentsMessageSender httpComponentsMessageSender() throws KeyManagementException,
      NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
    HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
    httpComponentsMessageSender.setHttpClient(httpClient());

    return httpComponentsMessageSender;
  }

  public HttpClient httpClient() throws KeyManagementException, NoSuchAlgorithmException,
      KeyStoreException, CertificateException, IOException {
    return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig())
        .setSSLSocketFactory(sslConnectionSocketFactory())
        .addInterceptorFirst(new ContentLengthHeaderRemover()).build();
  }

  public RequestConfig requestConfig() {
    return RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
        .setSocketTimeout(timeout).build();
  }

  public SSLConnectionSocketFactory sslConnectionSocketFactory() throws KeyManagementException,
      NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException {
    // allows the client to skip host name verification as otherwise following error is thrown:
    // java.security.cert.CertificateException: No name matching localhost found
    return new SSLConnectionSocketFactory(sslContext(), new HostnameVerifier() {
      @Override
      public boolean verify(String hostname, SSLSession session) {
        return true;
      }
    });
  }

  public SSLContext sslContext() throws KeyManagementException, NoSuchAlgorithmException,
      KeyStoreException, CertificateException, IOException {
    return SSLContextBuilder.create()
        .loadTrustMaterial(trustStore.getFile(), trustStorePassword.toCharArray())
        .setProtocol("TLS").build();
  }

  private static class ContentLengthHeaderRemover implements HttpRequestInterceptor {
    @Override
    public void process(HttpRequest request, HttpContext context)
        throws HttpException, IOException {
      request.removeHeaders(HTTP.CONTENT_LEN);
    }
  }
}
