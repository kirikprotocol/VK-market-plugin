package com.eyelinecom.whoisd.sads2.vk.market.web.renderers.telegram;

import com.eyelinecom.whoisd.sads2.vk.market.services.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.services.model.UserInput;
import com.eyelinecom.whoisd.sads2.vk.market.services.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.renderers.Renderer;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.NavigationSections;
import com.eyelinecom.whoisd.sads2.vk.market.web.util.UserInputUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * author: Artem Voronov
 */
public class ChooseItemInCartForDeletingTelegramRenderer extends Renderer {

  private static final int BUTTONS_PER_ROW = 2;
  private static final int PARTITION_SIZE = 6;

  private final ResourceBundle bundle;
  private final List<Item> itemDescriptions;
  private final String messageId;
  private final Integer itemId;
  private final Integer categoryId;
  private final NavigationSections navigation;

  public ChooseItemInCartForDeletingTelegramRenderer(Locale locale, List<Item> itemDescriptions, String messageId, Integer categoryId, Integer itemId, Integer cartListSection) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.itemDescriptions = itemDescriptions;
    this.messageId = messageId;
    this.categoryId = categoryId;
    this.itemId = itemId;
    this.navigation = new NavigationSections(cartListSection, itemDescriptions.size(), PARTITION_SIZE);
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String chooseItemPage = getChooseItemPage(ctxPath, requestParams, urlResolver);

    sendResponse(response, requestParams, Arrays.asList(chooseItemPage));
  }

  private String getChooseItemPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    String backBtnVal = UserInputUtils.toJsonAndEncode(new UserInput.Builder().category(categoryId).item(itemId).message(messageId).inline(true).build());


    sb.append(pageStart(getEditablePageAttrs(messageId, true)));
    sb.append(divStart());
    sb.append(bundle.getString("choose.item"));
    sb.append(divEnd());

    int itemsCount = itemDescriptions.size();

    if (itemsCount <= PARTITION_SIZE) {
      renderOnePart(itemDescriptions, sb, ctxPath, requestParams, urlResolver);
    } else {
      renderAllParts(sb, ctxPath, requestParams, urlResolver);
    }

    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(backBtnVal, bundle.getString("back.to.cart"), requestParams.getPluginParams(), ctxPath, "/cart", urlResolver));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    return sb.toString();
  }

  private static String createChooseBtnVal(UserInput input, Integer categoryId, Integer itemId) throws IOException {
    input.setCategoryId(categoryId);
    input.setItemId(itemId);
    return UserInputUtils.toJsonAndEncode(input);
  }

  private void renderOnePart(List<Item> partition, StringBuilder sb, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    UserInput input = new UserInput.Builder().message(messageId).build();
    int counter = 0;

    for (Item item : partition) {
      if (counter == 0)
        sb.append(buttonsStart(getInlineButtonsAttrs()));

      sb.append(button(createChooseBtnVal(input, item.getCategory().getId(), item.getId()), item.getName(), requestParams.getPluginParams(), ctxPath, "/delete-from-cart", urlResolver));
      counter++;

      if (counter == BUTTONS_PER_ROW) {
        sb.append(buttonsEnd());
        counter = 0;
      }
    }

    if (counter != 0)
      sb.append(buttonsEnd());
  }

  private void renderAllParts(StringBuilder sb, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    UserInput input = new UserInput.Builder().category(categoryId).item(itemId).message(messageId).build();
    String prevSectionBtnVal = createSectionBtnVal(input, navigation.getPrevSection());
    String nextSectionBtnVal = createSectionBtnVal(input, navigation.getNextSection());

    int itemsSize = itemDescriptions.size();
    List<List<Item>> partitions = new LinkedList<>();
    for (int i = 0; i < itemsSize; i += PARTITION_SIZE) {
      partitions.add(itemDescriptions.subList(i, Math.min(i + PARTITION_SIZE, itemsSize)));
    }

    List<Item> part = partitions.get(navigation.getCurrSection());
    renderOnePart(part, sb, ctxPath, requestParams, urlResolver);

    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(prevSectionBtnVal, "&lt;", requestParams.getPluginParams(), ctxPath, "/choose-item-in-cart", urlResolver));
    sb.append(button(nextSectionBtnVal, "&gt;", requestParams.getPluginParams(), ctxPath, "/choose-item-in-cart", urlResolver));
    sb.append(buttonsEnd());
  }

  private static String createSectionBtnVal(UserInput input, Integer cartSection) throws IOException {
    input.setCartListSection(cartSection);
    return UserInputUtils.toJsonAndEncode(input);
  }

}
