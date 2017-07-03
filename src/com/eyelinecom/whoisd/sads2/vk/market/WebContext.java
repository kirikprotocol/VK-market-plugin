package com.eyelinecom.whoisd.sads2.vk.market;


import com.eyelinecom.whoisd.sads2.vk.market.service.Services;

import javax.enterprise.context.ApplicationScoped;

/**
 * author: Denis Enenko
 * date: 29.06.17
 */
@ApplicationScoped
public class WebContext {

  private static Services services;


  static synchronized void init(Services services) {
    if(WebContext.services == null)
      WebContext.services = services;
  }

}