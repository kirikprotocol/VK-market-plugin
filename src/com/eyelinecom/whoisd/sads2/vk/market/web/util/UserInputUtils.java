package com.eyelinecom.whoisd.sads2.vk.market.web.util;

import com.eyelinecom.whoisd.sads2.vk.market.services.model.UserInput;
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

  public static String empty() throws IOException {
    return "%7B%7D";
  }

}
