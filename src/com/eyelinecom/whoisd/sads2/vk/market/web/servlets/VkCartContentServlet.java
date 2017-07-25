package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;
import com.eyelinecom.whoisd.sads2.vk.market.model.item.CartItem;
import com.eyelinecom.whoisd.sads2.vk.market.services.cart.CartService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram.CartContentRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInput;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputParser;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * author: Artem Voronov
 */
public class VkCartContentServlet extends VkHttpServlet {

  @Inject
  private UrlResolver urlResolver;

  @Inject
  private CartService cartService;

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws VkMarketServiceException, IOException {
    String userId = params.getUserId();
    UserInput userInput = UserInputParser.parse(params.getUserInput(), userId);
    Cart userCart = cartService.getCartItems(userId);

    List<Item> itemDescriptions = getItemDescriptions(userCart, params);
    Map<Integer, Integer> itemQuantities = getItemQuantities(userCart);

    Renderer renderer = new CartContentRenderer(params.getLocale(), itemDescriptions, itemQuantities, userInput.getMessageId(), userInput.getCategoryId(), userInput.getItemId());
    renderer.render(response, request.getContextPath(), params, urlResolver);
  }

  @SuppressWarnings("unchecked")
  private static List<Item> getItemDescriptions(Cart userCart, RequestParameters params) throws VkMarketServiceException {
    if (userCart.isEmpty())
      return Collections.emptyList();

    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    return vk.getItemsById(userCart.getItems().stream().map(it->it.getVkItemId()).collect(Collectors.toList()));
  }

  @SuppressWarnings("unchecked")
  private static Map<Integer, Integer> getItemQuantities(Cart userCart) throws VkMarketServiceException {
    if (userCart.isEmpty())
      return Collections.emptyMap();

    return userCart.getItems().stream().collect(Collectors.toMap(CartItem::getVkItemId, CartItem::getQuantity));
  }
}