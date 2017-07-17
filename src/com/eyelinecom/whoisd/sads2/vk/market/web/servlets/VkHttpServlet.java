package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.service.market.VkMarketServiceException;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.NotSupportedProtocolRenderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: Denis Enenko
 * date: 13.07.17
 */
abstract class VkHttpServlet extends HttpServlet {

  protected final static Logger log = Logger.getLogger("VK_MARKET_PLUGIN");


  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    _handleRequest(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    _handleRequest(req, resp);
  }

  private void _handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    logRequest(request);

    try {
      RequestParameters params = new RequestParameters(request);
      Protocol protocol = Protocol.getValue(params.getProtocol().toUpperCase());

      if(protocol == null) {
        Renderer renderer = new NotSupportedProtocolRenderer(params.getProtocol().toUpperCase(), params.getLocale());
        renderer.render(response, request.getContextPath(), params, null);
        return;
      }

      handleRequest(request, response, protocol, params);
    }
    catch(HttpServletRequestException e) {
      log.error(e.getMessage(), e);
      response.sendError(e.getHttpStatus(), e.getMessage());
    }
    catch(Exception e) {
      log.error(e.getMessage(), e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  protected abstract void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws VkMarketServiceException, IOException;

  protected static void sendRedirect(HttpServletResponse response, String url) throws IOException {
    if(log.isInfoEnabled())
      log.info("Send redirect: " + url);

    response.sendRedirect(url);
  }

  private static void logRequest(HttpServletRequest request) {
    if(log.isInfoEnabled()) {
      String requestUrl = request.getRequestURL().toString();
      String query = request.getQueryString();

      if(query != null && !query.isEmpty())
        requestUrl += "?" + query;

      log.info("Requested URL: " + requestUrl);
    }
  }

}
