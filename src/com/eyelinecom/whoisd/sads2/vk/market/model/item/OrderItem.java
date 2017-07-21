package com.eyelinecom.whoisd.sads2.vk.market.model.item;

import com.eyelinecom.whoisd.sads2.vk.market.model.order.Order;

import javax.persistence.*;

/**
 * author: Artem Voronov
 */
@Entity(name = "order_item")
@Table(name = "order_items")
public class OrderItem extends Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id", unique = false, nullable = false)
  private Order order;

  public Integer getId() {
    return id;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }
}
