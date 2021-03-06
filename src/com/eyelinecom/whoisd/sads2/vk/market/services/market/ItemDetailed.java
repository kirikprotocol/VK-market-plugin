package com.eyelinecom.whoisd.sads2.vk.market.services.market;

import java.util.List;

/**
 * author: Denis Enenko
 * date: 29.06.17
 */
public class ItemDetailed extends Item {

  private final String description;
  private final List<String> extraPhotoUrls;


  ItemDetailed(Integer id, Category category, String name, String description, Price price, String mainPhotoUrl, List<String> extraPhotoUrls) {
    super(id, category, name, price, mainPhotoUrl);
    this.description = description;
    this.extraPhotoUrls = extraPhotoUrls;
  }

  public String getDescription() {
    return description;
  }

  public List<String> getExtraPhotoUrls() {
    return extraPhotoUrls;
  }

}
