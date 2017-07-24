package com.eyelinecom.whoisd.sads2.vk.market.services;

import com.eyeline.utils.config.xml.XmlConfigSection;
import com.eyelinecom.whoisd.sads2.vk.market.services.cart.CartService;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.DBService;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.ShortUrlService;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * author: Denis Enenko
 * date: 29.06.17
 */
public class Services {

  private final static Logger log = Logger.getLogger("VK_MARKET_PLUGIN");

  private final UrlResolver urlResolver;
  private final DBService dbService;
  private final CartService cartService;

  public Services(XmlConfigSection config) throws ServicesException {
    this.urlResolver = new ShortUrlService(log);
    this.dbService = initDBService(config);
    this.cartService = new CartService(dbService);
  }

  private DBService initDBService(XmlConfigSection config) throws ServicesException {
    try {
      Properties hibernateProperties = config.getSection("db").toProperties(".");
      String hibernateAddCfg = "/" + DBService.class.getPackage().getName().replace('.', '/') + "/hibernate.cfg.xml";
      return new DBService(hibernateProperties, hibernateAddCfg);
    }
    catch(Exception e) {
      throw new ServicesException("Error during DBService initialization.", e);
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
}