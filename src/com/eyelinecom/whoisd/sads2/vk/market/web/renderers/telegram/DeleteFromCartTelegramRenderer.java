package com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.ItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.UserInput;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * author: Artem Voronov
 */
public class DeleteFromCartTelegramRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final String name;
  private final String messageId;
  private final Integer categoryId;
  private final Integer itemId;
  private final boolean isCartEmpty;

  public DeleteFromCartTelegramRenderer(Locale locale, ItemDetailed itemDetailed, String messageId, Integer categoryId, Integer itemId, boolean isCartEmpty) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.messageId = messageId;
    this.categoryId = categoryId;
    this.itemId = itemId;
    this.name = itemDetailed.getName();
    this.isCartEmpty = isCartEmpty;
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String itemDeletedPage = getItemDeletedPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(itemDeletedPage));
  }

  private String getItemDeletedPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    String deleteMoreBtnVal = UserInputUtils.toJsonAndEncode(new UserInput.Builder().category(categoryId).item(itemId).message(messageId).build());
    String openCartBtnVal = UserInputUtils.toJsonAndEncode(new UserInput.Builder().category(categoryId).message(messageId).inline(true).build());

    sb.append(pageStart((getEditablePageAttrs(messageId, itemId != null))));
    sb.append(divStart());
    sb.append(bStart()).append(String.format(bundle.getString("item.deleted"), name)).append(bEnd());
    sb.append(divEnd());
    if (!isCartEmpty) {
      sb.append(buttonsStart(getInlineButtonsAttrs()));
      sb.append(button(deleteMoreBtnVal, bundle.getString("delete.more"), requestParams.getPluginParams(), ctxPath, "/choose-item-in-cart", urlResolver));
      sb.append(buttonsEnd());
    }
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button("", bundle.getString("continue.shopping"), requestParams.getPluginParams(), ctxPath, "/", urlResolver));
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(openCartBtnVal, bundle.getString("open.cart"), requestParams.getPluginParams(), ctxPath, "/cart", urlResolver));
    sb.append(buttonsEnd());
    if (!isCartEmpty) {
      sb.append(buttonsStart(getInlineButtonsAttrs()));
      sb.append(button(UserInputUtils.empty(), bundle.getString("proceed.to.checkout"), requestParams.getPluginParams(), ctxPath, "/ask-phone", urlResolver));
      sb.append(buttonsEnd());
    }
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(buttonExit(requestParams.getPluginParams(), ctxPath, urlResolver));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    return sb.toString();
  }
}
