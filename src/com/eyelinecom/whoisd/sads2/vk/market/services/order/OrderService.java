package com.eyelinecom.whoisd.sads2.vk.market.services.order;

import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;
import com.eyelinecom.whoisd.sads2.vk.market.model.item.CartItem;
import com.eyelinecom.whoisd.sads2.vk.market.model.item.OrderItem;
import com.eyelinecom.whoisd.sads2.vk.market.model.order.Order;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.DBService;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Artem Voronov
 */
public class OrderService {

  private final DBService db;

  public OrderService(DBService db) {
    this.db = db;
  }

  public Order registerOrder(Cart cart, String merchantEmail, String phone) {
    return db.tx(s -> {
      Order order = new Order();
      order.setUser(cart.getUser());
      order.setMerchantEmail(merchantEmail);
      order.setPhoneNumber(phone);
      s.save(order);

      List<OrderItem> orderItems = new ArrayList<>(cart.size());

      List<CartItem> cartItems = cart.getItems();
      for (CartItem it : cartItems) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setVkItemId(it.getVkItemId());
        orderItem.setQuantity(it.getQuantity());
        s.save(orderItem);
        orderItems.add(orderItem);
      }

      order.setItems(orderItems);
      return order;
    });
  }
}
