package com.reportgenerator.reports.exception;

/** @author ppattan */
public class BusinessException extends Exception {

  /** */
  private static final long serialVersionUID = 1L;

  private String errorCode;
  private String systemMessage;
  private Throwable throwable;

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public String getSystemMessage() {
    return systemMessage;
  }

  public void setSystemMessage(String systemMessage) {
    this.systemMessage = systemMessage;
  }

  public Throwable getThrowable() {
    return throwable;
  }

  public void setThrowable(Throwable throwable) {
    this.throwable = throwable;
  }

  public BusinessException(String errorCode, String systemMessage) {
    super();
    this.errorCode = errorCode;
    this.systemMessage = systemMessage;
  }

  public BusinessException(String errorCode, String systemMessage, Throwable throwable) {
    super();
    this.errorCode = errorCode;
    this.systemMessage = systemMessage;
    this.throwable = throwable;
  }
}
