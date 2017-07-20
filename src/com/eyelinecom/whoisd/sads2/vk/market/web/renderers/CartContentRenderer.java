package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.service.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * author: Artem Voronov
 */
public class CartContentRenderer extends Renderer {
  private final ResourceBundle bundle;

  public CartContentRenderer(Locale locale) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(pageStart());
    sb.append(divStart());
    sb.append("TODO: list of items with prices, total cost");//TODO
    sb.append(br());
    sb.append(divEnd());
    sb.append(buttonsStart());

    sb.append(button("continue_shopping", bundle.getString("continue.shopping"), requestParams.getPluginParams(), ctxPath, "/"));
    sb.append(buttonExit(requestParams.getPluginParams(), ctxPath));

    sb.append(buttonsEnd());
    sb.append(pageEnd());

    sendResponse(response, requestParams, Collections.singletonList(sb.toString()));
  }
}
