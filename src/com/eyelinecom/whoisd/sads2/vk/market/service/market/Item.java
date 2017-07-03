package com.eyelinecom.whoisd.sads2.vk.market.service.market;

/**
 * author: Denis Enenko
 * date: 29.06.17
 */
public class Item {

  private final Integer id;
  private final String category;
  private final String name;
  private final String price;
  private final String currency;
  private final String mainPhotoUrl;


  Item(Integer id, String category, String name, String price, String currency, String mainPhotoUrl) {
    this.id = id;
    this.category = category;
    this.name = name;
    this.price = price;
    this.currency = currency;
    this.mainPhotoUrl = mainPhotoUrl;
  }

  public Integer getId() {
    return id;
  }

  public String getCategory() {
    return category;
  }

  public String getName() {
    return name;
  }

  public String getPrice() {
    return price;
  }

  public String getCurrency() {
    return currency;
  }

  public String getMainPhotoUrl() {
    return mainPhotoUrl;
  }

}
