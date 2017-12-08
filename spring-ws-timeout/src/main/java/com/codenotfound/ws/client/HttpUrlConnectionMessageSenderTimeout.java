package com.codenotfound.ws.client;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.springframework.ws.transport.http.HttpTransportConstants;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;

public class HttpUrlConnectionMessageSenderTimeout extends HttpUrlConnectionMessageSender {

  private int timeout = 0;

  public void setTimeout(int timeout) {
    this.timeout = timeout;
  }

  @Override
  protected void prepareConnection(HttpURLConnection connection) throws IOException {
    connection.setRequestMethod(HttpTransportConstants.METHOD_POST);
    connection.setUseCaches(false);
    connection.setDoInput(true);
    connection.setDoOutput(true);
    if (isAcceptGzipEncoding()) {
      connection.setRequestProperty(HttpTransportConstants.HEADER_ACCEPT_ENCODING,
          HttpTransportConstants.CONTENT_ENCODING_GZIP);
    }
    // timeout for creating a connection
    connection.setConnectTimeout(timeout);
    // when you have a connection, timeout the read blocks for
    connection.setReadTimeout(timeout);
  }
}
