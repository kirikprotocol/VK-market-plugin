package com.eyelinecom.whoisd.sads2.vk.market.service.market;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.market.MarketCategory;
import com.vk.api.sdk.objects.market.MarketItem;
import com.vk.api.sdk.objects.market.MarketItemFull;
import com.vk.api.sdk.objects.market.responses.GetByIdExtendedResponse;
import com.vk.api.sdk.objects.market.responses.GetResponse;
import com.vk.api.sdk.objects.photos.Photo;

import java.util.*;

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

}