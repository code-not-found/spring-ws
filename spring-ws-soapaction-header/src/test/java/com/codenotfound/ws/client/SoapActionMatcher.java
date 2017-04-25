package com.codenotfound.ws.client;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.support.SoapUtils;
import org.springframework.ws.test.client.RequestMatcher;

public class SoapActionMatcher implements RequestMatcher {

  private final String expectedSoapAction;

  public SoapActionMatcher(String expectedSoapAction) {
    this.expectedSoapAction = SoapUtils.escapeAction(expectedSoapAction);
  }

  @Override
  public void match(URI uri, WebServiceMessage webServiceMessage)
      throws IOException, AssertionError {
    assertThat(webServiceMessage).isInstanceOf(SoapMessage.class);

    SoapMessage soapMessage = (SoapMessage) webServiceMessage;
    assertThat(soapMessage.getSoapAction()).isEqualTo(expectedSoapAction);
  }
}
