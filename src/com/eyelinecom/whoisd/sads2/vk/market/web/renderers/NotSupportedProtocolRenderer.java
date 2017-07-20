package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.service.shorturl.UrlResolver;
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

    sb.append(pageStart());
    sb.append(divStart());
    sb.append(MessageFormat.format(bundle.getString("protocol.not.supported.message"), protocol, Protocol.getValuesString()));
    sb.append(br());
    sb.append(divEnd());
    sb.append(buttonsStart());
    sb.append(buttonExit(requestParams.getPluginParams(), ctxPath));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    sendResponse(response, requestParams, Collections.singletonList(sb.toString()));
  }

}
