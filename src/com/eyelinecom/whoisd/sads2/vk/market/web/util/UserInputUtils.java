package com.eyelinecom.whoisd.sads2.vk.market.web.util;

import com.eyelinecom.whoisd.sads2.vk.market.web.model.UserInput;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * author: Artem Voronov
 */
public class UserInputUtils {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  static {
    MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
  }

  public static UserInput decodeAndParse(String encodedJson, String userId) throws IOException {
    UserInput result = MAPPER.readValue(URLDecoder.decode(encodedJson, StandardCharsets.UTF_8.name()), UserInput.class);

    if (result.getMessageId() == null)
      result.setMessageId(generateMessageId(userId));

    if (result.getFromInlineButton() == null)
      result.setFromInlineButton(false);

    return result;
  }

  public static String toJsonAndEncode(UserInput userInput) throws IOException {
    return URLEncoder.encode(MAPPER.writeValueAsString(userInput), StandardCharsets.UTF_8.name());
  }

  private static String generateMessageId(String userId) {
    return userId + "-" + System.currentTimeMillis();
  }

  public static String json() throws IOException {
    return "%7B%7D";
  }

  public static String json(Integer categoryId) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).build();
    return toJsonAndEncode(userInput);
  }

  public static String json(String messageId) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.message(messageId).build();
    return toJsonAndEncode(userInput);
  }

  public static String json(Integer categoryId, Integer itemId, String messageId) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).item(itemId).message(messageId).build();
    return toJsonAndEncode(userInput);
  }

  public static String json(Integer categoryId, Integer itemId, String messageId, Integer extraPhotoId) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).item(itemId).message(messageId).photo(extraPhotoId).build();
    return toJsonAndEncode(userInput);
  }

  public static String json(Integer categoryId, Integer itemId, String messageId, Boolean fromInlineButton) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).item(itemId).message(messageId).inline(fromInlineButton).build();
    return toJsonAndEncode(userInput);
  }

  public static String json(Integer categoryId, Integer itemId, String messageId, Integer extraPhotoId, Integer quantity) throws IOException {
    UserInput.Builder builder = new UserInput.Builder();
    UserInput userInput = builder.category(categoryId).item(itemId).message(messageId).photo(extraPhotoId).quantity(quantity).build();
    return toJsonAndEncode(userInput);
  }
}
