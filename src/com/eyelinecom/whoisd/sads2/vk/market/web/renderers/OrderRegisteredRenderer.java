package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * author: Artem Voronov
 */
public class OrderRegisteredRenderer extends Renderer {

  private final ResourceBundle bundle;

  public OrderRegisteredRenderer(Locale locale) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String askPhoneNumber = getAskPhoneNumberPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(askPhoneNumber));
  }

  private String getAskPhoneNumberPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(pageStart());
    sb.append(divStart());
    sb.append(bundle.getString("order.has.been.registered"));
    sb.append(divEnd());
    sb.append(pageEnd());

    return sb.toString();
  }
}
