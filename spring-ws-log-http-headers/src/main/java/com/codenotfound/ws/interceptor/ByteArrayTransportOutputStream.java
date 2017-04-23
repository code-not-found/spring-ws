package com.codenotfound.ws.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.ws.transport.TransportOutputStream;

public class ByteArrayTransportOutputStream extends TransportOutputStream {

  private static final String NEW_LINE = System.getProperty("line.separator");

  private ByteArrayOutputStream byteArrayOutputStream;

  @Override
  public void addHeader(String name, String value) throws IOException {
    createOutputStream();
    String header = name + ": " + value + NEW_LINE;
    byteArrayOutputStream.write(header.getBytes());
  }

  @Override
  protected OutputStream createOutputStream() throws IOException {
    if (byteArrayOutputStream == null) {
      byteArrayOutputStream = new ByteArrayOutputStream();
    }
    return byteArrayOutputStream;
  }

  public byte[] toByteArray() {
    return byteArrayOutputStream.toByteArray();
  }
}
