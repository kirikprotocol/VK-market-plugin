package com.eyelinecom.whoisd.sads2.vk.market.services.market;

import com.eyelinecom.whoisd.sads2.vk.market.model.cart.Cart;
import com.eyelinecom.whoisd.sads2.vk.market.model.order.Order;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.market.MarketCategory;
import com.vk.api.sdk.objects.market.MarketItem;
import com.vk.api.sdk.objects.market.MarketItemFull;
import com.vk.api.sdk.objects.market.responses.GetByIdExtendedResponse;
import com.vk.api.sdk.objects.market.responses.GetByIdResponse;
import com.vk.api.sdk.objects.market.responses.GetResponse;
import com.vk.api.sdk.objects.photos.Photo;

import java.util.*;
import java.util.stream.Collectors;

/**
 * author: Denis Enenko
 * date: 29.06.17
 */
public class VkMarketService {

  private final VkApiClient vk;
  private final UserActor actor;


  public VkMarketService(Integer userId, String accessToken) {
    TransportClient transportClient = HttpTransportClient.getInstance();
    this.vk = new VkApiClient(transportClient);
    this.actor = new UserActor(userId, accessToken);
  }

  public Set<Category> getCategories() throws VkMarketServiceException {
    Set<Category> categories = new HashSet<>();

    try {
      GetResponse response = vk.market().get(actor, actor.getId()).execute();

      for(MarketItem item : response.getItems()) {
        MarketCategory category = item.getCategory();
        categories.add(new Category(category.getId(), category.getName()));
      }
    }
    catch(Exception e) {
      throw new VkMarketServiceException(e.getMessage(), e);
    }

    return categories;
  }

  public List<Item> getItems() throws VkMarketServiceException {
    List<Item> items = new ArrayList<>();

    try {
      GetResponse response = vk.market().get(actor, actor.getId()).execute();

      for(MarketItem item : response.getItems()) {
        items.add(new Item(
            item.getId(),
            new Category(item.getCategory().getId(), item.getCategory().getName()),
            item.getTitle(),
            new Price(item.getPrice().getAmount(), item.getPrice().getCurrency().getName(), item.getPrice().getText()),
            item.getThumbPhoto()
        ));
      }
    }
    catch(Exception e) {
      throw new VkMarketServiceException(e.getMessage(), e);
    }

    return items;
  }

  public List<Item> getItemsByCategory(int categoryId) throws VkMarketServiceException {
    List<Item> items = new ArrayList<>();

    try {
      GetResponse response = vk.market().get(actor, actor.getId()).execute();

      for(MarketItem item : response.getItems()) {
        if(!item.getCategory().getId().equals(categoryId))
          continue;

        items.add(new Item(
            item.getId(),
            new Category(item.getCategory().getId(), item.getCategory().getName()),
            item.getTitle(),
            new Price(item.getPrice().getAmount(), item.getPrice().getCurrency().getName(), item.getPrice().getText()),
            item.getThumbPhoto()
        ));
      }
    }
    catch(Exception e) {
      throw new VkMarketServiceException(e.getMessage(), e);
    }

    return items;
  }

  public ItemDetailed getItemById(int itemId) throws VkMarketServiceException {
    try {
      GetByIdExtendedResponse response = vk.market().getByIdExtended(actor, actor.getId() + "_" + itemId).execute();
      if(response.getCount() == 0)
        return null;

      MarketItemFull item = response.getItems().get(0);

      return new ItemDetailed(
          item.getId(),
          new Category(item.getCategory().getId(), item.getCategory().getName()),
          item.getTitle(),
          item.getDescription(),
          new Price(item.getPrice().getAmount(), item.getPrice().getCurrency().getName(), item.getPrice().getText()),
          item.getThumbPhoto(),
          getExtraPhotos(item)
      );
    }
    catch(Exception e) {
      throw new VkMarketServiceException(e.getMessage(), e);
    }
  }

  public List<Item> getItemsById(List<Integer> itemIds) throws VkMarketServiceException {
    try {
      List<String> itemIdsParam = itemIds.stream().map(it-> actor.getId() + "_" + it).collect(Collectors.toList());
      GetByIdResponse response = vk.market().getById(actor, itemIdsParam).execute();
      if(response.getCount() == 0)
        return null;

      List<MarketItem> marketItems = response.getItems();
      return marketItems.stream().map(VkMarketService::convert).collect(Collectors.toList());
    }
    catch(Exception e) {
      throw new VkMarketServiceException(e.getMessage(), e);
    }
  }

  public List<Item> getItemsById(Cart userCart) throws VkMarketServiceException {
    if (userCart.isEmpty())
      return Collections.emptyList();

    List<Integer> itemIds = userCart.getItems().stream().map(it->it.getVkItemId()).collect(Collectors.toList());

    return getItemsById(itemIds);
  }

  public List<Item> getItemsById(Order order) throws VkMarketServiceException {
    if (order.isEmpty())
      return Collections.emptyList();

    List<Integer> itemIds = order.getItems().stream().map(it->it.getVkItemId()).collect(Collectors.toList());

    return getItemsById(itemIds);
  }

  private static List<String> getExtraPhotos(MarketItemFull item) {
    List<String> photos = new ArrayList<>();

    for(Photo photo : item.getPhotos()) {
      if(photo.getPhoto2560() != null)
        photos.add(photo.getPhoto2560());
      else if(photo.getPhoto1280() != null)
        photos.add(photo.getPhoto1280());
      else if(photo.getPhoto807() != null)
        photos.add(photo.getPhoto807());
      else if(photo.getPhoto604() != null)
        photos.add(photo.getPhoto604());
      else if(photo.getPhoto130() != null)
        photos.add(photo.getPhoto130());
      else if(photo.getPhoto75() != null)
        photos.add(photo.getPhoto75());
    }

    return photos;
  }

  private static Item convert(MarketItem marketItem) {
    return new Item(
      marketItem.getId(),
      new Category(marketItem.getCategory().getId(), marketItem.getCategory().getName()),
      marketItem.getTitle(),
      new Price(marketItem.getPrice().getAmount(), marketItem.getPrice().getCurrency().getName(), marketItem.getPrice().getText()),
      marketItem.getThumbPhoto()
    );
  }

}