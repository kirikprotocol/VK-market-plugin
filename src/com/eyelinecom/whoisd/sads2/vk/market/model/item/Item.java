package com.eyelinecom.whoisd.sads2.vk.market.model.item;

import javax.persistence.*;
import javax.validation.constraints.Min;

/**
 * author: Artem Voronov
 */
@MappedSuperclass
@Table(name = "item")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Item {

  @Column(name="vk_item_id", unique = false, nullable = false)
  private Integer vkItemId;

  @Column(name="quantity", unique = false, nullable = false)
  @Min(1)
  private Integer quantity;

  public Integer getVkItemId() {
    return vkItemId;
  }

  public void setVkItemId(Integer vkItemId) {
    this.vkItemId = vkItemId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }
}
