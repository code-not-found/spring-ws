package com.codenotfound.ws.client;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;



@Configuration
public class ClientConfig {

  @Value("${client.user.name}")
  private String name;

  @Value("${client.user.password}")
  private String password;

  @Bean
  Jaxb2Marshaller jaxb2Marshaller() {

    Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
    jaxb2Marshaller.setContextPath("org.example.ticketagent");
    return jaxb2Marshaller;
  }

  @Bean
  public WebServiceTemplate webServiceTemplate() {

    WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    webServiceTemplate.setMarshaller(jaxb2Marshaller());
    webServiceTemplate.setUnmarshaller(jaxb2Marshaller());
    webServiceTemplate.setDefaultUri("http://localhost:9090/codenotfound/ws/ticketagent");
    // set
    webServiceTemplate.setMessageSender(httpComponentsMessageSender());

    return webServiceTemplate;
  }

  @Bean
  public HttpComponentsMessageSender httpComponentsMessageSender() {
    HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
    httpComponentsMessageSender.setCredentials(usernamePasswordCredentials());

    return httpComponentsMessageSender;
  }

  @Bean
  public UsernamePasswordCredentials usernamePasswordCredentials() {
    return new UsernamePasswordCredentials(name, password);
  }
}
