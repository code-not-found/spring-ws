package com.codenotfound.order.endpoint;

import static org.springframework.ws.test.server.RequestCreators.withPayload;

import java.util.Collections;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.ws.test.server.ResponseMatchers;
import org.springframework.xml.transform.StringSource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreateOrderEndpointMockTest {

    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    @Before
    public void setUp() throws Exception {
        // create the mock client
        mockClient = MockWebServiceClient
                .createClient(applicationContext);
    }

    @Test
    public void testCreateOrder() throws XPathExpressionException {
        Source requestPayload = new StringSource(
                "<ns2:order xmlns:ns2=\"http://codenotfound.com/types/order\">"
                        + "<ns2:customer><ns2:firstName>John</ns2:firstName>"
                        + "<ns2:lastName>Doe</ns2:lastName>"
                        + "</ns2:customer><ns2:lineItems><ns2:lineItem>"
                        + "<ns2:product>" + "<ns2:id>2</ns2:id>"
                        + "<ns2:name>batman action figure</ns2:name>"
                        + "</ns2:product>"
                        + "<ns2:quantity>1</ns2:quantity>"
                        + "</ns2:lineItem>" + "</ns2:lineItems>"
                        + "</ns2:order>");

        Map<String, String> namespaces = Collections.singletonMap("ns1",
                "http://codenotfound.com/types/order");

        mockClient.sendRequest(withPayload(requestPayload))
                .andExpect(ResponseMatchers
                        .xpath("/ns1:orderConfirmation/ns1:confirmationId",
                                namespaces)
                        .exists());
    }
}
