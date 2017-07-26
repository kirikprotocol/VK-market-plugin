package com.eyelinecom.whoisd.sads2.vk.market.web;

import com.eyelinecom.whoisd.sads2.vk.market.model.order.Order;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Price;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.OrderDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.OrderItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.PriceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author: Artem Voronov
 */
public class Converter {

  public static OrderDetailed convert(Order order, int vkUserId, String vkAccessToken) throws VkMarketServiceException {
    VkMarketService vk = new VkMarketService(vkUserId, vkAccessToken);
    List<Item> itemDescriptions = vk.getItemsById(order);
    Map<Integer, Integer> itemQuantities = order.getItemQuantities();

    List<OrderItemDetailed> userOrderItems = new ArrayList<>(itemDescriptions.size());
    for (Item item : itemDescriptions) {
      Price price = item.getPrice();
      String strPrice = price.getPrice();
      Integer intPrice = Integer.parseInt(strPrice);
      String formattedPrice = PriceUtils.convert(intPrice) + " RUB";

      userOrderItems.add(new OrderItemDetailed(item.getName(), itemQuantities.get(item.getId()), formattedPrice));
    }
    int totalCost = PriceUtils.getTotalCost(itemDescriptions, itemQuantities);
    return new OrderDetailed(order.getId(), order.getPhoneNumber(), order.getMerchantEmail(), userOrderItems, PriceUtils.convert(totalCost) + "RUB");
  }
}
