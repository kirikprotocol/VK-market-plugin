package com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * author: Artem Voronov
 */
public class ChooseItemInCartTelegramRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final List<Item> itemDescriptions;
  private final Map<Integer, Integer> itemQuantities;
  private final String messageId;
  private final Integer itemId;
  private final int categoryId;

  public ChooseItemInCartTelegramRenderer(Locale locale, List<Item> itemDescriptions, Map<Integer, Integer> itemQuantities, String messageId, int categoryId, Integer itemId) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.itemDescriptions = itemDescriptions;
    this.itemQuantities = itemQuantities;
    this.messageId = messageId;
    this.categoryId = categoryId;
    this.itemId = itemId;
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String chooseItemPage = getChooseItemPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(chooseItemPage));
  }

  private String getChooseItemPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(pageStart(getEditablePageAttrs(messageId, itemId)));
    sb.append(divStart());
    sb.append(bundle.getString("choose.item"));
    sb.append(divEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    for (Item item : itemDescriptions) {
      sb.append(button(item.getCategory().getId() + "_" + item.getId() + "_" + messageId, item.getName(), requestParams.getPluginParams(), ctxPath, "/delete-from-cart", urlResolver));
    }
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(categoryId + "_" + itemId + "_" + messageId, bundle.getString("back.to.cart"), requestParams.getPluginParams(), ctxPath, "/cart", urlResolver));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    return sb.toString();
  }

}
