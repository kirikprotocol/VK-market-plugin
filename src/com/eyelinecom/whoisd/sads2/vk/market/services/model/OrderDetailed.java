package com.eyelinecom.whoisd.sads2.vk.market.services.model;

import java.util.List;

/**
 * author: Artem Voronov
 */
public class OrderDetailed {
  private final int id;
  private final String userPhoneNumber;
  private final String merchantEmail;
  private final List<OrderItemDetailed> items;
  private final String totalCost;

  public OrderDetailed(int id, String userPhoneNumber, String merchantEmail, List<OrderItemDetailed> items, String totalCost) {
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

  public List<OrderItemDetailed> getItems() {
    return items;
  }

  public String getTotalCost() {
    return totalCost;
  }
}
