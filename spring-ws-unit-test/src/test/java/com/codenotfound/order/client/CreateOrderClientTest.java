package com.codenotfound.order.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.codenotfound.types.order.CustomerType;
import com.codenotfound.types.order.LineItemType;
import com.codenotfound.types.order.LineItemsType;
import com.codenotfound.types.order.ObjectFactory;
import com.codenotfound.types.order.OrderConfirmation;
import com.codenotfound.types.order.ProductType;

@RunWith(MockitoJUnitRunner.class)
public class CreateOrderClientTest {

    @InjectMocks
    private CreateOrderClient createOrderClient; // no need to call constructor

    @Mock
    private WebServiceTemplate webServiceTemplate;
    @Mock
    private OrderConfirmation orderConfirmation;

    private CustomerType customer;
    private LineItemsType lineItems;

    @Before
    public void setUp() throws Exception {
        ObjectFactory factory = new ObjectFactory();

        customer = factory.createCustomerType();
        customer.setFirstName("Jane");
        customer.setLastName("Doe");

        ProductType product1 = factory.createProductType();
        product1.setId("2");
        product1.setName("superman action figure");

        LineItemType lineItem1 = factory.createLineItemType();
        lineItem1.setProduct(product1);
        lineItem1.setQuantity(BigInteger.valueOf(1));

        lineItems = factory.createLineItemsType();
        lineItems.getLineItem().add(lineItem1);
    }

    @Test
    public void testCreateOrder() {
        when(webServiceTemplate.marshalSendAndReceive(any()))
                .thenReturn(orderConfirmation);
        when(orderConfirmation.getConfirmationId()).thenReturn("1234");

        assertThat(createOrderClient.createOrder(customer, lineItems)
                .getConfirmationId()).isEqualTo("1234");
    }
}
