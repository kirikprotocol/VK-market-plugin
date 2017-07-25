package com.eyelinecom.whoisd.sads2.vk.market.services.notification;

import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.Executor;

public class MailServiceImpl implements MailService {

  private final static Logger logger = Logger.getLogger("MAIL_SERVICE");

  private final Executor mailSender;
  private MailSettings settings;


  public MailServiceImpl(Properties mailProperties, Executor executor) {
    this.settings = new MailSettings(mailProperties);
    this.mailSender = executor;
  }

  @Override
  public void sendMail(String emailTo, MailEntity mail) {
    try {
      mailSender.execute(new MailTask(emailTo, mail));
      if(logger.isDebugEnabled()) {
        logger.debug("Mail sent to " + emailTo + " successfully. Subject: " + mail.getSubject() + ". Message: " + mail.getMessage());
      }
    }
    catch(Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  @Override
  public void sendMail(String emailTo, String emailFrom, String emailFromName, MailEntity mail) {
    try {
      mailSender.execute(new MailTask(emailTo, emailFrom, emailFromName, mail));
      if(logger.isDebugEnabled()) {
        logger.debug("Mail sent to " + emailTo + " successfully. From: " + emailFrom + ". Name: " + emailFromName + ". Subject: " + mail.getSubject() + ". Message: " + mail.getMessage());
      }
    }
    catch(Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  private class MailTask implements Runnable {
    private String emailTo;
    private String emailFrom;
    private String emailFromName;
    private MailEntity mail;


    public MailTask(String emailTo, MailEntity mail) {
      this.emailTo = emailTo;
      this.mail = mail;

      Properties mailProperties = settings.getMailProperties();

      this.emailFrom = mailProperties.getProperty("mail.from");
      this.emailFromName = mailProperties.getProperty("mail.from.name");
    }

    public MailTask(String emailTo, String emailFrom, String emailFromName, MailEntity mail) {
      this.emailTo = emailTo;
      this.emailFrom = emailFrom;
      this.emailFromName = emailFromName;
      this.mail = mail;
    }

    @Override
    public void run() {
      try {
        Properties mailProperties = settings.getMailProperties();
        Session session = Session.getDefaultInstance(mailProperties);
        MimeMessage message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
        message.setFrom(new InternetAddress(emailFrom, emailFromName));
        message.setSubject(mail.getSubject(), "UTF-8");
        message.setContent(mail.getMessage(), "text/html; charset=UTF-8");

        Transport transport = session.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
      }
      catch(Exception e) {
        logger.error("Error sending mail to " + emailTo + ". Subject: " + mail.getSubject() + ". Message: " + mail.getMessage(), e);
      }
    }
  }

}
