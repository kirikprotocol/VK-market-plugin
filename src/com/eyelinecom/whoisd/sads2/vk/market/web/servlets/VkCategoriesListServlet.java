package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketService;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.CategoriesListRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: Denis Enenko
 * date: 27.06.17
 */
public class VkCategoriesListServlet extends VkHttpServlet {

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws VkMarketServiceException, IOException {
    VkMarketService vk = new VkMarketService(params.getVkUserId(), params.getVkAccessToken());
    Renderer renderer = new CategoriesListRenderer(vk.getCategories(), params.getLocale());
    renderer.render(response, request.getContextPath(), params, null);
  }

}
