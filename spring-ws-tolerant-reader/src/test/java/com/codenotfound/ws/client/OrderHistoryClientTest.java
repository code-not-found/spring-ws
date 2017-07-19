package com.codenotfound.ws.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import java.io.IOException;

import javax.xml.transform.Source;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.xml.transform.StringSource;

import com.codenotfound.ws.model.OrderHistory;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderHistoryClientTest {

  @Autowired
  private OrderHistoryClient orderHistoryClient;

  @Autowired
  private WebServiceTemplate webServiceTemplate;

  private MockWebServiceServer mockWebServiceServer;

  @Before
  public void createServer() {
    mockWebServiceServer = MockWebServiceServer.createServer(webServiceTemplate);
  }

  @Test
  public void testGetOrderHistory() throws IOException {
    Source requestPayload = new StringSource(
        "<ns1:getOrderHistoryRequest xmlns:ns1=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns1:userId>abc123</ns1:userId>" + "</ns1:getOrderHistoryRequest>");

    Source responsePayload = new StringSource(
        "<ns1:getOrderHistoryResponse xmlns:ns1=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns1:orderHistory>" + "<ns1:orderList>"
            + "<ns1:order><ns1:orderId>order1</ns1:orderId></ns1:order>"
            + "<ns1:order><ns1:orderId>order2</ns1:orderId></ns1:order>"
            + "<ns1:order><ns1:orderId>order3</ns1:orderId></ns1:order>" + "</ns1:orderList>"
            + "</ns1:orderHistory>" + "</ns1:getOrderHistoryResponse>");

    mockWebServiceServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

    OrderHistory orderHistory = orderHistoryClient.getOrderHistory("abc123");
    assertThat(orderHistory.getOrders().get(2).getOrderId()).isEqualTo("order3");

    mockWebServiceServer.verify();
  }

  @Test
  public void testGetOrderHistoryOnlyNeededElements() throws IOException {
    Source requestPayload = new StringSource(
        "<ns1:getOrderHistoryRequest xmlns:ns1=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns1:userId>def456</ns1:userId>" + "</ns1:getOrderHistoryRequest>");

    Source responsePayload = new StringSource(
        "<ns1:getOrderHistoryResponse xmlns:ns1=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns1:orderHistory>" + "<ns1:orderList>"
            + "<ns1:order><ns1:orderId>order4</ns1:orderId><ns1:orderName>order-name-1</ns1:orderName></ns1:order>"
            + "<ns1:order><ns1:orderId>order5</ns1:orderId><ns1:orderName>order-name-2</ns1:orderName></ns1:order>"
            + "<ns1:order><ns1:orderId>order6</ns1:orderId><ns1:orderName>order-name-3</ns1:orderName></ns1:order>"
            + "</ns1:orderList>" + "</ns1:orderHistory>" + "</ns1:getOrderHistoryResponse>");

    mockWebServiceServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

    OrderHistory orderHistory = orderHistoryClient.getOrderHistory("def456");
    assertThat(orderHistory.getOrders().get(2).getOrderId()).isEqualTo("order6");

    mockWebServiceServer.verify();
  }

  @Test
  public void testGetOrderHistoryMinimumAssumptions() throws IOException {
    Source requestPayload = new StringSource(
        "<ns1:getOrderHistoryRequest xmlns:ns1=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns1:userId>ghi789</ns1:userId>" + "</ns1:getOrderHistoryRequest>");

    Source responsePayload = new StringSource(
        "<ns1:getOrderHistoryResponse xmlns:ns1=\"http://codenotfound.com/types/orderhistory\">"
            + "<ns1:anotherElement>" + "<ns1:orderHistory>" + "<ns1:orderList>"
            + "<ns1:order><ns1:orderId>order7</ns1:orderId></ns1:order>"
            + "<ns1:order><ns1:orderId>order8</ns1:orderId></ns1:order>"
            + "<ns1:order><ns1:orderId>order9</ns1:orderId></ns1:order>" + "</ns1:orderList>"
            + "</ns1:orderHistory>" + "</ns1:anotherElement>" + "</ns1:getOrderHistoryResponse>");

    mockWebServiceServer.expect(payload(requestPayload)).andRespond(withPayload(responsePayload));

    OrderHistory orderHistory = orderHistoryClient.getOrderHistory("ghi789");
    assertThat(orderHistory.getOrders().get(2).getOrderId()).isEqualTo("order9");

    mockWebServiceServer.verify();
  }
}
