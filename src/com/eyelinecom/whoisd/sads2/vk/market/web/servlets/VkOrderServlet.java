package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;
import com.eyelinecom.whoisd.sads2.vk.market.services.cart.CartService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Price;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.notification.NotificationProvider;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.OrderRegisteredRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.PriceUtils;
import com.eyelinecom.whoisd.sads2.vk.market.web.model.Order;
import com.eyelinecom.whoisd.sads2.vk.market.web.model.OrderItem;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * author: Artem Voronov
 */
public class VkOrderServlet extends VkHttpServlet {

  @Inject
  private UrlResolver urlResolver;

  @Inject
  private CartService cartService;

  @Inject
  private NotificationProvider notificationProvider;

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws VkMarketServiceException, IOException {
    String userId = params.getUserId();
    String phoneNumber = params.getUserInput();
    String merchantEmail = params.getMerchantEmail();
    Locale locale = params.getLocale();

    //TODO: save order at db
    //TODO: convert Order -> UserOrder

    Cart userCart = cartService.getCartItems(userId);
    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    List<Item> itemDescriptions = vk.getItemsById(userCart);
    Map<Integer, Integer> itemQuantities = userCart.getItemQuantities();

    List<OrderItem> userOrderItems = new ArrayList<>(itemDescriptions.size());
    for (Item item : itemDescriptions) {

      Price price = item.getPrice();
      String strPrice = price.getPrice();
      Integer intPrice = Integer.parseInt(strPrice);
      String formattedPrice = PriceUtils.convert(intPrice) + " RUB";

      userOrderItems.add(new OrderItem(item.getName(), itemQuantities.get(item.getId()), formattedPrice));
    }
    int totalCost = PriceUtils.getTotalCost(itemDescriptions, itemQuantities);
    Order userOrder = new Order(123, phoneNumber, merchantEmail, userOrderItems, PriceUtils.convert(totalCost) + "RUB");//TODO: use real id
    notificationProvider.registerNewOrderNotification(locale, userOrder);

    Renderer renderer = new OrderRegisteredRenderer(params.getLocale());
    renderer.render(response, request.getContextPath(), params, urlResolver);
  }


}
