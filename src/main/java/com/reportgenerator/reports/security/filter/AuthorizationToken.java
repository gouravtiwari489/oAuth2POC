package com.reportgenerator.reports.security.filter;

import com.reportgenerator.reports.dto.OsiUserDTO;
import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AuthorizationToken implements Serializable {

  /** */
  private static final long serialVersionUID = 1L;

  private static final Integer DEFAULT_TTL = (60 * 60 * 24 * 30); // 30days

  private String token;

  private Date timeCreated;

  private Date expirationDate;

  private Date lastAccessedDate;

  private Integer ttl;

  private OsiUserDTO osiUserDTO;

  public OsiUserDTO getOsiUserDTO() {
    return osiUserDTO;
  }

  /*public AuthorizationToken(OsiUserDTO user) {
  	this(user, DEFAULT_TTL);
  }*/

  public AuthorizationToken(OsiUserDTO osiUserDTO, String token) {
    this.osiUserDTO = osiUserDTO;
    this.timeCreated = new Date();
    this.lastAccessedDate = new Date();
    this.token = token;
  }

  public AuthorizationToken(String token, OsiUserDTO osiUserDTO, Integer timeToLiveInSeconds) {
    this.token = token;
    this.osiUserDTO = osiUserDTO;
    this.timeCreated = new Date();
    this.lastAccessedDate = new Date();
    this.expirationDate = new Date(System.currentTimeMillis() + (timeToLiveInSeconds * 1000L));
    this.ttl = timeToLiveInSeconds;
  }

  public void resetExpirationDate() {
    this.lastAccessedDate = new Date();
    this.expirationDate = new Date(System.currentTimeMillis() + (ttl * 1000L));
  }

  public Date getLastAccessedDate() {
    return lastAccessedDate;
  }

  public void setLastAccessedDate(Date lastAccessedDate) {
    this.lastAccessedDate = lastAccessedDate;
  }

  public boolean hasExpired() {
    return this.expirationDate != null && this.expirationDate.before(new Date());
  }

  public String getToken() {
    return token;
  }

  public Date getTimeCreated() {
    return timeCreated;
  }

  @Override
  public String toString() {
    return "AuthorizationToken [token="
        + token
        + ", timeCreated="
        + timeCreated
        + ", expirationDate="
        + expirationDate
        + ", lastAccessedDate="
        + lastAccessedDate
        + ", ttl="
        + ttl
        + ", OsiUserDTO="
        + osiUserDTO
        + "]";
  }
}
