package com.eyelinecom.whoisd.sads2.vk.market.model.item;

import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;

import javax.persistence.*;

/**
 * author: Artem Voronov
 */
@Entity(name = "cart_item")
@Table(name = "cart_items")
public class CartItem extends Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "cart_id", unique = false, nullable = false)
  private Cart cart;

  public Integer getId() {
    return id;
  }

  public Cart getCart() {
    return cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }
}
