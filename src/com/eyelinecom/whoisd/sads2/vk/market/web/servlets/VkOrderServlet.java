package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.model.order.Order;
import com.eyelinecom.whoisd.sads2.vk.market.services.cart.CartService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.notification.NotificationProvider;
import com.eyelinecom.whoisd.sads2.vk.market.services.order.OrderService;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Converter;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.OrderRegisteredRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * author: Artem Voronov
 */
public class VkOrderServlet extends VkHttpServlet {

  @Inject
  private UrlResolver urlResolver;

  @Inject
  private CartService cartService;

  @Inject
  private OrderService orderService;

  @Inject
  private NotificationProvider notificationProvider;

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws VkMarketServiceException, IOException {
    String userId = params.getUserId();
    String phoneNumber = params.getUserInput();
    String merchantEmail = params.getMerchantEmail();
    int vkUserId = params.getVkUserId();
    String vkAccessToken = params.getVkAccessToken();
    Locale locale = params.getLocale();

    Order order = orderService.registerOrder(cartService.getCartItems(userId), merchantEmail, phoneNumber);

    //TODO: clean cart

    notificationProvider.registerNewOrderNotification(locale, Converter.convert(order, vkUserId, vkAccessToken));

    Renderer renderer = new OrderRegisteredRenderer(params.getLocale());
    renderer.render(response, request.getContextPath(), params, urlResolver);
  }


}
