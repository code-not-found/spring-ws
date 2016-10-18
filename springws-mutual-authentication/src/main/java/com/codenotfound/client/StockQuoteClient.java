package com.codenotfound.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;

import com.example.stockquote.ObjectFactory;
import com.example.stockquote.TradePrice;
import com.example.stockquote.TradePriceRequest;

@Component
public class StockQuoteClient {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(StockQuoteClient.class);

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    public float getLastTradePrice(String tickerSymbol) {

        ObjectFactory factory = new ObjectFactory();
        TradePriceRequest tradePriceRequest = factory
                .createTradePriceRequest();

        tradePriceRequest.setTickerSymbol(tickerSymbol);

        LOGGER.info("Client sending TradePriceRequest[tickerSymbol={}]",
                tradePriceRequest.getTickerSymbol());

        TradePrice tradePrice = (TradePrice) webServiceTemplate
                .marshalSendAndReceive(tradePriceRequest);

        LOGGER.info("Client received TradePrice[pirce={}]",
                tradePrice.getPrice());
        return tradePrice.getPrice();
    }
}
