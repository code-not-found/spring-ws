package com.codenotfound.ws.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.netflix.discovery.EurekaClient;

@Configuration
public class ClientConfig {

  @Autowired
  private EurekaClient discoveryClient;

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

    // set the host:port information fetched from Eureka
    webServiceTemplate.setDefaultUri(
        discoveryClient.getApplication("ticketagent-service")
            .getInstances().get(0).getHomePageUrl()
            + "/codenotfound/ws/helloworld");

    return webServiceTemplate;
  }
}
