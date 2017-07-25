package com.eyelinecom.whoisd.sads2.vk.market.services.cart;

import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;
import com.eyelinecom.whoisd.sads2.vk.market.model.item.CartItem;
import com.eyelinecom.whoisd.sads2.vk.market.model.user.User;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.DBService;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.query.UserQuery;
import org.hibernate.Session;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * author: Artem Voronov
 */
public class CartService {
  private final DBService db;

  public CartService(DBService db) {
    this.db = db;
  }

  private static User getOrCreateUser(String userId, Session s) {
    User user = (User) UserQuery.byUserId(userId, s).uniqueResult();

    if (user == null) {
      Cart cart = new Cart();
      user = new User();
      user.setUserId(userId);
      user.setCart(cart);
      s.save(cart);
      s.save(user);
      cart.setUser(user);
    }

    return user;
  }

  public Cart getCartItems(String userId) {
    return db.tx(s -> {
      User user = getOrCreateUser(userId, s);
      return user.getCart();
    });
  }

  public void addToCart(String userId, Integer vkItemId, Integer quantity) {
    db.vtx(s -> {
      User user = getOrCreateUser(userId, s);

      Cart userCart = user.getCart();
      List<CartItem> items = userCart.getItems();

      Set<Integer> vkItemIds = items.stream().map(CartItem::getVkItemId).collect(Collectors.toSet());
      if (!vkItemIds.contains(vkItemId)) {
        CartItem item = new CartItem();
        item.setVkItemId(vkItemId);
        item.setQuantity(quantity);
        item.setCart(user.getCart());
        s.save(item);
        items.add(item);
      } else {
        CartItem exists = items.stream().filter(it-> vkItemId.equals(it.getVkItemId()))
          .findFirst().orElseThrow(() -> new IllegalStateException("Illegal state of cart: missed vkItemId '"+vkItemId+"', for cart of user '"+userId+"'"));

        exists.setQuantity(exists.getQuantity() + quantity);
      }
    });
  }

  public Cart removeFromCart(String userId, Integer vkItemId) {
    return db.tx(s -> {
      User user = getOrCreateUser(userId, s);

      Cart userCart = user.getCart();
      List<CartItem> items = userCart.getItems();

      for (Iterator<CartItem> iterator = items.iterator(); iterator.hasNext();) {
        CartItem currentItem = iterator.next();
        if (vkItemId.equals(currentItem.getVkItemId())) {
          s.delete(currentItem);
          iterator.remove();
        }
      }
      return userCart;
    });
  }
}
