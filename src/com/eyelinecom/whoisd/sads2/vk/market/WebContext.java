package com.eyelinecom.whoisd.sads2.vk.market;


import com.eyelinecom.whoisd.sads2.vk.market.services.Services;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.DBService;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * author: Denis Enenko
 * date: 29.06.17
 */
@ApplicationScoped
public class WebContext {

  private static Services services;
  private static String pushUrl;


  static synchronized void init(Services services, String pushUrl) {
    if(WebContext.services == null) {
      WebContext.services = services;
      WebContext.pushUrl = pushUrl;
    }
  }

  @Produces
  public UrlResolver getUrlResolver() {
    return services.getUrlResolver();
  }

  @Produces
  public DBService getDbService() {
    return services.getDbService();
  }

  public static String getPushUrl() {
    return pushUrl;
  }

}