package com.codenotfound.order.endpoint;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.codenotfound.types.order.ObjectFactory;
import com.codenotfound.types.order.Order;
import com.codenotfound.types.order.OrderConfirmation;

@Endpoint
public class CreateOrderEndpoint {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CreateOrderEndpoint.class);

    private static final String NAMESPACE_URI = "http://codenotfound.com/types/order";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "order")
    @ResponsePayload
    public OrderConfirmation createOrder(@RequestPayload Order request) {
        LOGGER.info(
                "Endpoint received order for Customer[firstName={},lastName={}]",
                request.getCustomer().getFirstName(),
                request.getCustomer().getLastName());

        // process order

        ObjectFactory factory = new ObjectFactory();
        OrderConfirmation response = factory.createOrderConfirmation();
        response.setConfirmationId(UUID.randomUUID().toString());

        LOGGER.info("Endpoint sending orderConfirmationId='{}'",
                response.getConfirmationId());
        return response;
    }
}
