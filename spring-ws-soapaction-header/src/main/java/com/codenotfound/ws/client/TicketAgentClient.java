package com.codenotfound.ws.client;

import java.math.BigInteger;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.example.ticketagent.ObjectFactory;
import org.example.ticketagent.TFlightsResponse;
import org.example.ticketagent.TListFlights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapMessage;

@Component
public class TicketAgentClient {

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    @SuppressWarnings("unchecked")
    public List<BigInteger> listFlights() {

        ObjectFactory factory = new ObjectFactory();
        TListFlights tListFlights = factory.createTListFlights();

        JAXBElement<TListFlights> request = factory
                .createListFlightsRequest(tListFlights);

        JAXBElement<TFlightsResponse> response = (JAXBElement<TFlightsResponse>) webServiceTemplate
                .marshalSendAndReceive(request,
                        new WebServiceMessageCallback() {

                            public void doWithMessage(
                                    WebServiceMessage webServiceMessage) {
                                // set the soapaction header
                                ((SoapMessage) webServiceMessage)
                                        .setSoapAction(
                                                "http://example.com/TicketAgent/listFlights");
                            }
                        });

        return response.getValue().getFlightNumber();
    }
}
