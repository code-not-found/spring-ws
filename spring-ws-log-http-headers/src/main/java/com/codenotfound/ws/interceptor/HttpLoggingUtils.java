package com.codenotfound.ws.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.xml.transform.TransformerObjectSupport;

public class HttpLoggingUtils extends TransformerObjectSupport {

  private static final Logger LOGGER = LoggerFactory.getLogger(HttpLoggingUtils.class);

  private static final String NEW_LINE = System.getProperty("line.separator");

  private HttpLoggingUtils() {}

  public static void logMessage(String id, WebServiceMessage webServiceMessage) {
    try {
      ByteArrayTransportOutputStream byteArrayTransportOutputStream =
          new ByteArrayTransportOutputStream();
      webServiceMessage.writeTo(byteArrayTransportOutputStream);

      String httpMessage = new String(byteArrayTransportOutputStream.toByteArray());
      LOGGER.info(NEW_LINE + "----------------------------" + NEW_LINE + id + NEW_LINE
          + "----------------------------" + NEW_LINE + httpMessage + NEW_LINE);
    } catch (Exception e) {
      LOGGER.error("Unable to log HTTP message.", e);
    }
  }
}
