package com.codenotfound.order.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.codenotfound.types.order.CustomerType;
import com.codenotfound.types.order.LineItemsType;
import com.codenotfound.types.order.ObjectFactory;
import com.codenotfound.types.order.Order;
import com.codenotfound.types.order.OrderConfirmation;

@Component
public class CreateOrderClient {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CreateOrderClient.class);

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    public OrderConfirmation createOrder(CustomerType customer,
            LineItemsType lineItems) {

        ObjectFactory factory = new ObjectFactory();
        Order order = factory.createOrder();

        order.setCustomer(customer);
        order.setLineItems(lineItems);

        LOGGER.info(
                "Client sending order for Customer[firstName={},lastName={}]",
                customer.getFirstName(), customer.getLastName());

        OrderConfirmation orderConfirmation = (OrderConfirmation) webServiceTemplate
                .marshalSendAndReceive(order);

        LOGGER.info("Client received orderConfirmationId='{}'",
                orderConfirmation.getConfirmationId());
        return orderConfirmation;
    }
}
