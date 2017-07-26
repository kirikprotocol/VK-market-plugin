package com.eyelinecom.whoisd.sads2.vk.market.web.model;

/**
 * author: Artem Voronov
 */
public class UserInput {
  private Integer categoryId;
  private Integer itemId;
  private String messageId;
  private Integer extraPhotoId;
  private Integer quantity;
  private Boolean fromInlineButton;

  public UserInput() {
  }

  public Integer getCategoryId() {
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

  public Boolean getFromInlineButton() {
    return fromInlineButton;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public void setItemId(Integer itemId) {
    this.itemId = itemId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public void setExtraPhotoId(Integer extraPhotoId) {
    this.extraPhotoId = extraPhotoId;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public void setFromInlineButton(Boolean fromInlineButton) {
    this.fromInlineButton = fromInlineButton;
  }

  public static class Builder {
    private UserInput userInput = new UserInput();

    public Builder category(Integer categoryId) {
      userInput.setCategoryId(categoryId);
      return this;
    }

    public Builder item(Integer itemId) {
      userInput.setItemId(itemId);
      return this;
    }

    public Builder message(String messageId) {
      userInput.setMessageId(messageId);
      return this;
    }

    public Builder photo(Integer extraPhotoId) {
      userInput.setExtraPhotoId(extraPhotoId);
      return this;
    }

    public Builder quantity(Integer quantity) {
      userInput.setQuantity(quantity);
      return this;
    }

    public Builder inline(Boolean inline) {
      userInput.setFromInlineButton(inline);
      return this;
    }

    public UserInput build() {
      return userInput;
    }

  }
}
