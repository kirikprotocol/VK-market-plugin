package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.services.model.OrderDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.OrderItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * author: Artem Voronov
 */
public class OrderRegisteredRenderer extends Renderer {

  private static final int PARTITION_SIZE = 3;

  private final ResourceBundle bundle;
  private final OrderDetailed orderDetailed;

  public OrderRegisteredRenderer(Locale locale, OrderDetailed orderDetailed) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.orderDetailed = orderDetailed;
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    sendResponse(response, requestParams, initFeedBackPages(ctxPath, requestParams, urlResolver));
  }

  private List<String> initFeedBackPages(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    List<OrderItemDetailed> items = orderDetailed.getItems();
    int itemsCount = items.size();
    if (itemsCount <= PARTITION_SIZE)
      return Arrays.asList(getOrderRegisteredPage(ctxPath, requestParams, urlResolver));

    List<String> result = new LinkedList<>();
    result.add(getBasicInfoPage());

    for (int i = 0, partitionCounter = 0; i < itemsCount; i += PARTITION_SIZE, partitionCounter++) {
      int tillIndex = Math.min(i + PARTITION_SIZE, itemsCount);
      List<OrderItemDetailed> part = items.subList(i, tillIndex);
      result.add(getOrderListPartitionPage(part, partitionCounter, tillIndex == itemsCount, ctxPath, requestParams, urlResolver));
    }

    return result;
  }

  private String getBasicInfoPage() throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(pageStart());
    sb.append(divStart());
    sb.append(String.format(bundle.getString("order.is.registered"), orderDetailed.getId()));
    sb.append(br());
    sb.append(br());
    sb.append(String.format(bundle.getString("total.cost"), orderDetailed.getTotalCost()));
    sb.append(br());
    sb.append(br());
    sb.append(bundle.getString("wait.for.contact"));
    sb.append(br());
    sb.append(String.format(bundle.getString("merchant.email"), orderDetailed.getMerchantEmail()));
    sb.append(divEnd());
    sb.append(pageEnd());
    return sb.toString();
  }

  private String getOrderListPartitionPage(List<OrderItemDetailed> partition, int partitionCounter, boolean isFinal, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();
    sb.append(pageStart());
    sb.append(divStart());
    orderList(partition, partitionCounter * PARTITION_SIZE, sb);
    sb.append(divEnd());
    if (isFinal)
      exitButtons(sb, ctxPath, requestParams, urlResolver);
    sb.append(pageEnd());
    return sb.toString();
  }

  private void exitButtons(StringBuilder sb, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    sb.append(buttonsStart());
    sb.append(button("", bundle.getString("continue.shopping"), requestParams.getPluginParams(), ctxPath, "/", urlResolver));
    sb.append("<link pageId=\"" + requestParams.getExitUrl().replaceAll("&", "&amp;") + "\">" + bundle.getString("exit.btn") + "</link>");
    sb.append(buttonsEnd());
  }

  private void orderList(List<OrderItemDetailed> items, int startCounterWith, StringBuilder sb) throws IOException {
    if (startCounterWith == 0) {
      sb.append(bundle.getString("order.list")).append(":");
      sb.append(br());
    }
    int count = startCounterWith;
    for (OrderItemDetailed it : items) {
      sb.append(++count).append(". ").append(it.getName()).append(" x ").append(it.getQuantity()).append(". ")
        .append(bundle.getString("price.per.unit")).append(" - ").append(it.getPrice());
      sb.append(br());
    }
  }

  private String getOrderRegisteredPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(pageStart());
    sb.append(divStart());
    sb.append(String.format(bundle.getString("order.is.registered"), orderDetailed.getId()));
    sb.append(br());
    sb.append(br());
    sb.append(String.format(bundle.getString("total.cost"), orderDetailed.getTotalCost()));
    sb.append(br());
    sb.append(br());
    sb.append(bundle.getString("wait.for.contact"));
    sb.append(br());
    sb.append(String.format(bundle.getString("merchant.email"), orderDetailed.getMerchantEmail()));
    sb.append(br());
    sb.append(br());
    orderList(orderDetailed.getItems(), 0, sb);
    sb.append(divEnd());
    exitButtons(sb, ctxPath, requestParams, urlResolver);
    sb.append(pageEnd());

    return sb.toString();
  }
}
