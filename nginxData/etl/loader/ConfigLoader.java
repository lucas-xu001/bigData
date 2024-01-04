package com.root.etl.loader;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import cn.hutool.setting.Setting;
import com.root.etl.bean.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
  private static Config config;
  
  public static Config getConfig() {
    return config;
  }
  
  private static final Logger logger = LoggerFactory.getLogger(ConfigLoader.class);
  
  public static void loadConfig(String settingPath) {
    logger.info("" + settingPath);
    config = new Config();
    Setting setting = new Setting(settingPath);
    config.setJdbcUrl(setting.getStr("jdbcUrl"));
    logger.info("jdbcUrl: " + config.getJdbcUrl());
    config.setUser(setting.getStr("user"));
    logger.info("user: " + config.getUser());
    config.setPassword(setting.getStr("password"));
    logger.info("password: " + config.getPassword());
    config.setDatabase(setting.getStr("database"));
    logger.info("database: " + config.getDatabase());
    config.setDriver(setting.getStr("driver"));
    logger.info("driver: " + config.getDriver());
    config.setTargetPath(setting.getStr("targetPath"));
    logger.info("targetPath: " + config.getTargetPath());
    config.setFileName(setting.getStr("fileName"));
    logger.info("fileName: " + config.getFileName());
    config.setBaseDataNum(setting.getInt("baseDataNum"));
    logger.info("baseDataNum: " + config.getBaseDataNum());
    config.setThreadNum(setting.getInt("threadNum"));
    logger.info("threadNum: " + config.getThreadNum());
    config.setStartTime(setting.getStr("startTime"));
    logger.info("startTime: " + config.getStartTime() + " " + config.getStartTimeTs());
    config.setEndTime(setting.getStr("endTime"));
    logger.info("endTime: " + config.getEndTime() + " " + config.getEndTimeTs());
    config.setCycleBrowser(setting.getDouble("cycleBrowser", Double.valueOf(0.0D)));
    logger.info("cycleBrowser: " + config.getCycleBrowser());
    config.setCycleDevice(setting.getDouble("cycleDevice", Double.valueOf(0.0D)));
    logger.info("cycleDevice: " + config.getCycleDevice());
    config.setFastFixedIpBrowser(setting.getDouble("fastFixedIpBrowser", Double.valueOf(0.0D)));
    logger.info("fastFixedIpBrowser: " + config.getFastFixedIpBrowser());
    config.setFastFixedIdDevice(setting.getDouble("fastFixedIdDevice", Double.valueOf(0.0D)));
    logger.info("fastFixedIdDevice: " + config.getFastFixedIdDevice());
    config.setNormalBrowser(setting.getDouble("normalBrowser", Double.valueOf(0.0D)));
    logger.info("normalBrowser: " + config.getNormalBrowser());
    config.setNormalDevice(setting.getDouble("normalDevice", Double.valueOf(0.0D)));
    logger.info("normalDevice: " + config.getNormalDevice());
    config.setBotBrowser(setting.getDouble("botBrowser", Double.valueOf(0.0D)));
    logger.info("borBrowser: " + config.getBotBrowser());
    config.setLogLevel(setting.getStr("logLevel"));
    logger.info("logLevel: " + config.getLogLevel());
    config.setWhereDataFrom(setting.getBool("whereDataFrom"));
    Logger logback = (Logger)LoggerFactory.getLogger("ROOT");
    switch (config.getLogLevel()) {
      case "DEBUG":
        logback.setLevel(Level.DEBUG);
        return;
      case "INFO":
        logback.setLevel(Level.INFO);
        return;
      case "WARN":
        logback.setLevel(Level.WARN);
      case "ERROR":
        logback.setLevel(Level.DEBUG);
        return;
    } 
    logback.setLevel(Level.INFO);
  }
}
