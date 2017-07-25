package com.eyelinecom.whoisd.sads2.vk.market.services.notification;


import java.util.Properties;
import java.util.regex.Pattern;

/**
 * author: Denis Enenko
 * date: 25.03.2015
 */
class MailSettings {

  private final static Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9]+[\\.\\-_A-Za-z0-9!#$&'*+/=?^_`{|}~:]*@[A-Za-z0-9]+[\\.\\-_A-Za-z0-9!#$&'*+/=?^_`{|}~:]*$");
  private Properties mailProperties;


  public MailSettings(Properties mailProperties) {
    validate(mailProperties);
    this.mailProperties = mailProperties;
  }

  private void validate(Properties properties) {
    String mailHost = (String) properties.get("mail.host");
    if(mailHost == null) throw new RuntimeException("Property mail.host is null");

    String mailUser = (String) properties.get("mail.user");
    if(mailUser == null) throw new RuntimeException("Property mail.user is null");

    String mailPassword = (String) properties.get("mail.password");
    if(mailPassword == null) throw new RuntimeException("Property mail.password is null");

    String mailTransportProtocol = (String) properties.get("mail.transport.protocol");
    if(mailTransportProtocol == null) throw new RuntimeException("Property mail.transport.protocol is null");

    String mailFrom = (String) properties.get("mail.from");
    if(mailFrom == null) throw new RuntimeException("Property mail.from is null");

    String mailFromName = (String) properties.get("mail.from.name");
    if(mailFromName == null) throw new RuntimeException("Property mail.from.name is null");

    if(!EMAIL_PATTERN.matcher(mailFrom).matches()) throw new RuntimeException("Property mail.from has incorrect format");
  }

  public Properties getMailProperties() {
    return mailProperties;
  }

}
