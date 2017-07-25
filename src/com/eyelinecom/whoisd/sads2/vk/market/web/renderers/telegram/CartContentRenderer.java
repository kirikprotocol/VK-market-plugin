package com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Price;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputJsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * author: Artem Voronov
 */
public class CartContentRenderer extends Renderer {
  private final ResourceBundle bundle;
  private final List<Item> itemDescriptions;
  private final Map<Integer, Integer> itemQuantities;
  private final Integer totalCost;
  private final String messageId;
  private final Integer categoryId;
  private final Integer itemId;
  private final Boolean fromInlineButton;

  public CartContentRenderer(Locale locale, List<Item> itemDescriptions, Map<Integer, Integer> itemQuantities, String messageId, Integer categoryId, Integer itemId, Boolean fromInlineButton) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.messageId = messageId;
    this.categoryId = categoryId;
    this.itemId = itemId;
    this.itemDescriptions = itemDescriptions;
    this.itemQuantities = itemQuantities;
    this.totalCost = itemDescriptions.stream().map(it -> Integer.parseInt(it.getPrice().getPrice()) * itemQuantities.get(it.getId())).mapToInt(Integer::intValue).sum();
    this.fromInlineButton = fromInlineButton;
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String cartContentPage = getCartContentPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(cartContentPage));
  }

  private String getCartContentPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(pageStart(getEditablePageAttrs(messageId, itemId != null || fromInlineButton)));
    sb.append(divStart());

    if (itemDescriptions.isEmpty()) {
      sb.append(bStart()).append(bundle.getString("cart.is.empty")).append(bEnd());
      sb.append(br());
    } else {
      int count = 0;
      for (Item item : itemDescriptions) {
        Price price = item.getPrice();
        sb.append(++count).append(". ").append(item.getName()).append(" x ").append(itemQuantities.get(item.getId())).append(", ")
          .append(convert(Integer.parseInt(price.getPrice()))).append(" ").append("RUB").append(" - ").append(bundle.getString("per.unit"));
        sb.append(br());
      }

      //TODO: currency в текущей версии API VK всегда равен RUB
      //TODO: нужно рассчитывать для общего случая
      sb.append(br());
      sb.append(bStart()).append(bundle.getString("total.cost")).append(": ").append(convert(totalCost)).append(" ").append("RUB").append(bEnd());

    }
    sb.append(divEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    if (categoryId == null && itemId == null) {
      sb.append(button(UserInputJsonBuilder.json(), bundle.getString("back"), requestParams.getPluginParams(), ctxPath, "/", urlResolver));
    } else {
      sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId), bundle.getString("back"), requestParams.getPluginParams(), ctxPath, "/category", urlResolver));
    }
    sb.append(buttonsEnd());
    if (!itemDescriptions.isEmpty()) {
      sb.append(buttonsStart(getInlineButtonsAttrs()));
      sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId), bundle.getString("delete.from.cart"), requestParams.getPluginParams(), ctxPath, "/choose-item-in-cart", urlResolver));
      sb.append(buttonsEnd());
      sb.append(buttonsStart(getInlineButtonsAttrs()));
      sb.append(button("", bundle.getString("proceed.to.checkout"), requestParams.getPluginParams(), ctxPath, "/order", urlResolver));//TODO
      sb.append(buttonsEnd());
    }
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(buttonExit(requestParams.getPluginParams(), ctxPath, urlResolver));
    sb.append(buttonsEnd());
    sb.append(pageEnd());
    return sb.toString();
  }

  private static String convert(Integer totalCost) {
    if(totalCost == null)
      throw new IllegalArgumentException("null total cost");

    int i = totalCost / 100;
    int f = totalCost % 100;
    return "" + i + "." + (f < 10 ? "0" + f : f);
  }
}