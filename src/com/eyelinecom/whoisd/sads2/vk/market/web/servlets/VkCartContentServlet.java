package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;
import com.eyelinecom.whoisd.sads2.vk.market.model.item.CartItem;
import com.eyelinecom.whoisd.sads2.vk.market.model.user.User;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.DBService;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.query.UserQuery;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.ItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.CartContentRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInput;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputParser;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
  private DBService db;

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws VkMarketServiceException, IOException {
    String userId = params.getUserId();
    UserInput userInput = UserInputParser.parse(params.getUserInput(), userId);
    Cart userCart = getCartItems(userId);

    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    List<Item> itemDescriptions = vk.getItemsById(userCart.getItems().stream().map(it->it.getVkItemId()).collect(Collectors.toList()));
    Map<Integer, Integer> itemQuantities = userCart.getItems().stream().collect(Collectors.toMap(CartItem::getVkItemId, CartItem::getQuantity));

    Renderer renderer = new CartContentRenderer(params.getLocale(), itemDescriptions, itemQuantities, userInput.getMessageId(), userInput.getCategoryId(), userInput.getItemId());
    renderer.render(response, request.getContextPath(), params, urlResolver);
  }

  private Cart getCartItems(String userId) {
    return db.tx(s -> {
      User user = (User) UserQuery.byUserId(userId, s).uniqueResult();

      if (user == null) {
        Cart cart = new Cart();
        user = new User();
        user.setUserId(userId);
        user.setCart(cart);
        s.save(cart);
        s.save(user);
        cart.setUser(user);
      }

      return user.getCart();
    });
  }
}