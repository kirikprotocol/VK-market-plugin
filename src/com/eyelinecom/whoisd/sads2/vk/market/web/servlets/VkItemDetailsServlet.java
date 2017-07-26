package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.ItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram.ItemDetailsTelegramRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.UserInput;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputUtils;

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
    UserInput value = UserInputUtils.decodeAndParse(params.getUserInput(), params.getUserId());

    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    ItemDetailed itemDetailed = vk.getItemById(value.getItemId());

    Renderer renderer = new ItemDetailsTelegramRenderer(params.getLocale(), itemDetailed, value.getMessageId(), value.getExtraPhotoId(), value.getCategoryId(), value.getItemId());
    renderer.render(response, request.getContextPath(), params, urlResolver);
  }

}
