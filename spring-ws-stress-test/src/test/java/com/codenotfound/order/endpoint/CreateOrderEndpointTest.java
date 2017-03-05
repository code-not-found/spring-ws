package com.codenotfound.order.endpoint;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import com.codenotfound.order.endpoint.CreateOrderEndpoint;
import com.codenotfound.types.order.CustomerType;
import com.codenotfound.types.order.LineItemType;
import com.codenotfound.types.order.LineItemsType;
import com.codenotfound.types.order.ObjectFactory;
import com.codenotfound.types.order.Order;
import com.codenotfound.types.order.ProductType;

public class CreateOrderEndpointTest {

    private CreateOrderEndpoint createOrderEndpoint = new CreateOrderEndpoint();

    private Order order;

    @Before
    public void setUp() throws Exception {
        ObjectFactory factory = new ObjectFactory();

        CustomerType customer = factory.createCustomerType();
        customer.setFirstName("John");
        customer.setLastName("Doe");

        ProductType product1 = factory.createProductType();
        product1.setId("1");
        product1.setName("batman action figure");

        LineItemType lineItem1 = factory.createLineItemType();
        lineItem1.setProduct(product1);
        lineItem1.setQuantity(BigInteger.valueOf(1));

        LineItemsType lineItems = factory.createLineItemsType();
        lineItems.getLineItem().add(lineItem1);

        order = factory.createOrder();
        order.setCustomer(customer);
        order.setLineItems(lineItems);
    }

    @Test
    public void testCreateOrder() {
        assertThat(createOrderEndpoint.createOrder(order)
                .getConfirmationId()).isNotBlank();
    }
}
