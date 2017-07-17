package com.eyelinecom.whoisd.sads2.vk.market.web;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Denis Enenko
 * date: 13.07.17
 */
public enum Protocol {

  TELEGRAM,
  //Неподдерживаемые протоколы:
  /*
  FACEBOOK,
  VIBER,
  VK,
  SKYPE
  */;

  private static List<String> names = new ArrayList<>();

  static {
    for(Protocol protocol : Protocol.values()) {
      names.add(protocol.name());
    }
  }

  public static Protocol getValue(String name) {
    if(!names.contains(name))
      return null;

    return valueOf(name);
  }

  public static String getValuesString() {
    StringBuilder sb = new StringBuilder();

    for(String name : names) {
      if(sb.length() > 0) sb.append(", ");
      sb.append(name);
    }

    return sb.toString();
  }

}
