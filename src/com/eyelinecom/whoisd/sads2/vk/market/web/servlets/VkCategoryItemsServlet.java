package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram.CategoryItemsTelegramRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.UserInput;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputUtils;

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
    UserInput value = UserInputUtils.decodeAndParse(params.getUserInput(), params.getUserId());

    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    List<Item> categoryItems = vk.getItemsByCategory(value.getCategoryId());

    Renderer renderer = getCategoryItemsRenderer(protocol, value.getMessageId(), categoryItems, value.getItemId(), params.getLocale());
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

}
