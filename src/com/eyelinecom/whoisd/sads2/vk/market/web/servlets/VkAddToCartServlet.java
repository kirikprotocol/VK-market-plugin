package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;
import com.eyelinecom.whoisd.sads2.vk.market.model.item.CartItem;
import com.eyelinecom.whoisd.sads2.vk.market.model.user.User;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.DBService;
import com.eyelinecom.whoisd.sads2.vk.market.services.db.query.UserQuery;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.ItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.AddToCartTelegramRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInput;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputParser;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: Artem Voronov
 */
public class VkAddToCartServlet extends VkHttpServlet {

  @Inject
  private UrlResolver urlResolver;

  @Inject
  private DBService db;

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws VkMarketServiceException, IOException {
    String userId = params.getUserId();
    UserInput value = UserInputParser.parse(params.getUserInput(), userId);

    addToCart(userId, value.getItemId(), value.getQuantity());

    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    ItemDetailed itemDetailed = vk.getItemById(value.getItemId());

    Renderer renderer = new AddToCartTelegramRenderer(params.getLocale(), itemDetailed, value.getMessageId(),
      value.getExtraPhotoId(), value.getCategoryId(), value.getItemId(), value.getQuantity());
    renderer.render(response, request.getContextPath(), params, urlResolver);
  }

  private void addToCart(String userId, Integer itemId, Integer quantity) {
    db.vtx(s -> {
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

      CartItem item = new CartItem();
      item.setVkItemId(itemId);
      item.setQuantity(quantity);
      item.setCart(user.getCart());
      s.save(item);
      user.addToCart(item);
    });
  }
}
