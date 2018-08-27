package com.reportgenerator.reports.security.filter;

import java.util.UUID;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {

  /**
   * Generates a JWT token containing username as subject, and userId and role as additional claims.
   * These properties are taken from the specified User object. Tokens validity is infinite.
   *
   * @param u the user for which the token will be generated
   * @return the JWT token
   */
  public String generateToken(String username) {
    String randString = UUID.randomUUID().toString();
    String stringToBeEncoded = username + randString;
    byte[] firstByteArray = Base64.encodeBase64(stringToBeEncoded.getBytes());
    String primaryEncodedString = new String(firstByteArray);
    byte[] secoundByteArray = Base64.encodeBase64(primaryEncodedString.getBytes());
    String secoundaryEncodedString = new String(secoundByteArray);
    return secoundaryEncodedString;
  }
}
