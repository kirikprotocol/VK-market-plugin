package com.eyelinecom.whoisd.sads2.vk.market;

import com.eyeline.utils.config.ConfigException;
import com.eyeline.utils.config.xml.XmlConfig;
import com.eyeline.utils.config.xml.XmlConfigSection;
import com.eyelinecom.whoisd.sads2.vk.market.services.Services;
import com.eyelinecom.whoisd.sads2.vk.market.services.ServicesException;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * author: Denis Enenko
 * date: 27.06.17
 */
public class InitListener implements ServletContextListener {

  private final static String CONFIG_DIR = "sads-vk-market.config.dir";
  private final static String DEFAULT_CONFIG_DIR = "conf";
  private final static String CONFIG_FILE_NAME = "config.xml";


  @Override
  public void contextInitialized(ServletContextEvent servletContextEvent) {
    final File configDir = getConfigDir();

    initLog4j(configDir);

    XmlConfigSection cfg = loadXmlConfig(configDir);
    String pushUrl = getPushUrl(cfg);
    Services services = initServices(cfg);

    WebContext.init(services, pushUrl);
  }

  private File getConfigDir() {
    String configDir = System.getProperty(CONFIG_DIR);

    if(configDir == null) {
      configDir = DEFAULT_CONFIG_DIR;
      System.err.println("System property '" + CONFIG_DIR + "' is not set. Using default value: " + configDir);
    }

    File cfgDir = new File(configDir);

    if(!cfgDir.exists())
      throw new RuntimeException("Config directory '" + cfgDir.getAbsolutePath() + "' does not exist");

    System.out.println("Using config directory '" + cfgDir.getAbsolutePath() + "'");

    return cfgDir;
  }

  private XmlConfigSection loadXmlConfig(File configDir) {
    final File cfgFile = new File(configDir, CONFIG_FILE_NAME);
    XmlConfigSection result;
    try {
      XmlConfig cfg = new XmlConfig();
      cfg.load(cfgFile);
      result = cfg.getSection("vk.market.plugin");
    } catch (ConfigException e) {
      throw new RuntimeException("Unable to load config.xml", e);
    }
    return result;
  }

  private String getPushUrl(XmlConfigSection config) {
    try {
      return config.getString("push.url");
    }
    catch(ConfigException e) {
      throw new RuntimeException("Parameter deploy.url is not found.", e);
    }
  }

  private void initLog4j(File configDir) {
    final File log4jProps = new File(configDir, "log4j.properties");
    System.out.println("Log4j conf file: " + log4jProps.getAbsolutePath() + ", exists: " + log4jProps.exists());
    PropertyConfigurator.configureAndWatch(log4jProps.getAbsolutePath(), TimeUnit.MINUTES.toMillis(1));
  }

  private Services initServices(XmlConfigSection config) {
    try {
      return new Services(config);
    }
    catch(ServicesException e) {
      throw new RuntimeException("Can't init services", e);
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
  }

}