package com.eyelinecom.whoisd.sads2.vk.market.web.util;

/**
 * author: Artem Voronov
 */
public class UserInput {
  private final int categoryId;
  private final Integer itemId;
  private final String messageId;
  private final Integer extraPhotoId;
  private final Integer quantity;

  public UserInput(int categoryId, Integer itemId, String messageId, Integer extraPhotoId, Integer quantity) {
    this.categoryId = categoryId;
    this.itemId = itemId;
    this.messageId = messageId;
    this.extraPhotoId = extraPhotoId;
    this.quantity = quantity;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public Integer getItemId() {
    return itemId;
  }

  public String getMessageId() {
    return messageId;
  }

  public Integer getExtraPhotoId() {
    return extraPhotoId;
  }

  public Integer getQuantity() {
    return quantity;
  }
}
