package com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
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
public class ChooseItemInCartForDeletingTelegramRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final List<Item> itemDescriptions;
  private final String messageId;
  private final Integer itemId;
  private final Integer categoryId;

  public ChooseItemInCartForDeletingTelegramRenderer(Locale locale, List<Item> itemDescriptions, String messageId, Integer categoryId, Integer itemId) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.itemDescriptions = itemDescriptions;
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

    sb.append(pageStart(getEditablePageAttrs(messageId, true)));
    sb.append(divStart());
    sb.append(bundle.getString("choose.item"));
    sb.append(divEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    for (Item item : itemDescriptions) {
      sb.append(button(UserInputJsonBuilder.json(item.getCategory().getId(), item.getId(), messageId), item.getName(), requestParams.getPluginParams(), ctxPath, "/delete-from-cart", urlResolver));
    }
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, true), bundle.getString("back.to.cart"), requestParams.getPluginParams(), ctxPath, "/cart", urlResolver));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    return sb.toString();
  }

}
