package com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.ItemDetailed;
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
public class ItemDetailsTelegramRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final List<String> extraPhotoUrls;
  private final Integer extraPhotoId;
  private final String name;
  private final Price price;
  private final String description;
  private final Navigation navigation;
  private final String messageId;
  private final Integer categoryId;
  private final Integer itemId;


  public ItemDetailsTelegramRenderer(Locale locale, ItemDetailed itemDetailed, String messageId, Integer extraPhotoId, Integer categoryId, Integer itemId) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.extraPhotoId = extraPhotoId;
    this.messageId = messageId;
    this.categoryId = categoryId;
    this.itemId = itemId;
    this.extraPhotoUrls = itemDetailed.getExtraPhotoUrls();
    this.description = itemDetailed.getDescription();
    this.name = itemDetailed.getName();
    this.price = itemDetailed.getPrice();
    this.navigation = new Navigation();
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String itemDetailsPage = getItemDetailsPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(itemDetailsPage));
  }

  private String getItemDetailsPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(pageStart((getEditablePageAttrs(messageId, itemId != null))));
    sb.append(divStart());
    if (!extraPhotoUrls.isEmpty()) {
      sb.append(extraPhotoUrls.get(navigation.currExtraPhotoId));
      sb.append(br());
    }
    sb.append(bStart()).append(name).append(bEnd());
    sb.append(br());
    sb.append(description);
    sb.append(br());
    sb.append(price.getText());
    sb.append(br());
    sb.append(divEnd());
    if (!extraPhotoUrls.isEmpty()) {
      sb.append(buttonsStart(getInlineButtonsAttrs()));
      sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, navigation.prevExtraPhotoId), "&lt;", requestParams.getPluginParams(), ctxPath, "/item-details", urlResolver));
      sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId), bundle.getString("back.to.category.items"), requestParams.getPluginParams(), ctxPath, "/category", urlResolver));
      sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, navigation.currExtraPhotoId), bundle.getString("add.to.cart.btn"), requestParams.getPluginParams(), ctxPath, "/ask-quantity", urlResolver));
      sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, navigation.nextExtraPhotoId), "&gt;", requestParams.getPluginParams(), ctxPath, "/item-details", urlResolver));
      sb.append(buttonsEnd());
    }
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button("", bundle.getString("change.category"), requestParams.getPluginParams(), ctxPath, "/", urlResolver));
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId), bundle.getString("open.cart"), requestParams.getPluginParams(), ctxPath, "/cart", urlResolver));
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(buttonExit(requestParams.getPluginParams(), ctxPath, urlResolver));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    return sb.toString();
  }

  //TODO: duplication
  private class Navigation {

    private final int prevExtraPhotoId;
    private final int currExtraPhotoId;
    private final int nextExtraPhotoId;

    private Navigation() {
      currExtraPhotoId = findCurrentExtraPhotoId();
      prevExtraPhotoId = currExtraPhotoId == 0 ? extraPhotoUrls.size() - 1 : currExtraPhotoId - 1;
      nextExtraPhotoId = currExtraPhotoId == extraPhotoUrls.size() - 1 ? 0 : currExtraPhotoId + 1;
    }

    private int findCurrentExtraPhotoId() {
      if(extraPhotoId == null)
        return 0;

      return extraPhotoId;
    }

  }
}
