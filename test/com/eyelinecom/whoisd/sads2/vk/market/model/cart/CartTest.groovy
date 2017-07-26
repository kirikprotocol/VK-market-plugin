package com.eyelinecom.whoisd.sads2.vk.market.model.cart

import com.eyelinecom.whoisd.sads2.vk.market.model.DBTestBase
import com.eyelinecom.whoisd.sads2.vk.market.model.user.UserTest

import static org.junit.Assert.assertNotNull

/**
 * author: Artem Voronov
 */
class CartTest extends DBTestBase {

  static Cart createCorrectCart(Map overrides = [:]) {
    def defaultFields = [
      items: new LinkedList()
    ]
    return new Cart(defaultFields + overrides)
  }

  void testSaveAndLoad() {
    def cart = createCorrectCart()
    def user = UserTest.createCorrectUser(cart: cart)

    tx { s ->
      s.save(cart)
      s.save(user)
      cart.user = user
    }

    assertNotNull(cart.id)

    def cart1 = tx { session -> session.get(Cart, cart.id) as Cart}

    assertNotNull(cart1)
    assertCartsEquals(cart, cart1)
  }

  static void assertCartsEquals(Cart expected, Cart actual) {
    UserTest.assertUsersEquals expected.user, actual.user
  }
}
