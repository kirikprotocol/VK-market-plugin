package com.eyelinecom.whoisd.sads2.vk.market.model.user;


import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;
import com.eyelinecom.whoisd.sads2.vk.market.model.order.Order;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Пользователь Telegram, Skype, Viber, VK и др. каналов, которые поддерживаются платформой SADS
 */
@Entity(name = "user")
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "user_id", nullable = false, unique = true)
  private String userId; //в SADS это 32-х символьная строка, которая фигурирует в URL, в параметре user_id

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "cart_id", unique = false, nullable = false)
  private Cart cart;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private List<Order> orders = new LinkedList<>();

  public Integer getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }
}
