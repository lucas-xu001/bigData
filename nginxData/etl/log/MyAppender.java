package com.atguigu.etl.log;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.encoder.Encoder;
import cn.hutool.core.io.file.PathUtil;
import com.atguigu.etl.loader.ConfigLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

public class MyAppender {
  public static final Logger logger = LoggerFactory.getLogger(MyAppender.class);
  
  public static void addAppender() {
    String fileName = ConfigLoader.getConfig().getFileName();
    String targetPath = ConfigLoader.getConfig().getTargetPath();
    if (!targetPath.endsWith("/"))
      targetPath = targetPath + "/"; 
    Path path = Paths.get(targetPath, new String[] { fileName });
    Path abs = PathUtil.toAbsNormal(path);
    fileName = abs.toString();
    MyAppender.logger.info("" + fileName);
    LoggerContext lc = (LoggerContext)StaticLoggerBinder.getSingleton().getLoggerFactory();
    Logger logger = LoggerFactory.getLogger("com.atguigu.etl.behaviors");
    Logger logger1 = (Logger)logger;
    FileAppender<ILoggingEvent> fileAppender = new FileAppender();
    fileAppender.setContext((Context)lc);
    fileAppender.setName("FILE");
    fileAppender.setAppend(true);
    fileAppender.setFile(fileName);
    PatternLayoutEncoder encoder = new PatternLayoutEncoder();
    encoder.setContext((Context)lc);
    encoder.setPattern("%msg%n");
    fileAppender.setEncoder((Encoder)encoder);
    encoder.start();
    DataFilter dataFilter = new DataFilter();
    dataFilter.setContext((Context)lc);
    fileAppender.addFilter(dataFilter);
    dataFilter.start();
    fileAppender.start();
    logger1.addAppender((Appender)fileAppender);
  }
}
