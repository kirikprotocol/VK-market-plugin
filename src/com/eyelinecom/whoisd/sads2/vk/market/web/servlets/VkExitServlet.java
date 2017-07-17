package com.eyelinecom.whoisd.sads2.vk.market.web.servlets;

import com.eyelinecom.whoisd.sads2.vk.market.web.Protocol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * author: Denis Enenko
 * date: 13.07.17
 */
public class VkExitServlet extends VkHttpServlet {

  protected void handleRequest(HttpServletRequest request, HttpServletResponse response, Protocol protocol, RequestParameters params) throws IOException {
    sendRedirect(response, params.getExitUrl());
  }

}
