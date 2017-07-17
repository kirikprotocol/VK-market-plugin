package com.eyelinecom.whoisd.sads2.vk.market.service.shorturl;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * author: Denis Enenko
 * date: 17.07.17
 */
public class ShortUrlService implements UrlResolver {

  private final Logger log;

  /**
   * Маппинг из короткого URL в полный URL.
   */
  private final Map<String, String> map = new HashMap<>();


  public ShortUrlService(Logger log) {
    this.log = log;
  }

  public synchronized String getShortUrl(String url) {
    try { Thread.sleep(1); } catch(InterruptedException ignored) {}

    String shortUrl = "/" + System.currentTimeMillis();
    map.put(shortUrl, url);

    if(log.isDebugEnabled())
      log.debug("URL -> short URL: URL = " + url + ", short URL = " + shortUrl);

    return shortUrl;
  }

  public String getUrl(String shortUrl) {
    String url = map.get(shortUrl);

    if(log.isDebugEnabled())
      log.debug("Short URL -> URL: short URL = " + shortUrl + ", URL = " + url);

    return url;
  }

}
