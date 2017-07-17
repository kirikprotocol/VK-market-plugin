package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.service.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.*;

/**
 * author: Denis Enenko
 * date: 13.07.17
 */
public abstract class Renderer {

  private final static Logger log = Logger.getLogger("VK_MARKET_PLUGIN");
  private final ResourceBundle bundle;


  Renderer(Locale locale) {
    this.bundle = ResourceBundle.getBundle(Renderer.class.getName(), locale);
  }

  public abstract void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException;

  protected static String pageStart() {
    return pageStart(null);
  }

  protected static String pageStart(Map<String, String> attributes) {
    StringBuilder sb = new StringBuilder();

    sb.append("<page version=\"2.0\"");

    String attributesVal = getAttributesValue(attributes);
    if(attributesVal != null) {
      sb.append(" attributes=\"").append(attributesVal).append("\"");
    }

    sb.append(">");

    return sb.toString();
  }

  protected static String pageEnd() {
    return "</page>";
  }

  protected static String divStart() {
    return "<div>";
  }

  protected static String divEnd() {
    return "</div>";
  }

  protected static String br() {
    return "<br/>";
  }

  protected static String bStart() {
    return "<b>";
  }

  protected static String bEnd() {
    return "</b>";
  }

  protected static String buttonsStart() {
    return buttonsStart(null);
  }

  protected static String buttonsStart(Map<String, String> attributes) {
    StringBuilder sb = new StringBuilder();

    sb.append("<navigation");

    String attributesVal = getAttributesValue(attributes);
    if(attributesVal != null) {
      sb.append(" attributes=\"").append(attributesVal).append("\"");
    }

    sb.append(">");

    return sb.toString();
  }

  protected static String buttonsEnd() {
    return "</navigation>";
  }

  protected static String button(String value, String label, Map<String, String> params, String ctxPath, String urlPath) throws IOException {
    return button(value, label, params, ctxPath, urlPath, null);
  }

  protected static String button(String value, String label, Map<String, String> params, String ctxPath, String urlPath, UrlResolver urlResolver) throws IOException {
    Map<String, String> _params = new HashMap<>(params);
    _params.put("user_input", value);

    String url;

    if(urlResolver != null) {
      url = formatPageUrl(_params, ctxPath + urlPath, false);
      url = ctxPath + "/su" + urlResolver.getShortUrl(url);
    }
    else {
      url = formatPageUrl(_params, ctxPath + urlPath, true);
    }

    return "<link pageId=\"" + url + "\">"
        + label
        + "</link>";
  }

  protected String buttonExit(Map<String, String> params, String ctxPath) throws IOException {
    return "<link pageId=\"" + formatPageUrl(params, ctxPath + "/exit", true) + "\">"
        + bundle.getString("exit.btn")
        + "</link>";
  }

  private static String getAttributesValue(Map<String, String> attrs) {
    if(attrs == null || attrs.isEmpty())
      return null;

    StringBuilder attrsVal = new StringBuilder();

    for(Map.Entry<String, String> e : attrs.entrySet()) {
      if(attrsVal.length() > 0) attrsVal.append(";");
      attrsVal.append(e.getKey()).append(":").append(e.getValue());
    }

    return attrsVal.toString();
  }

  private static String formatPageUrl(Map<String, String> params, String path, boolean encode) throws IOException {
    if(params == null || params.isEmpty())
      return path;

    StringBuilder query = new StringBuilder();

    for(Map.Entry<String, String> e : params.entrySet()) {
      if(query.length() > 0) {
        query.append(encode ? "&amp;" : "&");
      }
      query.append(encode ? URLEncoder.encode(e.getKey(), "UTF-8") : e.getKey());
      query.append("=");
      query.append(encode ? URLEncoder.encode(e.getValue(), "UTF-8") : e.getValue());
    }

    return path.contains("?") ? path + "&amp;" + query.toString() : path + "?" + query.toString();
  }

  protected static void sendResponse(HttpServletResponse response, List<String> xmlPages) throws IOException {
    String xmlPage = xmlPages.get(0);

    if(log.isInfoEnabled())
      log.info("Send response: " + xmlPage);

    response.setContentType("text/xml; charset=utf-8");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpServletResponse.SC_OK);

    PrintWriter out = response.getWriter();
    out.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    out.write(xmlPage);
    out.close();

    if(xmlPages.size() > 1) {
      for(int i = 1; i < xmlPages.size(); i++) {
        sendPush(xmlPages.get(i));
      }
    }
  }

  private static void sendPush(String xmlPage) {
    if(log.isInfoEnabled())
      log.info("Send push: " + xmlPage);

    //todo Implementing PUSH: https://confluence.eyeline.mobi/pages/viewpage.action?pageId=18776267
  }

}
