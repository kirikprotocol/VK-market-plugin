package com.eyelinecom.whoisd.sads2.vk.market.services.model;

/**
 * author: Artem Voronov
 */
public class OrderItemDetailed {
  private final String name;
  private final int quantity;
  private final String price;

  public OrderItemDetailed(String name, int quantity, String price) {
    this.name = name;
    this.quantity = quantity;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }

  public String getPrice() {
    return price;
  }
}
