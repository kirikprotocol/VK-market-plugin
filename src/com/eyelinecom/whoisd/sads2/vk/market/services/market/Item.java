package com.eyelinecom.whoisd.sads2.vk.market.services.market;

/**
 * author: Denis Enenko
 * date: 29.06.17
 */
public class Item {

  private final Integer id;
  private final Category category;
  private final String name;
  private final Price price;
  private final String mainPhotoUrl;


  Item(Integer id, Category category, String name, Price price, String mainPhotoUrl) {
    this.id = id;
    this.category = category;
    this.name = name;
    this.price = price;
    this.mainPhotoUrl = mainPhotoUrl;
  }

  public Integer getId() {
    return id;
  }

  public Category getCategory() {
    return category;
  }

  public String getName() {
    return name;
  }

  public Price getPrice() {
    return price;
  }

  public String getMainPhotoUrl() {
    return mainPhotoUrl;
  }

}
