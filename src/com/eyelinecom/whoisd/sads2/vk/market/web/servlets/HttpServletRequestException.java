package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

/**
 * author: Denis Enenko
 * date: 04.07.17
 */
class HttpServletRequestException extends Exception {

  private final int httpStatus;


  HttpServletRequestException(String message, int httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  HttpServletRequestException(String message, Throwable cause, int httpStatus) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }

  int getHttpStatus() {
    return httpStatus;
  }

}