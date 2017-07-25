package com.eyelinecom.whoisd.sads2.vk.market.services.notification;

/**
 * author: Denis Enenko
 * date: 25.03.2015
 */
public class MailEntity {

  private String subject;
  private String message;


  public MailEntity(String subject, String message) {
    this.subject = subject;
    this.message = message;
  }

  String getSubject() {
    return subject;
  }

  String getMessage() {
    return message;
  }

}
