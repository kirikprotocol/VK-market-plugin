package com.eyelinecom.whoisd.sads2.vk.market.services.notification;

import com.eyelinecom.whoisd.sads2.vk.market.services.model.Order;

import java.util.Locale;

/**
 * author: Artem Voronov
 */
public class NotificationService implements NotificationProvider {

  private final MailService mailService;
  private final TemplateService mailTemplateService;

  public NotificationService(MailService mailService) {
    this.mailTemplateService = new TemplateService();
    this.mailService = mailService;
  }

  public void registerNewOrderNotification(Locale locale, Order order) {
    MailEntity mail = mailTemplateService.getOrderInfoTemplate(locale, order);
    mailService.sendMail(order.getMerchantEmail(), mail);
  }
}
