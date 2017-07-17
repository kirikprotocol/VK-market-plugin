package com.eyelinecom.whoisd.sads2.vk.market.service.shorturl;

/**
 * author: Denis Enenko
 * date: 17.07.17
 */
public interface UrlResolver {

  String getShortUrl(String url);

  String getUrl(String shortUrl);

}
