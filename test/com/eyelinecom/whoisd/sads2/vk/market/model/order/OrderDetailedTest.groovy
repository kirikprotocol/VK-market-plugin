package com.eyelinecom.whoisd.sads2.vk.market.model.order

import com.eyelinecom.whoisd.sads2.vk.market.model.DBTestBase
import com.eyelinecom.whoisd.sads2.vk.market.model.user.User
import com.eyelinecom.whoisd.sads2.vk.market.model.user.UserTest

import static org.junit.Assert.assertNotNull

/**
 * author: Artem Voronov
 */
class OrderDetailedTest extends DBTestBase {

  static Order createCorrectOrder(Map overrides = [:]) {
    def defaultFields = [
      phoneNumber : '71112223344',
      merchantEmail : "unclebob@example.com",
      user: UserTest.createCorrectUser(),
      items: new LinkedList()
    ]
    return new Order(defaultFields + overrides)
  }

  void testSaveAndLoad() {
    def order = createCorrectOrder()

    tx { s ->
      s.save(order.user.cart)
      s.save(order.user)
      s.save(order)
    }

    assertNotNull(order.id)

    def order1 = tx { s -> s.get(Order, order.id) as Order}

    assertNotNull(order1)
    assertOrdersEquals(order, order1)

    def order2 = tx { s ->
      def user = s.get(User, order.user.id) as User
      return user.orders.get(0)
    }

    assertNotNull(order2)
    assertOrdersEquals(order, order2)
  }

  static void assertOrdersEquals(Order expected, Order actual) {
    assertEquals expected.phoneNumber, actual.phoneNumber
    assertEquals expected.merchantEmail, actual.merchantEmail
    UserTest.assertUsersEquals expected.user, actual.user
  }

}
