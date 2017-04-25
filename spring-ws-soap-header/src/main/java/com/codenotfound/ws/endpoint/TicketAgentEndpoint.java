package com.codenotfound.ws.endpoint;

import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;

import org.example.ticketagent.ListFlightsSoapHeaders;
import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.server.endpoint.annotation.SoapHeader;

@Endpoint
public class TicketAgentEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(TicketAgentEndpoint.class);

  @SuppressWarnings("unchecked")
  @PayloadRoot(namespace = "http://example.org/TicketAgent.xsd", localPart = "listFlightsRequest")
  @ResponsePayload
  public JAXBElement<TFlightsResponse> listFlights(
      @RequestPayload JAXBElement<TListFlights> request, @SoapHeader(
          value = "{http://example.org/TicketAgent.xsd}listFlightsSoapHeaders") SoapHeaderElement soapHeader) {

    boolean isGoldClubMember = false;

    try {
      // create an unmarshaller
      JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
      Unmarshaller unmarshaller = context.createUnmarshaller();

      // unmarshal the header from the specified source
      JAXBElement<ListFlightsSoapHeaders> headers =
          (JAXBElement<ListFlightsSoapHeaders>) unmarshaller.unmarshal(soapHeader.getSource());

      // get the header values
      ListFlightsSoapHeaders requestSoapHeaders = headers.getValue();
      isGoldClubMember = requestSoapHeaders.isIsGoldClubMember();
    } catch (Exception e) {
      LOGGER.error("error during unmarshalling of the SOAP headers", e);
    }


    ObjectFactory factory = new ObjectFactory();
    TFlightsResponse tFlightsResponse = factory.createTFlightsResponse();
    tFlightsResponse.getFlightNumber().add(BigInteger.valueOf(101));

    // add an extra flightNumber in the case of a GoldClubMember
    if (isGoldClubMember) {
      LOGGER.info("GoldClubMember found!");
      tFlightsResponse.getFlightNumber().add(BigInteger.valueOf(202));
    }

    return factory.createListFlightsResponse(tFlightsResponse);
  }
}
