package com.codenotfound.ws.endpoint;

import java.io.IOException;

import javax.xml.transform.Source;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.test.server.RequestCreator;
import org.springframework.ws.test.support.creator.PayloadMessageCreator;

public class SoapActionCreator implements RequestCreator {

    private final Source payload;

    private final String soapAction;

    public SoapActionCreator(Source payload, String soapAction) {
        this.payload = payload;
        this.soapAction = soapAction;

    }

    @Override
    public WebServiceMessage createRequest(
            WebServiceMessageFactory webServiceMessageFactory)
                    throws IOException {

        WebServiceMessage webServiceMessage = new PayloadMessageCreator(
                payload).createMessage(webServiceMessageFactory);

        SoapMessage soapMessage = (SoapMessage) webServiceMessage;
        soapMessage.setSoapAction(soapAction);

        return webServiceMessage;
    }
}
