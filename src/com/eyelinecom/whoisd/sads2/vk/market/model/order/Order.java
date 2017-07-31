package com.eyelinecom.whoisd.sads2.vk.market.model.order;

import com.eyelinecom.whoisd.sads2.vk.market.model.item.CartItem;
import com.eyelinecom.whoisd.sads2.vk.market.model.item.OrderItem;
import com.eyelinecom.whoisd.sads2.vk.market.model.user.User;

import javax.persistence.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author: Artem Voronov
 */
@Entity(name = "order")
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Integer id;

  @Column(name = "phone_number", nullable = false, unique = false)
  private String phoneNumber;

  @Column(name = "merchant_email", nullable = false, unique = false)
  private String merchantEmail;

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
  private List<OrderItem> items = new LinkedList<>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", unique = false, nullable = false)
  private User user;

  public Integer getId() {
    return id;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getMerchantEmail() {
    return merchantEmail;
  }

  public void setMerchantEmail(String merchantEmail) {
    this.merchantEmail = merchantEmail;
  }

  public List<OrderItem> getItems() {
    return items;
  }

  public void setItems(List<OrderItem> items) {
    this.items = items;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Transient
  public boolean isEmpty() {
    return items == null || items.isEmpty();
  }

  @Transient
  public Map<Integer, Integer> getItemQuantities() {
    if (isEmpty())
      return Collections.emptyMap();

    return items.stream().collect(Collectors.toMap(OrderItem::getVkItemId, OrderItem::getQuantity));
  }

}
