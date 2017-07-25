package com.eyelinecom.whoisd.sads2.vk.market.services.notification;

/**
 * author: Denis Enenko, Artem Voronov
 * date: 25.03.2015
 */
public interface MailService {

  void sendMail(String emailTo, MailEntity mail);

  void sendMail(String emailTo, String emailFrom, String emailFromName, MailEntity mail);

}
