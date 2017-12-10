package com.codenotfound.ws.client;

import java.util.Base64;

public class BasicAuthenticationUtil {

  private BasicAuthenticationUtil() {}

  public static String generateBasicAutenticationHeader(String userName, String userPassword) {
    byte[] encodedBytes = Base64.getEncoder().encode((userName + ":" + userPassword).getBytes());
    return "Basic " + new String(encodedBytes);
  }
}
