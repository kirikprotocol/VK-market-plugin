package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;
import com.eyelinecom.whoisd.sads2.vk.market.services.cart.CartService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram.CartContentRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.UserInput;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputUtils;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    UserInput userInput = UserInputUtils.decodeAndParse(params.getUserInput(), userId);
    Cart userCart = cartService.getCartItems(userId);

    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    List<Item> itemDescriptions = vk.getItemsById(userCart);
    Map<Integer, Integer> itemQuantities = userCart.getItemQuantities();

    Renderer renderer = new CartContentRenderer(params.getLocale(), itemDescriptions, itemQuantities, userInput.getMessageId(),
      userInput.getCategoryId(), userInput.getItemId(), userInput.getFromInlineButton(), userInput.getCartListSection());
    renderer.render(response, request.getContextPath(), params, urlResolver);
  }
}