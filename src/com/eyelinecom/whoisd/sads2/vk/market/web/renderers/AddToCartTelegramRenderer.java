package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.ItemDetailed;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Price;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;

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

  //TODO: clean unused
  private final ResourceBundle bundle;
  private final List<String> extraPhotoUrls;
  private final Integer extraPhotoId;
  private final String name;
  private final Price price;
  private final String description;
  private final String messageId;
  private final int categoryId;
  private final Integer itemId;
  private final Integer quantity;


  public AddToCartTelegramRenderer(Locale locale, ItemDetailed itemDetailed, String messageId, Integer extraPhotoId, int categoryId, Integer itemId, Integer quantity) {
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
    this.quantity = quantity;
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String itemDetailsPage = getItemDetailsPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(itemDetailsPage));
  }

  private String getItemDetailsPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();
    //TODO

    sb.append(pageStart((getEditablePageAttrs(messageId, itemId))));
    sb.append(divStart());
    sb.append("item id: " + itemId);
    sb.append(br());
    sb.append("quantity: " + quantity);
    sb.append(divEnd());
    sb.append(pageEnd());

    return sb.toString();
  }

}
