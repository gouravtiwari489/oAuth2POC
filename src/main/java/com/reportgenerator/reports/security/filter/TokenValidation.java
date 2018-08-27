package com.reportgenerator.reports.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TokenValidation {

  @Autowired private InMemoryTokenStore tokenStore;

  public boolean validateToken(String token) {
    boolean isValidToken = false;
    try {

      if (tokenStore.retrieveToken(token) != null) {
        isValidToken = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return isValidToken;
  }
}
