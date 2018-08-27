package com.reportgenerator.reports.security.filter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class InMemoryTokenStore implements AuthTokenStore {

  private static final Logger logger = LoggerFactory.getLogger(InMemoryTokenStore.class);

  // An authentication token storage which stores <service_key, auth_token>.
  private final Map<String, AuthorizationToken> authorizationTokensStorage =
      new ConcurrentHashMap<String, AuthorizationToken>();

  @Override
  public void storeToken(AuthorizationToken authorizationToken) {
    authorizationTokensStorage.put(authorizationToken.getToken(), authorizationToken);
  }

  @Override
  public AuthorizationToken retrieveToken(String token) {
    AuthorizationToken authorizationToken = authorizationTokensStorage.get(token);
    if (authorizationToken != null) authorizationToken.resetExpirationDate();
    return authorizationToken;
  }
  
  public String getUserOrganization(String token) {
	    AuthorizationToken authorizationToken = authorizationTokensStorage.get(token);
	    return authorizationToken.getOsiUserDTO().getOrgCode();
	  }

  @Override
  public AuthorizationToken removeToken(String token) {
    return authorizationTokensStorage.remove(token);
  }

  public void removeAllTokens() {
    authorizationTokensStorage.clear();
  }

  @Override
  public void clearExpiredTokens() {
    logger.info("clearing user expired tokens. Token size: " + authorizationTokensStorage.size());
    if (authorizationTokensStorage.size() > 0) {
      for (Map.Entry<String, AuthorizationToken> tokenMapEntry :
          authorizationTokensStorage.entrySet()) {

        if (tokenMapEntry.getValue().hasExpired()) {
          logger.info("Removing token: " + tokenMapEntry.getValue());
          //System.out.println("Removing token: " + tokenMapEntry.getValue());
          authorizationTokensStorage.remove(tokenMapEntry.getKey());
        }
      }
    }
  }
}
