package com.codenotfound.ws.client;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

@Component
public class TicketAgentClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(TicketAgentClient.class);

  @Autowired
  private WebServiceTemplate webServiceTemplate;

  @SuppressWarnings("unchecked")
  public List<BigInteger> listFlights() {
    ObjectFactory factory = new ObjectFactory();
    TListFlights tListFlights = factory.createTListFlights();

    JAXBElement<TListFlights> request = factory.createListFlightsRequest(tListFlights);
    JAXBElement<TFlightsResponse> response = null;

    try {
      response = (JAXBElement<TFlightsResponse>) webServiceTemplate.marshalSendAndReceive(request);
      return response.getValue().getFlightNumber();
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      // TODO handle the exception
      return new ArrayList<>();
    }
  }
}
