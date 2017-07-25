package com.eyelinecom.whoisd.sads2.vk.market.services.notification;

import java.util.Locale;

/**
 * author: Artem Voronov
 */
public interface NotificationProvider {

  void registerNewOrderNotification(Locale locale, String phoneNumber, String merchantEmail);

}
