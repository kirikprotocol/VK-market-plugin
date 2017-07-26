package com.eyelinecom.whoisd.sads2.vk.market.services.notification;


import com.eyelinecom.whoisd.sads2.vk.market.services.model.OrderDetailed;

import java.util.Locale;

/**
 * author: Artem Voronov
 */
public interface NotificationProvider {

  void registerNewOrderNotification(Locale locale, OrderDetailed orderDetailed);

}
