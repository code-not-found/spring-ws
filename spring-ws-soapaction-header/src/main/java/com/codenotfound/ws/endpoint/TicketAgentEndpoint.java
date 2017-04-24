package com.codenotfound.ws.endpoint;

import java.math.BigInteger;

import javax.xml.bind.JAXBElement;

import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

@Endpoint
public class TicketAgentEndpoint {

    @SoapAction(value = "http://example.com/TicketAgent/listFlights")
    @ResponsePayload
    public JAXBElement<TFlightsResponse> listFlights(
            @RequestPayload JAXBElement<TListFlights> request) {

        ObjectFactory factory = new ObjectFactory();
        TFlightsResponse tFlightsResponse = factory
                .createTFlightsResponse();
        tFlightsResponse.getFlightNumber()
                .add(BigInteger.valueOf(101));

        return factory.createListFlightsResponse(tFlightsResponse);
    }
}
