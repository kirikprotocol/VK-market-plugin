package com.eyelinecom.whoisd.sads2.vk.market.model.item

import com.eyelinecom.whoisd.sads2.vk.market.model.DBTestBase
import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart
import com.eyelinecom.whoisd.sads2.vk.market.model.cart.CartTest
import com.eyelinecom.whoisd.sads2.vk.market.model.user.User
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

    //adding item
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

    //removing item
    vtx { s ->
      def loadedUser = s.get(User, user.id) as User

      for (Iterator<CartItem> iterator = loadedUser.cart.items.iterator(); iterator.hasNext();) {
        CartItem currentItem = iterator.next()
        if (item1.vkItemId == currentItem.vkItemId) {
          s.delete(currentItem)
          iterator.remove()
        }
      }
    }

    def item2 = tx { session -> session.get(CartItem, item.id) as CartItem}
    def cart2 = tx { session -> session.get(Cart, cart.id) as Cart}

    assertNull(item2)
    assertNotNull(cart2)
    assertEquals(cart2.items.size(), 0)
  }

  static void assertCartItemsEquals(CartItem expected, CartItem actual) {
    assertEquals expected.vkItemId, actual.vkItemId
    assertEquals expected.quantity, actual.quantity
    CartTest.assertCartsEquals expected.cart, actual.cart
  }
}
