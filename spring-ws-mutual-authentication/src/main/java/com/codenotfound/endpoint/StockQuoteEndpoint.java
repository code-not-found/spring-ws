package com.codenotfound.endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.example.stockquote.ObjectFactory;
import com.example.stockquote.TradePrice;
import com.example.stockquote.TradePriceRequest;

@Endpoint
public class StockQuoteEndpoint {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(StockQuoteEndpoint.class);

    private static final String NAMESPACE_URI = "http://example.com/stockquote.xsd";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "TradePriceRequest")
    @ResponsePayload
    public TradePrice getLastTradePrice(
            @RequestPayload TradePriceRequest request) {

        LOGGER.info(
                "Endpoint received TradePriceRequest[tickerSymbol={}]",
                request.getTickerSymbol());

        float price = 100;

        ObjectFactory factory = new ObjectFactory();
        TradePrice response = factory.createTradePrice();
        response.setPrice(price);

        LOGGER.info("Endpoint sending TradePrice[price={}]",
                response.getPrice());
        return response;
    }
}
