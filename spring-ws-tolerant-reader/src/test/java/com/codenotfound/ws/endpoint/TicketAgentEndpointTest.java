package com.codenotfound.ws.endpoint;

import static org.springframework.ws.test.server.RequestCreators.withPayload;
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
  public void testGetOrderHistory() {
    Source requestPayload = new StringSource(
        "<ns1:getOrderHistoryRequest xmlns:ns1=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns1:userId>jkl123</ns1:userId>" + "</ns1:getOrderHistoryRequest>");

    Source responsePayload = new StringSource(
        "<ns2:getOrderHistoryResponse xmlns:ns2=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns2:orderHistory>" + "<ns2:orderList>"
            + "<ns2:order><ns2:orderId>order0</ns2:orderId></ns2:order>"
            + "<ns2:order><ns2:orderId>order1</ns2:orderId></ns2:order>"
            + "<ns2:order><ns2:orderId>order2</ns2:orderId></ns2:order>" + "</ns2:orderList>"
            + "</ns2:orderHistory>" + "</ns2:getOrderHistoryResponse>");

    mockClient.sendRequest(withPayload(requestPayload)).andExpect(payload(responsePayload));
  }

  @Test
  public void testGetOrderHistoryOnlyNeededElements() {
    Source requestPayload = new StringSource(
        "<ns1:getOrderHistoryRequest xmlns:ns1=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns1:userId>mno123</ns1:userId>" + "<ns1:userName>user-name</ns1:userName>"
            + "</ns1:getOrderHistoryRequest>");

    Source responsePayload = new StringSource(
        "<ns2:getOrderHistoryResponse xmlns:ns2=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns2:orderHistory>" + "<ns2:orderList>"
            + "<ns2:order><ns2:orderId>order0</ns2:orderId></ns2:order>"
            + "<ns2:order><ns2:orderId>order1</ns2:orderId></ns2:order>"
            + "<ns2:order><ns2:orderId>order2</ns2:orderId></ns2:order>" + "</ns2:orderList>"
            + "</ns2:orderHistory>" + "</ns2:getOrderHistoryResponse>");

    mockClient.sendRequest(withPayload(requestPayload)).andExpect(payload(responsePayload));
  }

  @Test
  public void testGetOrderHistoryMinimumAssumptions() {
    Source requestPayload = new StringSource(
        "<ns1:getOrderHistoryRequest xmlns:ns1=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns1:oldWrapper>" + "<ns1:userId>pqr123</ns1:userId>" + "</ns1:oldWrapper>"
            + "</ns1:getOrderHistoryRequest>");

    Source responsePayload = new StringSource(
        "<ns2:getOrderHistoryResponse xmlns:ns2=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns2:orderHistory>" + "<ns2:orderList>"
            + "<ns2:order><ns2:orderId>order0</ns2:orderId></ns2:order>"
            + "<ns2:order><ns2:orderId>order1</ns2:orderId></ns2:order>"
            + "<ns2:order><ns2:orderId>order2</ns2:orderId></ns2:order>" + "</ns2:orderList>"
            + "</ns2:orderHistory>" + "</ns2:getOrderHistoryResponse>");

    mockClient.sendRequest(withPayload(requestPayload)).andExpect(payload(responsePayload));
  }
}
