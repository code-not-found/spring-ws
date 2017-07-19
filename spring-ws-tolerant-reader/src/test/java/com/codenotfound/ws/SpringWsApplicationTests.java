package com.codenotfound.ws;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.codenotfound.ws.client.OrderHistoryClient;
import com.codenotfound.ws.model.OrderHistory;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringWsApplicationTests {

  @Autowired
  private OrderHistoryClient orderHistoryClient;

  @Test
  public void testListFlights() throws IOException {
    OrderHistory aOrderHistory = orderHistoryClient.getOrderHistory("abc123");

    assertThat(aOrderHistory.getOrders().get(2).getOrderId()).isEqualTo("order2");
  }
}
