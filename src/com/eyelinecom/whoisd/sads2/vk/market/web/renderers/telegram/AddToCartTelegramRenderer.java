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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * author: Artem Voronov
 */
public class AddToCartTelegramRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final List<String> extraPhotoUrls;
  private final Integer extraPhotoId;
  private final String name;
  private final String messageId;
  private final Integer categoryId;
  private final Integer itemId;
  private final Integer quantity;


  public AddToCartTelegramRenderer(Locale locale, ItemDetailed itemDetailed, String messageId, Integer extraPhotoId, Integer categoryId, Integer itemId, Integer quantity) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.extraPhotoId = extraPhotoId;
    this.messageId = messageId;
    this.categoryId = categoryId;
    this.itemId = itemId;
    this.extraPhotoUrls = itemDetailed.getExtraPhotoUrls();
    this.name = itemDetailed.getName();
    this.quantity = quantity;
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String itemAddedPage = getItemAddedPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(itemAddedPage));
  }

  private String getItemAddedPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    String addMoreBtnVal = UserInputUtils.toJsonAndEncode(new UserInput.Builder().category(categoryId).item(itemId).message(messageId).photo(extraPhotoId).build());
    String backToCategoryBtnVal = UserInputUtils.toJsonAndEncode(new UserInput.Builder().category(categoryId).item(itemId).message(messageId).build());
    String openCartBtnVal = UserInputUtils.toJsonAndEncode(new UserInput.Builder().category(categoryId).item(itemId).message(messageId).build());
    String empty = UserInputUtils.empty();

    sb.append(pageStart((getEditablePageAttrs(messageId, itemId != null))));
    sb.append(divStart());
    if (!extraPhotoUrls.isEmpty()) {
      sb.append(extraPhotoUrls.get(extraPhotoId == null ? 0 : extraPhotoId));
      sb.append(br());
    }
    sb.append(bStart()).append(String.format(bundle.getString("added.quantity"), name, quantity)).append(bEnd());
    sb.append(divEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(addMoreBtnVal, bundle.getString("add.more"), requestParams.getPluginParams(), ctxPath, "/ask-quantity", urlResolver));
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(backToCategoryBtnVal, bundle.getString("back.to.current.category"), requestParams.getPluginParams(), ctxPath, "/category", urlResolver));
    sb.append(button(empty, bundle.getString("another.category"), requestParams.getPluginParams(), ctxPath, "/", urlResolver));
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(openCartBtnVal, bundle.getString("open.cart"), requestParams.getPluginParams(), ctxPath, "/cart", urlResolver));
    sb.append(button(empty, bundle.getString("proceed.to.checkout"), requestParams.getPluginParams(), ctxPath, "/ask-phone", urlResolver));
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(buttonExit(requestParams.getPluginParams(), ctxPath, urlResolver));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    return sb.toString();
  }

}
