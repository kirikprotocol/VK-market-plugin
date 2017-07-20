package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * author: Denis Enenko
 * date: 13.07.17
 */
public class RequestParameters {

  private final String userId;
  private final String userInput;
  private final String protocol;
  private final int vkUserId;
  private final String vkAccessToken;
  private final Locale locale;
  private final String exitUrl;
  private final String merchantEmail;
  private final String serviceId;


  RequestParameters(HttpServletRequest request) throws HttpServletRequestException {
    userId = getUserId(request);
    userInput = request.getParameter("user_input");
    protocol = getRequiredParameter(request, "protocol");
    vkUserId = getVkUserId(request);
    vkAccessToken = getRequiredParameter(request, "vk_access_token");
    locale = getLocale(request);
    exitUrl = getRequiredParameter(request, "exit_url");
    merchantEmail = getRequiredParameter(request, "merchant_email");
    serviceId = getRequiredParameter(request, "service");
  }

  public String getUserId() {
    return userId;
  }

  public String getUserInput() {
    return userInput;
  }

  public int getVkUserId() {
    return vkUserId;
  }

  public String getVkAccessToken() {
    return vkAccessToken;
  }

  public Locale getLocale() {
    return locale;
  }

  public String getExitUrl() {
    return exitUrl;
  }

  public String getProtocol() {
    return protocol;
  }

  public String getMerchantEmail() {
    return merchantEmail;
  }

  public String getServiceId() {
    return serviceId;
  }

  public Map<String, String> getPluginParams() {
    Map<String, String> pluginParams = new HashMap<>();

    pluginParams.put("vk_user_id", vkUserId + "");
    pluginParams.put("vk_access_token", vkAccessToken);
    pluginParams.put("merchant_email", merchantEmail);
    pluginParams.put("service", serviceId);
    pluginParams.put("locale", locale.getLanguage());
    pluginParams.put("exit_url", exitUrl);

    return pluginParams;
  }

  private static String getUserId(HttpServletRequest request) throws HttpServletRequestException {
    String userId = request.getParameter("user_id");

    if(userId == null) {
      userId = request.getParameter("subscriber");
      if(userId == null)
        throw new HttpServletRequestException("No \"user_id\" parameter", HttpServletResponse.SC_BAD_REQUEST);
    }

    return userId;
  }

  private static int getVkUserId(HttpServletRequest request) throws HttpServletRequestException {
    String vkUserId = getRequiredParameter(request, "vk_user_id");

    try {
      return Integer.parseInt(vkUserId);
    }
    catch(NumberFormatException e) {
      throw new HttpServletRequestException("Invalid \"vk_user_id\" parameter: " + vkUserId, HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  private static Locale getLocale(HttpServletRequest request) {
    String locale = request.getParameter("locale");

    if(locale == null)
      locale = "en";

    return new Locale(locale);
  }

  private static String getRequiredParameter(HttpServletRequest request, String name) throws HttpServletRequestException {
    String value = request.getParameter(name);

    if(value == null)
      throw new HttpServletRequestException("No \"" + name + "\" parameter", HttpServletResponse.SC_BAD_REQUEST);

    if(value.isEmpty())
      throw new HttpServletRequestException("Empty \"" + name + "\" parameter", HttpServletResponse.SC_BAD_REQUEST);

    return value;
  }

}
