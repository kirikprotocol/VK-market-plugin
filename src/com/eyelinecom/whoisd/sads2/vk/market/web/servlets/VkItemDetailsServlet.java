package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.ItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.ItemDetailsTelegramRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: Artem Voronov
 */
public class VkItemDetailsServlet extends VkHttpServlet {

  @Inject
  private UrlResolver urlResolver;

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws VkMarketServiceException, IOException {
    UserInput value = parseUserInput(params.getUserInput(), params.getUserId());

    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    ItemDetailed itemDetailed = vk.getItemById(value.itemId);

    Renderer renderer = new ItemDetailsTelegramRenderer(params.getLocale(), itemDetailed, value.messageId, value.extraPhotoId, value.categoryId, value.itemId);
    renderer.render(response, request.getContextPath(), params, urlResolver);
  }

  private static UserInput parseUserInput(String val, String userId) {
    int idx0 = val.indexOf("_");
    if(idx0 == -1) {
      return new UserInput(Integer.parseInt(val), null, userId + "-" + System.currentTimeMillis(), null);
    }
    String categoryId = val.substring(0, idx0);

    int idx1 = val.indexOf("_", idx0 + 1);
    String itemId = val.substring(idx0 + 1, idx1);

    int idx2 = val.indexOf("_", idx1 + 1);

    String messageId;
    if(idx2 == -1) {
      messageId = val.substring(idx1 + 1, val.length());
      return new UserInput(Integer.parseInt(categoryId), Integer.parseInt(itemId), messageId, null);
    }

    messageId = val.substring(idx1 + 1, idx2);
    String extraPhotoId = val.substring(idx2 + 1, val.length());

    return new UserInput(Integer.parseInt(categoryId), Integer.parseInt(itemId), messageId, Integer.parseInt(extraPhotoId));
  }

  private static class UserInput {

    private final int categoryId;
    private final Integer itemId;
    private final String messageId;
    private final Integer extraPhotoId;

    private UserInput(int categoryId, Integer itemId, String messageId, Integer extraPhotoId) {
      this.categoryId = categoryId;
      this.itemId = itemId;
      this.messageId = messageId;
      this.extraPhotoId = extraPhotoId;
    }

  }
}
