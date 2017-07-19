package com.codenotfound.ws.client;

import java.io.IOException;
import java.util.List;

import javax.xml.transform.dom.DOMResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.support.MarshallingUtils;
import org.springframework.xml.xpath.NodeMapper;
import org.springframework.xml.xpath.XPathExpression;
import org.w3c.dom.Node;

import com.codenotfound.types.orderhistory.GetOrderHistoryRequest;
import com.codenotfound.types.orderhistory.ObjectFactory;
import com.codenotfound.ws.model.Order;
import com.codenotfound.ws.model.OrderHistory;

@Component
public class OrderHistoryClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(OrderHistoryClient.class);

  @Autowired
  private XPathExpression orderXPath;
  @Autowired
  private XPathExpression orderIdXPath;

  @Autowired
  private WebServiceTemplate webServiceTemplate;

  public OrderHistory getOrderHistory(String userId) throws IOException {
    // create the request
    ObjectFactory factory = new ObjectFactory();
    GetOrderHistoryRequest getOrderHistoryRequest = factory.createGetOrderHistoryRequest();
    getOrderHistoryRequest.setUserId(userId);

    // marshal the request
    WebServiceMessage request = webServiceTemplate.getMessageFactory().createWebServiceMessage();
    MarshallingUtils.marshal(webServiceTemplate.getMarshaller(), getOrderHistoryRequest, request);

    // call the service
    DOMResult responseResult = new DOMResult();
    webServiceTemplate.sendSourceAndReceiveToResult(request.getPayloadSource(), responseResult);

    // extract the needed elements
    List<Order> orders = orderXPath.evaluate(responseResult.getNode(), new NodeMapper<Order>() {

      @Override
      public Order mapNode(Node node, int nodeNum) {
        // get the orderId
        String orderId = orderIdXPath.evaluateAsString(node);
        // create an order
        return new Order(orderId);
      }
    });

    OrderHistory result = new OrderHistory();
    result.setOrders(orders);
    LOGGER.info("found '{}' orders for userId='{}'", result.getOrders().size(), userId);

    return result;
  }
}
