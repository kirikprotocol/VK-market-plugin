package com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.ItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Price;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputJsonBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * author: Artem Voronov
 */
public class AskQuantityTelegramRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final List<String> extraPhotoUrls;
  private final Integer extraPhotoId;
  private final String name;
  private final Price price;
  private final String description;
  private final String messageId;
  private final Integer categoryId;
  private final Integer itemId;


  public AskQuantityTelegramRenderer(Locale locale, ItemDetailed itemDetailed, String messageId, Integer extraPhotoId, Integer categoryId, Integer itemId) {
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
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String askQuantityPage = getAskQuantityPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(askQuantityPage));
  }

  private String getAskQuantityPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(pageStart((getEditablePageAttrs(messageId, itemId != null))));
    sb.append(divStart());
    if (!extraPhotoUrls.isEmpty()) {
      sb.append(extraPhotoUrls.get(extraPhotoId == null ? 0 : extraPhotoId));
    }
    sb.append(br());
    sb.append(bStart()).append(name).append(bEnd());
    sb.append(br());
    if (extraPhotoId != null) {
      sb.append(description);
      sb.append(br());
    }
    sb.append(price.getText());
    sb.append(br());
    sb.append(divEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, extraPhotoId, 1), "1", requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, extraPhotoId, 2), "2", requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, extraPhotoId, 3), "3", requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, extraPhotoId, 4), "4", requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, extraPhotoId, 5), "5", requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, extraPhotoId, 10), "10", requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, extraPhotoId, 25), "25", requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, extraPhotoId, 50), "50", requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId, extraPhotoId, 100), "100", requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(buttonsEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(UserInputJsonBuilder.json(categoryId, itemId, messageId), bundle.getString("back.to.category.items"), requestParams.getPluginParams(), ctxPath, "/category", urlResolver));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    return sb.toString();
  }

}
