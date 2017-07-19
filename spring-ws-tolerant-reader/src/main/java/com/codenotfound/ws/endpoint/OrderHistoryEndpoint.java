package com.codenotfound.ws.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.Namespace;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.server.endpoint.annotation.XPathParam;

import com.codenotfound.types.orderhistory.GetOrderHistoryResponse;
import com.codenotfound.types.orderhistory.ObjectFactory;
import com.codenotfound.types.orderhistory.OrderHistoryType;
import com.codenotfound.types.orderhistory.OrderListType;
import com.codenotfound.types.orderhistory.OrderType;

@Endpoint
public class OrderHistoryEndpoint {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderHistoryEndpoint.class);

  private static final String userIdXPath = "//*[local-name()='userId']";

  @PayloadRoot(namespace = "http://codenotfound.com/types/orderhistory",
      localPart = "getOrderHistoryRequest")
  @Namespace(prefix = "oh", uri = "http://codenotfound.com/types/orderhistory")
  @ResponsePayload
  public GetOrderHistoryResponse getOrderHistory(@XPathParam(userIdXPath) String userId) {
    LOGGER.info("received request for order history of userId='{}'", userId);

    // fetch the order history for the received user
    ObjectFactory factory = new ObjectFactory();
    OrderListType orderListType = factory.createOrderListType();

    for (int i = 0; i < 3; i++) {
      OrderType orderType = factory.createOrderType();
      orderType.setOrderId("order" + i);
      orderListType.getOrder().add(orderType);
    }

    OrderHistoryType orderHistoryType = factory.createOrderHistoryType();
    orderHistoryType.setOrderList(orderListType);

    GetOrderHistoryResponse result = factory.createGetOrderHistoryResponse();
    result.setOrderHistory(orderHistoryType);

    return result;
  }
}
