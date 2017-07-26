package com.eyelinecom.whoisd.sads2.vk.market.services;

import com.eyeline.utils.config.xml.XmlConfigSection;
import com.eyelinecom.whoisd.sads2.vk.market.services.cart.CartService;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.DBService;
import com.eyelinecom.whoisd.sads2.vk.market.services.notification.MailService;
import com.eyelinecom.whoisd.sads2.vk.market.services.notification.MailServiceImpl;
import com.eyelinecom.whoisd.sads2.vk.market.services.notification.NotificationProvider;
import com.eyelinecom.whoisd.sads2.vk.market.services.notification.NotificationService;
import com.eyelinecom.whoisd.sads2.vk.market.services.order.OrderService;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.ShortUrlService;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * author: Denis Enenko
 * date: 29.06.17
 */
public class Services {

  private final static Logger log = Logger.getLogger("VK_MARKET_PLUGIN");

  private final UrlResolver urlResolver;
  private final DBService dbService;
  private final CartService cartService;
  private final NotificationProvider notificationProvider;
  private final OrderService orderService;

  public Services(XmlConfigSection config) throws ServicesException {
    this.urlResolver = new ShortUrlService(log);
    this.dbService = initDBService(config);
    this.cartService = new CartService(dbService);
    this.notificationProvider = initNotificationProvider(config, Executors.newSingleThreadExecutor());
    this.orderService = new OrderService(dbService);
  }

  private static DBService initDBService(XmlConfigSection config) throws ServicesException {
    try {
      Properties hibernateProperties = config.getSection("db").toProperties(".");
      String hibernateAddCfg = "/" + DBService.class.getPackage().getName().replace('.', '/') + "/hibernate.cfg.xml";
      return new DBService(hibernateProperties, hibernateAddCfg);
    }
    catch(Exception e) {
      throw new ServicesException("Error during DBService initialization.", e);
    }
  }

  private static NotificationProvider initNotificationProvider(XmlConfigSection config, Executor executor) throws ServicesException {
    try {
      XmlConfigSection mailSection = config.getSection("mail");
      Properties mailProperties = mailSection.toProperties(".");
      MailService mailService = new MailServiceImpl(mailProperties, executor);
      return new NotificationService(mailService);
    }
    catch(Exception e) {
      throw new ServicesException("Error during MailService initialization.", e);
    }
  }

  public UrlResolver getUrlResolver() {
    return urlResolver;
  }

  public DBService getDbService() {
    return dbService;
  }

  public CartService getCartService() {
    return cartService;
  }

  public NotificationProvider getNotificationProvider() {
    return notificationProvider;
  }

  public OrderService getOrderService() {
    return orderService;
  }
}