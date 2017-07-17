package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.service.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.service.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.service.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.service.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.CategoryItemsTelegramRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * author: Denis Enenko
 * date: 14.07.17
 */
public class VkCategoryItemsServlet extends VkHttpServlet {

  @Inject
  private UrlResolver urlResolver;


  @Override
  protected void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws VkMarketServiceException, IOException {
    UserInput value = parseUserInput(params.getUserInput(), params.getUserId());

    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    List<Item> categoryItems = vk.getItemsByCategory(value.categoryId);

    Renderer renderer = getCategoryItemsRenderer(protocol, value.messageId, categoryItems, value.itemId, params.getLocale());
    renderer.render(response, request.getContextPath(), params, urlResolver);
  }

  private static Renderer getCategoryItemsRenderer(Protocol protocol, String messageId, List<Item> categoryItems, Integer itemId, Locale locale) {
    switch(protocol) {
      case TELEGRAM:
        return new CategoryItemsTelegramRenderer(categoryItems, messageId, itemId, locale);
      default:
        throw new IllegalArgumentException("Unsupported protocol: " + protocol.name());
    }
  }

  private static UserInput parseUserInput(String val, String userId) {
    int idx0 = val.indexOf("_");
    if(idx0 == -1) {
      return new UserInput(Integer.parseInt(val), null, userId + "-" + System.currentTimeMillis());
    }
    String categoryId = val.substring(0, idx0);

    int idx1 = val.indexOf("_", idx0 + 1);
    String itemId = val.substring(idx0 + 1, idx1);

    String messageId = val.substring(idx1 + 1, val.length());

    return new UserInput(Integer.parseInt(categoryId), Integer.parseInt(itemId), messageId);
  }

  private static class UserInput {

    private final int categoryId;
    private final Integer itemId;
    private final String messageId;

    private UserInput(int categoryId, Integer itemId, String messageId) {
      this.categoryId = categoryId;
      this.itemId = itemId;
      this.messageId = messageId;
    }

  }

}
