package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.services.model.OrderDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.OrderItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * author: Artem Voronov
 */
public class OrderRegisteredRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final OrderDetailed orderDetailed;

  public OrderRegisteredRenderer(Locale locale, OrderDetailed orderDetailed) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.orderDetailed = orderDetailed;
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
    sb.append(String.format(bundle.getString("order.is.registered"), orderDetailed.getId()));
    sb.append(br());
    sb.append(br());
    sb.append(bundle.getString("order.list")).append(":");
    sb.append(br());
    List<OrderItemDetailed> items = orderDetailed.getItems();
    int count = 0;
    for (OrderItemDetailed it : items) {//TODO: если список будет большим, использовать пуши или вообще сделать отправку по email
      sb.append(++count).append(". ").append(it.getName()).append(" x ").append(it.getQuantity()).append(". ")
        .append(bundle.getString("price.per.unit")).append(" - ").append(it.getPrice());
      sb.append(br());
    }
    sb.append(br());
    sb.append(String.format(bundle.getString("total.cost"), orderDetailed.getTotalCost()));
    sb.append(br());
    sb.append(br());
    sb.append(bundle.getString("wait.for.contact"));
    sb.append(br());
    sb.append(String.format(bundle.getString("merchant.email"), orderDetailed.getMerchantEmail()));
    sb.append(divEnd());
    sb.append(buttonsStart());
    sb.append(button("", bundle.getString("continue.shopping"), requestParams.getPluginParams(), ctxPath, "/", urlResolver));
    sb.append(buttonExit(requestParams.getPluginParams(), ctxPath));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    return sb.toString();
  }
}
