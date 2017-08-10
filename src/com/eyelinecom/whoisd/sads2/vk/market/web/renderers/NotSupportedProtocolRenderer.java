package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * author: Denis Enenko
 * date: 13.07.17
 */
public class NotSupportedProtocolRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final String protocol;


  public NotSupportedProtocolRenderer(String protocol, Locale locale) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.protocol = protocol;
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    String text = MessageFormat.format(bundle.getString("protocol.not.supported.message"), protocol, Protocol.getValuesString());

    sb.append(pageStart());
    sb.append(divStart());
    sb.append("<select navigationId=\"submit\" title=\"").append(text).append("\" name=\"answer\">");
    sb.append("<option accesskey=\"1\" value=\"leave\" selected=\"true\">");
    sb.append(bundle.getString("exit.btn"));
    sb.append("</option>");
    sb.append("</select>");
    sb.append(br());
    sb.append(divEnd());
    sb.append("<navigation id=\"submit\">");
    sb.append("<link pageId=\"").append(requestParams.getEventRefererPageUrl().replaceAll("&", "&amp;")).append("\"/>");
    sb.append("</navigation>");
    sb.append(pageEnd());

    sendResponse(response, requestParams, Collections.singletonList(sb.toString()));
  }

}
