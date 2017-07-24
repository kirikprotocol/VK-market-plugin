package com.eyelinecom.whoisd.sads2.vk.market.web.util;


/**
 * author: Artem Voronov
 */
public class UserInputParser {

  public static UserInput parse(String val, String userId) {
    int idx0 = val.indexOf("_");
    if(idx0 == -1) {
      return new UserInput(Integer.parseInt(val), null, userId + "-" + System.currentTimeMillis(), null, null);
    }
    String categoryId = val.substring(0, idx0);

    int idx1 = val.indexOf("_", idx0 + 1);
    String itemId = val.substring(idx0 + 1, idx1);

    int idx2 = val.indexOf("_", idx1 + 1);

    String messageId;
    if(idx2 == -1) {
      messageId = val.substring(idx1 + 1, val.length());
      return new UserInput(Integer.parseInt(categoryId), Integer.parseInt(itemId), messageId, null, null);
    }

    messageId = val.substring(idx1 + 1, idx2);

    int idx3 = val.indexOf("_", idx2 + 1);

    String extraPhotoId;
    if(idx3 == -1) {
      extraPhotoId = val.substring(idx2 + 1, val.length());
      return new UserInput(Integer.parseInt(categoryId), Integer.parseInt(itemId), messageId, Integer.parseInt(extraPhotoId), null);
    }

    extraPhotoId = val.substring(idx2 + 1, idx3);
    String quantity = val.substring(idx3 + 1, val.length());

    return new UserInput(Integer.parseInt(categoryId), Integer.parseInt(itemId), messageId, Integer.parseInt(extraPhotoId), Integer.parseInt(quantity));
  }
}
