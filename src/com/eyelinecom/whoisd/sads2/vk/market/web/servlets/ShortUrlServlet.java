package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.service.shorturl.UrlResolver;
import org.apache.log4j.Logger;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: Denis Enenko
 * date: 17.07.17
 */
public class ShortUrlServlet extends HttpServlet {

  protected final static Logger log = Logger.getLogger("VK_MARKET_PLUGIN");

  @Inject
  private UrlResolver urlResolver;


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    handleRequest(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    handleRequest(req, resp);
  }

  private void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    logRequest(request);

    try {
      String shortUrl = request.getPathInfo();
      String url = urlResolver.getUrl(shortUrl);

      if(url == null) {
        log.error("URL not found for short URL: " + shortUrl);
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "URL not found for short URL: " + shortUrl);
        return;
      }

      sendRedirect(response, url);
    }
    catch(Exception e) {
      log.error(e.getMessage(), e);
      response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

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
