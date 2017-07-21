package com.eyelinecom.whoisd.sads2.vk.market.model.item

import com.eyelinecom.whoisd.sads2.vk.market.model.DBTestBase
import com.eyelinecom.whoisd.sads2.vk.market.model.order.Order
import com.eyelinecom.whoisd.sads2.vk.market.model.order.OrderTest


/**
 * author: Artem Voronov
 */
class OrderItemTest extends DBTestBase {

  static OrderItem createCorrectOrderItem(Map overrides = [:]) {
    def defaultFields = [
      vkItemId : 66886,
      quantity : 1,
      order: OrderTest.createCorrectOrder()
    ]
    return new OrderItem(defaultFields + overrides)
  }

  void testSaveAndLoad() {
    def order = OrderTest.createCorrectOrder()
    def item = createCorrectOrderItem(order: order)

    tx { s ->
      s.save(order.user.cart)
      s.save(order.user)
      s.save(order)
      s.save(item)
      order.items << item
    }

    assertNotNull(item.id)

    def item1 = tx { session -> session.get(OrderItem, item.id) as OrderItem}

    assertNotNull(item1)
    assertOrderItemsEquals(item, item1)

    def order1 = tx { session -> session.get(Order, order.id) as Order}

    assertNotNull(order1)
    assertEquals(order1.items.size(), 1)
  }

  static void assertOrderItemsEquals(OrderItem expected, OrderItem actual) {
    assertEquals expected.vkItemId, actual.vkItemId
    assertEquals expected.quantity, actual.quantity
    OrderTest.assertOrdersEquals expected.order, actual.order
  }
}
