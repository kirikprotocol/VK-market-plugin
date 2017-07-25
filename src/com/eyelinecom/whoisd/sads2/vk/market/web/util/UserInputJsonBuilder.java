package com.eyelinecom.whoisd.sads2.vk.market.web.util;

import java.io.IOException;

/**
 * author: Artem Voronov
 */
public class UserInputJsonBuilder {

  public static String json() throws IOException {
    return "%7B%7D";
  }

  public static String json(Integer categoryId) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).build();
    return UserInputParser.toJsonAndEncode(userInput);
  }

  public static String json(String messageId) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.message(messageId).build();
    return UserInputParser.toJsonAndEncode(userInput);
  }

  public static String json(Integer categoryId, Integer itemId, String messageId) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).item(itemId).message(messageId).build();
    return UserInputParser.toJsonAndEncode(userInput);
  }

  public static String json(Integer categoryId, Integer itemId, String messageId, Integer extraPhotoId) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).item(itemId).message(messageId).photo(extraPhotoId).build();
    return UserInputParser.toJsonAndEncode(userInput);
  }

  public static String json(Integer categoryId, Integer itemId, String messageId, Boolean fromInlineButton) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).item(itemId).message(messageId).inline(fromInlineButton).build();
    return UserInputParser.toJsonAndEncode(userInput);
  }

  public static String json(Integer categoryId, Integer itemId, String messageId, Integer extraPhotoId, Integer quantity) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).item(itemId).message(messageId).photo(extraPhotoId).quantity(quantity).build();
    return UserInputParser.toJsonAndEncode(userInput);
  }

}


