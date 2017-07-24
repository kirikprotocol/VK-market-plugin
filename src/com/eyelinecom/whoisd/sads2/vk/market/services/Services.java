package com.eyelinecom.whoisd.sads2.vk.market.services;

import com.eyeline.utils.config.xml.XmlConfigSection;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.ShortUrlService;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import org.apache.log4j.Logger;

/**
 * author: Denis Enenko
 * date: 29.06.17
 */
public class Services {

  private final static Logger log = Logger.getLogger("VK_MARKET_PLUGIN");

  private final UrlResolver urlResolver;


  public Services(XmlConfigSection config) throws ServicesException {
    urlResolver = new ShortUrlService(log);
  }

  public UrlResolver getUrlResolver() {
    return urlResolver;
  }

}