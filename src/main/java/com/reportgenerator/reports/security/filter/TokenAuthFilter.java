package com.reportgenerator.reports.security.filter;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

@Component
public class TokenAuthFilter implements Filter {

  private final String tokenHeader = "Auth_Token";

  @Autowired private AuthTokenStore authTokenStore;

  @Value("${emsurl}")
  private String emsurl;

  @Value("${authToken.skipUrls}")
  private String authenticationSkipUrls;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "DELETE, HEAD, GET, POST, PUT, OPTIONS");
    response.setHeader("Access-Control-Max-Age", "18000");
    response.setHeader("Access-Control-Allow-Headers", "Auth_Token, Content-Type, Accept");

    System.out.println("##############Filter#####################");
    try {
      // Fetch List of Authentication Skip Url's
      /*List<String> authenticationSkipUrlsList = buildAuthenticationSkipUrlsList(authenticationSkipUrls);
          		String url = request.getRequestURL().toString();
          		boolean isNotAuthSkipUrl = validateUrlForAuthentication(url, authenticationSkipUrlsList);
      if(!request.getMethod().equals("OPTIONS") && isNotAuthSkipUrl) {
      	RestTemplate restTemplate = new RestTemplate();
      	String authToken = request.getHeader(this.tokenHeader);
      	final String uri = emsurl+"validate-token";
      	HttpHeaders headers = new HttpHeaders();
      	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      	headers.set("Auth_Token", authToken);
      	HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
      	ResponseEntity<OsiUserDTO> result = restTemplate.exchange(uri, HttpMethod.GET, entity, OsiUserDTO.class);

      	if(result!=null && result.getBody()!=null){
      	    AuthorizationToken token = new AuthorizationToken(result.getBody(), authToken);
      	    authTokenStore.storeToken(token);
      	} else {
      		response.sendRedirect("/api/unauthorized");
      	}
      }*/
    } catch (Exception ex) {
      ex.printStackTrace();
      response.sendRedirect("/api/unauthorized");
      //throw new AuthException(HttpStatus.UNAUTHORIZED.value(), "ERR_2001", env.getProperty("ERR_2001") ,null);
    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    //authTokenStore.removeAllTokens();

  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {}

  private List<String> buildAuthenticationSkipUrlsList(String authenticationSkipUrls) {
    List<String> authenticationSkipUrlsList = new ArrayList<>();

    StringTokenizer strTokens = new StringTokenizer(authenticationSkipUrls, ",");
    while (strTokens.hasMoreTokens()) {
      authenticationSkipUrlsList.add(strTokens.nextToken());
    }

    return authenticationSkipUrlsList;
  }

  private boolean validateUrlForAuthentication(
      String url, List<String> authenticationSkipUrlsList) {
    boolean isNotAuthSkipUrl = true;

    for (String authSkipUrlString : authenticationSkipUrlsList) {
      if (url.contains(authSkipUrlString)) {
        isNotAuthSkipUrl = false;
        break;
      }
    }
    return isNotAuthSkipUrl;
  }
}
