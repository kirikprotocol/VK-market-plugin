package com.eyelinecom.whoisd.sads2.vk.market.model.item

import com.eyelinecom.whoisd.sads2.vk.market.model.DBTestBase
import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart
import com.eyelinecom.whoisd.sads2.vk.market.model.cart.CartTest
import com.eyelinecom.whoisd.sads2.vk.market.model.user.UserTest

import static org.junit.Assert.assertNotNull

/**
 * author: Artem Voronov
 */
class CartItemTest extends DBTestBase {

  static CartItem createCorrectCartItem(Map overrides = [:]) {
    def defaultFields = [
      vkItemId : 66886,
      quantity : 1,
      cart: CartTest.createCorrectCart()
    ]
    return new CartItem(defaultFields + overrides)
  }

  void testSaveAndLoad() {
    def cart = CartTest.createCorrectCart()
    def user = UserTest.createCorrectUser(cart: cart)
    def item = createCorrectCartItem(cart: cart)

    tx { s ->
      s.save(cart)
      cart.user = user
      s.save(user)
      s.save(item)
      cart.items << item
    }

    assertNotNull(item.id)

    def item1 = tx { session -> session.get(CartItem, item.id) as CartItem}

    assertNotNull(item1)
    assertCartItemsEquals(item, item1)

    def cart1 = tx { session -> session.get(Cart, cart.id) as Cart}

    assertNotNull(cart1)
    assertEquals(cart1.items.size(), 1)
  }

  static void assertCartItemsEquals(CartItem expected, CartItem actual) {
    assertEquals expected.vkItemId, actual.vkItemId
    assertEquals expected.quantity, actual.quantity
    CartTest.assertCartsEquals expected.cart, actual.cart
  }
}
