package com.codenotfound.order.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.codenotfound.types.order.CustomerType;
import com.codenotfound.types.order.LineItemType;
import com.codenotfound.types.order.LineItemsType;
import com.codenotfound.types.order.ObjectFactory;
import com.codenotfound.types.order.ProductType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class CreateOrderClientIT {

    @Autowired
    private CreateOrderClient createOrderClient;

    private CustomerType customer;
    private LineItemsType lineItems;

    @Before
    public void setUp() throws Exception {
        ObjectFactory factory = new ObjectFactory();

        customer = factory.createCustomerType();
        customer.setFirstName("John");
        customer.setLastName("Doe");

        ProductType product1 = factory.createProductType();
        product1.setId("1");

        LineItemType lineItem1 = factory.createLineItemType();
        lineItem1.setProduct(product1);
        lineItem1.setQuantity(BigInteger.valueOf(2));

        lineItems = factory.createLineItemsType();
        lineItems.getLineItem().add(lineItem1);
    }

    @Test
    public void testCreateOrder() {
        assertThat(createOrderClient.createOrder(customer, lineItems)
                .getConfirmationId()).isEqualTo("gero et");
    }
}
