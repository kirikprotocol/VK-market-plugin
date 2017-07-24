package com.eyelinecom.whoisd.sads2.vk.market.model.cart;

import com.eyelinecom.whoisd.sads2.vk.market.model.item.CartItem;
import com.eyelinecom.whoisd.sads2.vk.market.model.user.User;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * author: Artem Voronov
 */
@Entity(name="cart")
@Table(name="carts")
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "cart")
  private List<CartItem> items = new LinkedList<>();

  @OneToOne(fetch = FetchType.EAGER, mappedBy = "cart")
  private User user;

  public Integer getId() {
    return id;
  }

  public List<CartItem> getItems() {
    return items;
  }

  public void setItems(List<CartItem> items) {
    this.items = items;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Transient
  public void add(CartItem item) {
    items.add(item);
  }
}
