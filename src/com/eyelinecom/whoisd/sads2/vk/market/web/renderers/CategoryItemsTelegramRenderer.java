package com.eyelinecom.whoisd.sads2.vk.market.web.renderers;

import com.eyelinecom.whoisd.sads2.vk.market.service.market.Item;
import com.eyelinecom.whoisd.sads2.vk.market.service.shorturl.UrlResolver;
import com.eyelinecom.whoisd.sads2.vk.market.web.servlets.RequestParameters;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * author: Denis Enenko
 * date: 14.07.17
 */
public class CategoryItemsTelegramRenderer extends Renderer {

  private final ResourceBundle bundle;
  private final List<Item> items;
  private final String messageId;
  private final Integer itemId;
  private final Navigation navigation;


  public CategoryItemsTelegramRenderer(List<Item> items, String messageId, Integer itemId, Locale locale) {
    super(locale);
    this.bundle = ResourceBundle.getBundle(getClass().getName(), locale);
    this.items = items;
    this.messageId = messageId;
    this.itemId = itemId;
    this.navigation = new Navigation();
  }

  @Override
  public void render(HttpServletResponse response, String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    String itemPage = getItemPage(ctxPath, requestParams, urlResolver);
    String menuPage = getMenuPage();

    sendResponse(response, Arrays.asList(itemPage, menuPage));
  }

  private String getItemPage(String ctxPath, RequestParameters requestParams, UrlResolver urlResolver) throws IOException {
    StringBuilder sb = new StringBuilder();

    sb.append(pageStart(getEditablePageAttrs()));
    sb.append(divStart());
    sb.append(navigation.currItem.getMainPhotoUrl());
    sb.append(br());
    sb.append(bStart()).append(navigation.currItem.getName()).append(bEnd());
    sb.append(br());
    sb.append(navigation.currItem.getPrice().getText());
    sb.append(br());
    sb.append(divEnd());
    sb.append(buttonsStart(getInlineButtonsAttrs()));
    sb.append(button(navigation.prevItem.getCategory().getId() + "_" + navigation.prevItem.getId() + "_" + messageId, "&lt;", requestParams.getPluginParams(), ctxPath, "/category", urlResolver));
    sb.append(button(navigation.currItem.getCategory().getId() + "_" + navigation.currItem.getId() + "_" + messageId, bundle.getString("item.details.btn"), requestParams.getPluginParams(), ctxPath, "/item-details", urlResolver));
    sb.append(button(navigation.currItem.getCategory().getId() + "_" + navigation.currItem.getId() + "_" + messageId, bundle.getString("add.to.cart.btn"), requestParams.getPluginParams(), ctxPath, "/add-to-cart", urlResolver));
    sb.append(button(navigation.nextItem.getCategory().getId() + "_" + navigation.nextItem.getId() + "_" + messageId, "&gt;", requestParams.getPluginParams(), ctxPath, "/category", urlResolver));
    sb.append(buttonsEnd());
    sb.append(pageEnd());

    return sb.toString();
  }

  private String getMenuPage() throws IOException {
    StringBuilder sb = new StringBuilder();

    //todo: Страницу с обычными кнопками перехода к выбору категории, корзине, выходу из плагина...

    return sb.toString();
  }

  private Map<String, String> getEditablePageAttrs() {
    Map<String, String> attrs = new HashMap<>();

    attrs.put("telegram.message.id", messageId);
    attrs.put("telegram.message.edit", String.valueOf(itemId != null));
    attrs.put("telegram.keep.session", "true");
    attrs.put("telegram.links.realignment.enabled", "false");

    return attrs;
  }

  private static Map<String, String> getInlineButtonsAttrs() {
    Map<String, String> attrs = new HashMap<>();

    attrs.put("telegram.inline", "true");

    return attrs;
  }

  private class Navigation {

    private final Item prevItem;
    private final Item currItem;
    private final Item nextItem;

    private Navigation() {
      currItem = findCurrentItem();

      int currItemIndex = itemId == null ? 0 : items.indexOf(currItem);
      int prevItemIndex = currItemIndex == 0 ? items.size() - 1 : currItemIndex - 1;
      int nextItemIndex = currItemIndex == items.size() - 1 ? 0 : currItemIndex + 1;

      prevItem = items.get(prevItemIndex);
      nextItem = items.get(nextItemIndex);
    }

    private Item findCurrentItem() {
      if(itemId == null)
        return items.get(0);

      for(Item item : items) {
        if(item.getId().equals(itemId))
          return item;
      }

      return null;
    }

  }

}