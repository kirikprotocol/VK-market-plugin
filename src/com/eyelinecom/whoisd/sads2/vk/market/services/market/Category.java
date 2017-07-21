package com.eyelinecom.whoisd.sads2.vk.market.services.market;

/**
 * author: Denis Enenko
 * date: 04.07.17
 */
public class Category {

  private final Integer id;
  private final String name;


  Category(Integer id, String name) {
    this.id = id;
    this.name = name;
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if(this == o)
      return true;

    if(o == null || getClass() != o.getClass())
      return false;

    Category category = (Category) o;

    return id.equals(category.id) && name.equals(category.name);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + name.hashCode();
    return result;
  }

}
