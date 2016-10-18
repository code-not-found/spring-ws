package com.codenotfound;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.codenotfound.client.StockQuoteClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringWsApplicationTests {

    @Autowired
    private StockQuoteClient stockQuoteClient;

    @Test
    public void getLastTradePrice() {

        assertThat(stockQuoteClient.getLastTradePrice("XYZ"))
                .isEqualTo(100);
    }
}
