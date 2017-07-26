package com.eyelinecom.whoisd.sads2.vk.market.services.model;

import java.util.List;

/**
 * author: Artem Voronov
 */
public class Order {
  private final int id;
  private final String userPhoneNumber;
  private final String merchantEmail;
  private final List<OrderItem> items;
  private final String totalCost;

  public Order(int id, String userPhoneNumber, String merchantEmail, List<OrderItem> items, String totalCost) {
    this.id = id;
    this.userPhoneNumber = userPhoneNumber;
    this.merchantEmail = merchantEmail;
    this.items = items;
    this.totalCost = totalCost;
  }

  public int getId() {
    return id;
  }

  public String getUserPhoneNumber() {
    return userPhoneNumber;
  }

  public String getMerchantEmail() {
    return merchantEmail;
  }

  public List<OrderItem> getItems() {
    return items;
  }

  public String getTotalCost() {
    return totalCost;
  }
}
