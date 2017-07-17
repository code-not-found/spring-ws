package com.codenotfound.ws.endpoint;

import static org.springframework.ws.test.server.RequestCreators.withSoapEnvelope;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

import javax.xml.transform.Source;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TicketAgentEndpointTest {

  @Autowired
  private ApplicationContext applicationContext;

  private MockWebServiceClient mockClient;

  @Before
  public void createClient() {
    mockClient = MockWebServiceClient.createClient(applicationContext);
  }

  @Test
  public void testListFlights() {
    Source requestEnvelope = new StringSource(
        "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            + "<SOAP-ENV:Header>"
            + "<ns3:listFlightsSoapHeaders xmlns:ns3=\"http://example.org/TicketAgent.xsd\">"
            + "<isGoldClubMember>true</isGoldClubMember>" + "<clientId>abc123</clientId>"
            + "</ns3:listFlightsSoapHeaders>" + "</SOAP-ENV:Header>" + "<SOAP-ENV:Body>"
            + "<ns3:listFlightsRequest xmlns:ns3=\"http://example.org/TicketAgent.xsd\">"
            + "</ns3:listFlightsRequest>" + "</SOAP-ENV:Body>" + "</SOAP-ENV:Envelope>");

    Source responsePayload =
        new StringSource("<v1:listFlightsResponse xmlns:v1=\"http://example.org/TicketAgent.xsd\">"
            + "<flightNumber>101</flightNumber>" + "<flightNumber>202</flightNumber>"
            + "</v1:listFlightsResponse>");

    mockClient.sendRequest(withSoapEnvelope(requestEnvelope)).andExpect(payload(responsePayload));
  }
}
