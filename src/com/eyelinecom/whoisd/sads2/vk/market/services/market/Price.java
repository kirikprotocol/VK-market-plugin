package com.eyelinecom.whoisd.sads2.vk.market.services.market;

/**
 * author: Denis Enenko
 * date: 17.07.17
 */
public class Price {

  private final String price; //цена в копейках
  private final String currency;
  private final String text;


  Price(String price, String currency, String text) {
    this.price = price;
    this.currency = currency;
    this.text = text;
  }

  public String getPrice() {
    return price;
  }

  public String getCurrency() {
    return currency;
  }

  public String getText() {
    return text;
  }

}
