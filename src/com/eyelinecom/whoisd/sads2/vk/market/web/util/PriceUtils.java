package com.eyelinecom.whoisd.sads2.vk.market.web.util;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;

import java.util.List;
import java.util.Map;

/**
 * author: Artem Voronov
 */
public class PriceUtils {

  public static int getTotalCost(List<Item> items, Map<Integer, Integer> itemQuantities) {
    return items.stream().map(it -> Integer.parseInt(it.getPrice().getPrice()) * itemQuantities.get(it.getId())).mapToInt(Integer::intValue).sum();
  }

  public static String convert(Integer totalCost) {
    if(totalCost == null)
      throw new IllegalArgumentException("null total cost");

    int i = totalCost / 100;
    int f = totalCost % 100;
    return "" + i + "." + (f < 10 ? "0" + f : f);
  }
}
