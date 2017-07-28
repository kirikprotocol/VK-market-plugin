package com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.market.Price;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.UserInput;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.NavigationSections;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.PriceUtils;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * author: Artem Voronov
 */
public class CartContentRenderer extends Renderer {

  private static final int PARTITION_SIZE = 10;

  private final ResourceBundle bundle;
  private final List<Item> itemDescriptions;
  private final Map<Integer, Integer> itemQuantities;
  private final Integer totalCost;
  private final String messageId;
  private final Integer categoryId;
  private final Integer itemId;
  private final Boolean fromInlineButton;
  private final NavigationSections navigation;

  public CartContentRenderer(Locale locale, List<Item> itemDescriptions, Map<Integer, Integer> itemQuantities, String messageId,
                             Integer categoryId, Integer itemId, Boolean fromInlineButton, Integer cartListSection) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.messageId = messageId;
    this.categoryId = categoryId;
    this.itemId = itemId;
    this.itemDescriptions = itemDescriptions;
    this.itemQuantities = itemQuantities;
    this.totalCost = PriceUtils.getTotalCost(itemDescriptions, itemQuantities);
    this.fromInlineButton = fromInlineButton;
    this.navigation = new NavigationSections(cartListSection, itemDescriptions.size(), PARTITION_SIZE);
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String cartContentPage = getCartContentPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(cartContentPage));
  }

  private String getCartContentPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    String commonBtnVal = UserInputUtils.toJsonAndEncode(new UserInput.Builder().category(categoryId).item(itemId).message(messageId).build());

    sb.append(pageStart(getEditablePageAttrs(messageId, itemId != null || fromInlineButton)));
    sb.append(divStart());

    if (itemDescriptions.isEmpty()) {
      sb.append(bStart()).append(bundle.getString("cart.is.empty")).append(bEnd());
      sb.append(br());
      sb.append(divEnd());
    } else {
      if (itemDescriptions.size() <= PARTITION_SIZE) {
        renderAll(sb);
      } else {
        renderSection(sb, ctxPath, requestParams, urlResolver);
      }
    }
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    if (categoryId == null && itemId == null) {
      sb.append(button(UserInputUtils.empty(), bundle.getString("back"), requestParams.getPluginParams(), ctxPath, "/", urlResolver));
    } else {
      sb.append(button(commonBtnVal, bundle.getString("back"), requestParams.getPluginParams(), ctxPath, "/category", urlResolver));
    }
    sb.append(buttonsEnd());
    if (!itemDescriptions.isEmpty()) {
      sb.append(buttonsStart(getInlineButtonsAttrs()));
      sb.append(button(commonBtnVal, bundle.getString("delete.from.cart"), requestParams.getPluginParams(), ctxPath, "/choose-item-in-cart", urlResolver));
      sb.append(buttonsEnd());
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

  private static String createSectionBtnVal(UserInput input, Integer cartSection) throws IOException {
    input.setCartListSection(cartSection);
    return UserInputUtils.toJsonAndEncode(input);
  }

  private void totalCost(StringBuilder sb) {
    //TODO: currency в текущей версии API VK всегда равен RUB
    //TODO: нужно рассчитывать для общего случая
    sb.append(br());
    sb.append(bStart()).append(bundle.getString("total.cost")).append(": ").append(PriceUtils.convert(totalCost)).append(" ").append("RUB").append(bEnd());

  }

  private void renderAll(StringBuilder sb) {
    int count = 0;
    for (Item item : itemDescriptions) {
      Price price = item.getPrice();
      sb.append(++count).append(". ").append(item.getName()).append(" x ").append(itemQuantities.get(item.getId())).append(", ")
        .append(PriceUtils.convert(Integer.parseInt(price.getPrice()))).append(" ").append("RUB").append(" - ").append(bundle.getString("per.unit"));
      sb.append(br());
    }
    totalCost(sb);
    sb.append(divEnd());
  }

  private void renderSection(StringBuilder sb, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    UserInput input = new UserInput.Builder().category(categoryId).item(itemId).message(messageId).inline(true).build();
    String prevSectionBtnVal = createSectionBtnVal(input, navigation.getPrevSection());
    String nextSectionBtnVal = createSectionBtnVal(input, navigation.getNextSection());

    int itemsSize = itemDescriptions.size();
    List<List<Item>> partitions = new LinkedList<>();
    for (int i = 0; i < itemsSize; i += PARTITION_SIZE) {
      partitions.add(itemDescriptions.subList(i, Math.min(i + PARTITION_SIZE, itemsSize)));
    }

    int countPartition = navigation.getCurrSection() * PARTITION_SIZE;

    if (navigation.getCurrSection() != 0 && navigation.getCurrSection() < navigation.getSectionsCount() - 1){
      sb.append("...");
      sb.append(br());
    }

    List<Item> part = partitions.get(navigation.getCurrSection());
    for (Item item : part) {
      Price price = item.getPrice();
      sb.append(++countPartition).append(". ").append(item.getName()).append(" x ").append(itemQuantities.get(item.getId())).append(", ")
        .append(PriceUtils.convert(Integer.parseInt(price.getPrice()))).append(" ").append("RUB").append(" - ").append(bundle.getString("per.unit"));
      sb.append(br());
    }

    if (navigation.getCurrSection() != navigation.getSectionsCount() - 1){
      sb.append("...");
      sb.append(br());
    }

    totalCost(sb);
    sb.append(divEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(prevSectionBtnVal, "&lt;", requestParams.getPluginParams(), ctxPath, "/cart", urlResolver));
    sb.append(button(nextSectionBtnVal, "&gt;", requestParams.getPluginParams(), ctxPath, "/cart", urlResolver));
    sb.append(buttonsEnd());
  }

}
