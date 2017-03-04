package com.codenotfound.order.client;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.ws.test.client.RequestMatchers.payload;
import static org.springframework.ws.test.client.ResponseCreators.withPayload;

import java.math.BigInteger;

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

import com.codenotfound.types.order.CustomerType;
import com.codenotfound.types.order.LineItemType;
import com.codenotfound.types.order.LineItemsType;
import com.codenotfound.types.order.ObjectFactory;
import com.codenotfound.types.order.ProductType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateOrderClientMockTest {

    @Autowired
    private CreateOrderClient createOrderClient;
    @Autowired
    private WebServiceTemplate webServiceTemplate;

    private MockWebServiceServer mockWebServiceServer;

    private CustomerType customer;
    private LineItemsType lineItems;

    @Before
    public void setUp() throws Exception {
        // create the mock server
        mockWebServiceServer = MockWebServiceServer
                .createServer(webServiceTemplate);

        // create customer & lineItems objects
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
        Source requestPayload = new StringSource(
                "<ns2:order xmlns:ns2=\"http://codenotfound.com/types/order\">"
                        + "<ns2:customer><ns2:firstName>Jane</ns2:firstName>"
                        + "<ns2:lastName>Doe</ns2:lastName>"
                        + "</ns2:customer><ns2:lineItems><ns2:lineItem>"
                        + "<ns2:product>" + "<ns2:id>2</ns2:id>"
                        + "<ns2:name>superman action figure</ns2:name>"
                        + "</ns2:product>"
                        + "<ns2:quantity>1</ns2:quantity>"
                        + "</ns2:lineItem>" + "</ns2:lineItems>"
                        + "</ns2:order>");

        Source responsePayload = new StringSource(
                "<ns2:orderConfirmation xmlns:ns2=\"http://codenotfound.com/types/order\">"
                        + "<ns2:confirmationId>5678</ns2:confirmationId>"
                        + "</ns2:orderConfirmation>");

        mockWebServiceServer.expect(payload(requestPayload))
                .andRespond(withPayload(responsePayload));

        assertThat(createOrderClient.createOrder(customer, lineItems)
                .getConfirmationId()).isEqualTo("5678");

        mockWebServiceServer.verify();
    }
}
