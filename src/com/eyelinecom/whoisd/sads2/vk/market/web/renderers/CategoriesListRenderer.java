package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.Category;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * author: Denis Enenko
 * date: 13.07.17
 */
public class CategoriesListRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final Set<Category> categories;


  public CategoriesListRenderer(Set<Category> categories, Locale locale) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.categories = categories;
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(pageStart());
    sb.append(divStart());
    sb.append(bundle.getString("text"));
    sb.append(br());
    sb.append(divEnd());
    sb.append(buttonsStart());

    for(Category cat : categories) {
      sb.append(button(cat.getId() + "", cat.getName(), requestParams.getPluginParams(), ctxPath, "/category"));
    }
    sb.append(buttonExit(requestParams.getPluginParams(), ctxPath));

    sb.append(buttonsEnd());
    sb.append(pageEnd());

    sendResponse(response, requestParams, Collections.singletonList(sb.toString()));
  }

}
