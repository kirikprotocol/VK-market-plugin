package com.eyelinecom.whoisd.sads2.vk.market.services.notification;

import com.eyelinecom.whoisd.sads2.vk.market.web.model.Order;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * author: Artem Voronov
 */
class TemplateService {
  private final static Logger logger = Logger.getLogger("TEMPLATE_SERVICE");
  private final Configuration configuration;

  private enum Templates {
    ORDER("order");

    private final String name;

    Templates(String name) {
      this.name = name;
    }
  }

  TemplateService() {
    configuration = new Configuration() {{
      setOutputEncoding("UTF-8");
      setNumberFormat("0.####");
    }};

    configuration.setClassForTemplateLoading(TemplateService.class, "templates");
  }

  MailEntity getOrderInfoTemplate(Locale locale, Order order) {
    String templateName = getTemplateName(Templates.ORDER, locale);
    Map<String, Object> parameters = new HashMap<>();
    parameters.put("order", order);
    return getMail(templateName, parameters);
  }

  private MailEntity getMail(String templateName, Map<String, Object> parameters) {
    try {
      String mailText = processTemplate(templateName, parameters);

      int subjInd = mailText.indexOf('\n');
      String subject = mailText.substring(0, subjInd);
      String message = mailText.substring(subjInd + 1);

      return new MailEntity(subject, message);
    }
    catch (Exception e) {
      logger.error(e, e);
      return null;
    }
  }

  private static String getTemplateName(Templates template, Locale locale) {
    return template.name + "_" + locale.getLanguage() + ".ftl";
  }

  private Template loadTemplate(String templateName) throws IOException {
    return configuration.getTemplate(templateName, "UTF-8");
  }

  private String processTemplate(String templateName, Map<String, Object> parameters) {
    try {
      final Writer writer = new StringWriter();
      Template template = loadTemplate(templateName);
      template.process(parameters, writer);
      return writer.toString();
    }
    catch (TemplateException e) {
      throw new RuntimeException("Invalid template: " + templateName, e);
    }
    catch (IOException e) {
      throw new RuntimeException("Error during form generation using template: " + templateName, e);
    }
  }
}
